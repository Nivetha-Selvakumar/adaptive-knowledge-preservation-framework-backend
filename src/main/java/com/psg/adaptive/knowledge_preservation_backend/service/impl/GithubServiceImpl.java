package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.*;
import com.psg.adaptive.knowledge_preservation_backend.entities.EnterpriseApplicationConnectionEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryAgentEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumAgentStatus;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.EnterpriseApplicationConnectionMapper;
import com.psg.adaptive.knowledge_preservation_backend.mapper.GithubRepositoryMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.*;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubRepositoryAgentService;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import com.psg.adaptive.knowledge_preservation_backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GithubServiceImpl implements GithubService {
    @Value("${github.client-id}")
    String clientId;

    @Value("${github.client-secret}")
    String clientSecret;

    @Value("${github.redirect-uri}")
    String redirectUri;

    @Value("${github.oauth.authorize-uri}")
    String githubAuthorizeUri;

    @Value("${github.oauth.token-uri}")
    String githubTokenUri;

    @Value("${github.user-uri}")
    String githubUserUri;

    @Value("${app.frontend.url}")
    String frontendUrl;

    @Value("${github.oauth.navigation}")
    String githubNavigation;

    @Value("${github.repositories.uri}")
    String githubRepositoriesUri;

    @Value("${github.repository.uri}")
    String githubRepositoryUri;

    @Value("${github.repository.by.id.uri}")
    String githubRepositoryByIdUri;

    @Autowired
    UserRepo userRepository;

    @Autowired
    RepositoryRepo repositoryRepo;

    @Autowired
    RepositoryAgentRepo repositoryAgentRepo;

    @Autowired
    GithubRepositoryAgentService githubRepositoryAgentService;

    @Autowired
    RepositoryActivityRepo repositoryActivityRepo;

    @Autowired
    EnterpriseApplicationConnectionRepo enterpriseApplicationConnectionRepo;

    @Autowired
    JwtUtils jwtUtil;

    @Autowired
    EnterpriseApplicationConnectionMapper enterpriseApplicationConnectionMapper;

    @Autowired
    GithubRepositoryMapper githubRepositoryMapper;

    @Autowired
    GithubAgentClientServiceImpl githubAgentClient;

    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> connect(String authorizationHeader) throws Exception {

        String token = authorizationHeader.substring(7);

        String loggedInEmail = jwtUtil.validateToken(token).getSubject();

        String state = jwtUtil.generateShortLivedToken(loggedInEmail, Duration.ofMinutes(10));

        String url = githubAuthorizeUri + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=repo read:user" + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

        return Map.of("url", url);

    }

    @Override
    public void callback(String code, String state, HttpServletResponse response) throws Exception {

        try {

            String loggedInEmail = jwtUtil.validateAndExtractSubject(state);

            GithubTokenRequest request = new GithubTokenRequest(clientId, clientSecret, code, redirectUri);

            String tokenResponse = restClient.post().uri(githubTokenUri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(request).retrieve().body(String.class);

            JsonNode tokenJson = objectMapper.readTree(tokenResponse);

            if (!tokenJson.has("access_token")) {

                response.sendRedirect(frontendUrl + githubNavigation + "?github=failed");

                return;
            }

            String accessToken = tokenJson.get("access_token").asText();

            String githubUserResponse = restClient.get().uri(githubUserUri).header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON).retrieve().body(String.class);

            JsonNode githubUser = objectMapper.readTree(githubUserResponse);

            UserEntity user = userRepository.findByEmail(loggedInEmail).orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST.value()));

            EnterpriseApplicationConnectionEntity connection = enterpriseApplicationConnectionRepo.findByUserAndApplication(user, EnumEnterpriseApplication.GITHUB).orElse(new EnterpriseApplicationConnectionEntity());


            connection = enterpriseApplicationConnectionMapper.mapGithubConnection(connection, user, githubUser, tokenJson, accessToken);

            enterpriseApplicationConnectionRepo.save(connection);

            response.sendRedirect(frontendUrl + githubNavigation + "?github=connected");

        } catch (Exception ex) {

            log.error("GitHub connection failed", ex);

            response.sendRedirect(frontendUrl + githubNavigation + "?github=failed");

        }
    }

    @Override
    public Map<String, Object> sync(String authorizationHeader) throws CommonException {

        String jwt = authorizationHeader.substring(7);

        String email = jwtUtil.validateToken(jwt).getSubject();

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST.value()));

        EnterpriseApplicationConnectionEntity connection = enterpriseApplicationConnectionRepo.findByUserAndApplication(user, EnumEnterpriseApplication.GITHUB).orElseThrow(() -> new CommonException("GitHub not connected", HttpStatus.BAD_REQUEST.value()));

        GithubSyncRequestDto request = new GithubSyncRequestDto();

        request.setUserId(user.getId().toString());
        request.setGithubToken(connection.getAccessToken());

        return restClient.post().uri("http://localhost:8001/github/sync").body(request).retrieve().body(Map.class);
    }

    @Override
    public GithubConnectionStatusResponseDto getGithubStatus(UserDataDto userDataDto) throws CommonException {
        UserEntity user = userRepository.findByEmail(userDataDto.getEmail()).orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST.value()));

        EnterpriseApplicationConnectionEntity connection = enterpriseApplicationConnectionRepo.findByUserAndApplication(user, EnumEnterpriseApplication.GITHUB).orElse(null);

        if (connection == null || connection.getAccessToken() == null || connection.getAccessToken().isBlank() || !Boolean.TRUE.equals(connection.getConnected())) {

            return new GithubConnectionStatusResponseDto(false, null, null, null);

        }

        return new GithubConnectionStatusResponseDto(true, connection.getUsername(), connection.getDisplayName(), connection.getAvatarUrl());
    }

    @Override
    public void disconnectGithub(UserDataDto userDataDto) throws CommonException {

        UserEntity user = userRepository.findByEmail(userDataDto.getEmail()).orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST.value()));

        EnterpriseApplicationConnectionEntity connection = enterpriseApplicationConnectionRepo.findByUserAndApplication(user, EnumEnterpriseApplication.GITHUB).orElseThrow(() -> new CommonException("GitHub is not connected", HttpStatus.BAD_REQUEST.value()));

        connection.setConnected(false);
        connection.setAccessToken(null);
        connection.setRefreshToken(null);
        connection.setAccountId(null);
        connection.setUsername(null);
        connection.setDisplayName(null);
        connection.setEmail(null);
        connection.setAvatarUrl(null);
        connection.setScope(null);
        connection.setTokenType(null);
        connection.setExpiresAt(null);
        connection.setLastSyncedAt(null);

        enterpriseApplicationConnectionRepo.save(connection);

    }

    @Override
    public List<GithubRepositoryResponseDto> getRepositories(UserDataDto userDataDto) throws CommonException {

        UserEntity user =
                userRepository
                        .findByEmail(userDataDto.getEmail())
                        .orElseThrow(() ->
                                new CommonException("User not found", HttpStatus.BAD_REQUEST.value())
                        );

        EnterpriseApplicationConnectionEntity connection =
                enterpriseApplicationConnectionRepo
                        .findByUserAndApplication(
                                user,
                                EnumEnterpriseApplication.GITHUB
                        )
                        .orElseThrow(() ->
                                new CommonException("GitHub not connected", HttpStatus.BAD_REQUEST.value())
                        );

        if (connection.getAccessToken() == null ||
                connection.getAccessToken().isBlank()) {

            throw new CommonException("GitHub access token not found", HttpStatus.BAD_REQUEST.value());

        }

        String repositoriesResponse =
                restClient
                        .get()
                        .uri(githubRepositoriesUri)
                        .header(
                                "Authorization",
                                "Bearer "
                                        + connection.getAccessToken()
                        )
                        .header(
                                "Accept",
                                "application/vnd.github+json"
                        )
                        .header(
                                "X-GitHub-Api-Version",
                                "2022-11-28"
                        )
                        .accept(
                                MediaType.APPLICATION_JSON
                        )
                        .retrieve()
                        .body(String.class);


        // ==========================================================
        // 5. Parse GitHub response
        // ==========================================================

        JsonNode repositories =
                objectMapper.readTree(
                        repositoriesResponse
                );


        List<GithubRepositoryResponseDto> repositoryList =
                new ArrayList<>();


        // ==========================================================
        // 6. Process every GitHub repository
        // ==========================================================

        for (JsonNode repositoryJson : repositories) {

            String githubRepositoryId =
                    repositoryJson
                            .path("id")
                            .asText();


            // ======================================================
            // 7. Find repository in our database
            // ======================================================

            Optional<RepositoryEntity> repositoryEntity =
                    repositoryRepo
                            .findByGithubRepositoryIdAndUser(
                                    githubRepositoryId,
                                    user
                            );


            boolean agentActive = false;


            // ======================================================
            // 8. Check whether Repository Agent exists
            // ======================================================

            if (repositoryEntity.isPresent()) {

                Optional<RepositoryAgentEntity> agent =
                        repositoryAgentRepo
                                .findByRepository(
                                        repositoryEntity.get()
                                );


                // ==================================================
                // 9. Check agent status
                // ==================================================

                agentActive =
                        agent.isPresent()
                                && agent.get().getStatus()
                                == EnumAgentStatus.ACTIVE;
            }


            // ======================================================
            // 10. Map GitHub repository to response DTO
            // ======================================================

            repositoryList.add(
                    githubRepositoryMapper.map(
                            repositoryJson,
                            agentActive
                    )
            );
        }


        // ==========================================================
        // 11. Return repositories
        // ==========================================================

        return repositoryList;
    }

    @Override
    public GithubRepositorySyncResponseDto syncRepository(UserDataDto userDataDto, String repositoryId) throws Exception {

        /*
         * ==========================================================
         * 1. FIND USER
         * ==========================================================
         */

        UserEntity user = userRepository.findByEmail(userDataDto.getEmail()).orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST.value()));


        /*
         * ==========================================================
         * 2. FIND GITHUB CONNECTION
         * ==========================================================
         */

        EnterpriseApplicationConnectionEntity connection =
                enterpriseApplicationConnectionRepo
                        .findByUserAndApplication(
                                user,
                                EnumEnterpriseApplication.GITHUB
                        )
                        .orElseThrow(() ->
                                new CommonException(
                                        "GitHub not connected",
                                        HttpStatus.BAD_REQUEST.value()
                                )
                        );

        log.info("========================================");
        log.info("GitHub connection found");
        log.info("User: {}", user.getEmail());
        log.info("GitHub connected: {}", connection.getConnected());
        log.info("GitHub username: {}", connection.getUsername());
        log.info(
                "GitHub token available: {}",
                connection.getAccessToken() != null &&
                        !connection.getAccessToken().isBlank()
        );
        log.info("========================================");


        /*
         * ==========================================================
         * 3. VALIDATE GITHUB ACCESS TOKEN
         * ==========================================================
         */

        if (connection.getAccessToken() == null || connection.getAccessToken().isBlank()) {

            throw new CommonException("GitHub access token not found", HttpStatus.BAD_REQUEST.value());
        }


        /*
         * ==========================================================
         * 4. GET REPOSITORY DETAILS FROM GITHUB
         * ==========================================================
         */

        String repositoryResponse = restClient.get().uri(githubRepositoryByIdUri + "/" + repositoryId).header("Authorization", "Bearer " + connection.getAccessToken()).header("Accept", "application/vnd.github+json").header("X-GitHub-Api-Version", "2022-11-28").retrieve().body(String.class);

        log.info(
                "Fetching GitHub repository details. Repository ID: {}",
                repositoryId
        );
        log.info(
                "GitHub Repository URI: {}",
                githubRepositoryUri
        );

        /*
         * ==========================================================
         * 5. CONVERT GITHUB RESPONSE TO JSON
         * ==========================================================
         */

        JsonNode repositoryJson = objectMapper.readTree(repositoryResponse);


        /*
         * ==========================================================
         * 6. CREATE / UPDATE REPOSITORY
         * ==========================================================
         */

        RepositoryEntity repository = repositoryRepo.findByGithubRepositoryIdAndUser(repositoryId, user).orElse(new RepositoryEntity());


        repository = githubRepositoryMapper.mapRepository(repository, user, repositoryJson, repositoryId);


        repository = repositoryRepo.save(repository);


        /*
         * ==========================================================
         * 7. CREATE / GET REPOSITORY AGENT
         * ==========================================================
         */

        RepositoryAgentEntity repositoryAgent = repositoryAgentRepo.findByRepository(repository).orElse(new RepositoryAgentEntity());


        repositoryAgent = githubRepositoryMapper.mapRepositoryAgent(repositoryAgent, repository);


        /*
         * ==========================================================
         * 8. ACTIVATE JAVA AGENT RECORD
         * ==========================================================
         */

        repositoryAgent.setStatus(EnumAgentStatus.ACTIVE);


        repositoryAgent = repositoryAgentRepo.save(repositoryAgent);


        /*
         * ==========================================================
         * 9. CALL PYTHON REPOSITORY AGENT
         * ==========================================================
         *
         * Python will:
         *
         * Repository
         *      ↓
         * GithubAgent
         *      ↓
         * Commits
         * Issues
         * Pull Requests
         * README
         *      ↓
         * Repository Context
         *      ↓
         * Central Agent
         *      ↓
         * Knowledge Processing
         *
         * ==========================================================
         */

        GithubAgentResponseDto agentResponse;

        try {

            agentResponse = githubAgentClient.syncRepositoryAgent(repository.getGithubRepositoryId(), repository.getRepositoryName(), repository.getOwner(), connection.getAccessToken());

            log.info("Python Agent Activated: {}", agentResponse.getAgentId());

            log.info("Python Agent Status: {}", agentResponse.getAgentStatus());

            log.info("Commits Collected: {}", agentResponse.getCommits());

            log.info("Issues Collected: {}", agentResponse.getIssues());

            log.info("Pull Requests Collected: {}", agentResponse.getPullRequests());

        } catch (Exception exception) {

            /*
             * Python agent failed.
             *
             * Do not keep the Java agent as ACTIVE
             * if the runtime agent could not execute.
             */

            repositoryAgent.setStatus(EnumAgentStatus.FAILED);

            repositoryAgentRepo.save(repositoryAgent);

            throw new CommonException("GitHub repository agent execution failed: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


        /*
         * ==========================================================
         * 10. UPDATE LAST SYNC TIME
         * ==========================================================
         */

        repositoryAgent.setLastSyncedAt(LocalDateTime.now());


        repositoryAgent = repositoryAgentRepo.save(repositoryAgent);


        /*
         * ==========================================================
         * 11. RETURN RESPONSE
         * ==========================================================
         */

        return new GithubRepositorySyncResponseDto(

                repository.getGithubRepositoryId(),

                repository.getRepositoryName(),

                repositoryAgent.getStatus().getCode(),

                "Repository synchronized successfully.");
    }
}
