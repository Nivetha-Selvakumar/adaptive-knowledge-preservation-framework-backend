package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubSyncRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubTokenRequest;
import com.psg.adaptive.knowledge_preservation_backend.entities.EnterpriseApplicationConnectionEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.EnterpriseApplicationConnectionMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.EnterpriseApplicationConnectionRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.UserRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import com.psg.adaptive.knowledge_preservation_backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class GithubServiceImpl implements GithubService {
    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.redirect-uri}")
    private String redirectUri;

    @Value("${github.oauth.authorize-uri}")
    private String githubAuthorizeUri;

    @Value("${github.oauth.token-uri}")
    private String githubTokenUri;

    @Value("${github.user-uri}")
    private String githubUserUri;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    UserRepo userRepository;

    @Autowired
    EnterpriseApplicationConnectionRepo enterpriseRepository;

    @Autowired
    JwtUtils jwtUtil;

    @Autowired
    EnterpriseApplicationConnectionMapper enterpriseApplicationConnectionMapper;

    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    //    @Override
//    public void connect(HttpServletResponse response) throws IOException {
//
//        String url =
//                githubAuthorizeUri
//                        + "?client_id=" + clientId
//                        + "&redirect_uri=" + redirectUri
//                        + "&scope=repo read:user";
//
//        response.sendRedirect(url);
//    }
//
//    @Override
//    public void callback(String code, HttpServletResponse response) throws Exception {
//
//        try {
//
//            // Exchange authorization code for access token
//            GithubTokenRequest request = new GithubTokenRequest(
//                    clientId,
//                    clientSecret,
//                    code,
//                    redirectUri
//            );
//
//            String tokenResponse = restClient.post()
//                    .uri(githubTokenUri)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .body(request)
//                    .retrieve()
//                    .body(String.class);
//
//            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
//
//            if (!tokenJson.has("access_token")) {
//                response.sendRedirect("http://localhost:5173/github?connected=false");
//                return;
//            }
//
//            String accessToken = tokenJson.get("access_token").asText();
//
//            // Fetch GitHub user
//            JsonNode githubUser = restClient.get()
//                    .uri(githubUserUri)
//                    .header("Authorization", "Bearer " + accessToken)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .body(JsonNode.class);
//
//            // Logged-in application user
//            Authentication authentication =
//                    SecurityContextHolder.getContext().getAuthentication();
//
//            String loggedInEmail = authentication.getName();
//
//            UserEntity user = userRepository.findByEmail(loggedInEmail)
//                    .orElseThrow(() ->
//                            new RuntimeException("User not found"));
//
//            EnterpriseApplicationConnectionEntity connection =
//                    enterpriseRepository
//                            .findByUserAndApplication(
//                                    user,
//                                    EnumEnterpriseApplication.GITHUB
//                            )
//                            .orElse(new EnterpriseApplicationConnectionEntity());
//
//            connection = enterpriseApplicationConnectionMapper.mapGithubConnection(
//                    connection,
//                    user,
//                    githubUser,
//                    tokenJson,
//                    accessToken
//            );
//
//            enterpriseRepository.save(connection);
//
//            response.sendRedirect(frontendUrl + "/github?connected=true");
//
//        } catch (Exception ex) {
//
//            log.error("GitHub connection failed", ex);
//
//            response.sendRedirect(frontendUrl + "/github?connected=true");
//        }
//    }
    @Override
    public Map<String, String> connect(
            String authorizationHeader
    ) throws Exception {

        String token = authorizationHeader.substring(7);

        String loggedInEmail =
                jwtUtil.validateToken(token)
                        .getSubject();

        String state =
                jwtUtil.generateShortLivedToken(
                        loggedInEmail,
                        Duration.ofMinutes(10)
                );

        String url =
                githubAuthorizeUri
                        + "?client_id=" + clientId
                        + "&redirect_uri=" + redirectUri
                        + "&scope=repo read:user"
                        + "&state=" + URLEncoder.encode(
                        state,
                        StandardCharsets.UTF_8
                );

        return Map.of(
                "url",
                url
        );

    }

    @Override
    public void callback(
            String code,
            String state,
            HttpServletResponse response
    ) throws Exception {

        try {

            String loggedInEmail =
                    jwtUtil.validateAndExtractSubject(state);

            GithubTokenRequest request =
                    new GithubTokenRequest(
                            clientId,
                            clientSecret,
                            code,
                            redirectUri
                    );

            String tokenResponse =
                    restClient.post()
                            .uri(githubTokenUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .body(request)
                            .retrieve()
                            .body(String.class);

            JsonNode tokenJson =
                    objectMapper.readTree(tokenResponse);

            if (!tokenJson.has("access_token")) {

                response.sendRedirect(
                        frontendUrl + "/dashboard?github=failed"
                );

                return;
            }

            String accessToken =
                    tokenJson.get("access_token")
                            .asText();

            String githubUserResponse =
                    restClient.get()
                            .uri(githubUserUri)
                            .header(
                                    "Authorization",
                                    "Bearer " + accessToken
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .body(String.class);

            JsonNode githubUser =
                    objectMapper.readTree(githubUserResponse);

            UserEntity user =
                    userRepository
                            .findByEmail(loggedInEmail)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            EnterpriseApplicationConnectionEntity connection =
                    enterpriseRepository
                            .findByUserAndApplication(
                                    user,
                                    EnumEnterpriseApplication.GITHUB
                            )
                            .orElse(
                                    new EnterpriseApplicationConnectionEntity()
                            );


            connection =
                    enterpriseApplicationConnectionMapper
                            .mapGithubConnection(
                                    connection,
                                    user,
                                    githubUser,
                                    tokenJson,
                                    accessToken
                            );

            enterpriseRepository.save(connection);

            response.sendRedirect(
                    frontendUrl +
                            "/dashboard?github=connected"
            );

        } catch (Exception ex) {

            log.error(
                    "GitHub connection failed",
                    ex
            );

            response.sendRedirect(
                    frontendUrl +
                            "/dashboard?github=failed"
            );

        }
    }

    @Override
    public Map<String, Object> sync(String authorizationHeader) throws CommonException {

        String jwt = authorizationHeader.substring(7);

        String email = jwtUtil.validateToken(jwt).getSubject();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EnterpriseApplicationConnectionEntity connection =
                enterpriseRepository.findByUserAndApplication(
                        user,
                        EnumEnterpriseApplication.GITHUB
                ).orElseThrow(() -> new RuntimeException("GitHub not connected"));

        GithubSyncRequestDto request = new GithubSyncRequestDto();

        request.setUserId(user.getId().toString());
        request.setGithubToken(connection.getAccessToken());

        return restClient.post()
                .uri("http://localhost:8001/github/sync")
                .body(request)
                .retrieve()
                .body(Map.class);
    }

}
