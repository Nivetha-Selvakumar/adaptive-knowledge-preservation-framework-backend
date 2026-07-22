package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubTokenRequest;
import com.psg.adaptive.knowledge_preservation_backend.entities.EnterpriseApplicationConnectionEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import com.psg.adaptive.knowledge_preservation_backend.mapper.EnterpriseApplicationConnectionMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.EnterpriseApplicationConnectionRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.UserRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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
    EnterpriseApplicationConnectionMapper enterpriseApplicationConnectionMapper;

    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void connect(HttpServletResponse response) throws IOException {

        String url =
                githubAuthorizeUri
                        + "?client_id=" + clientId
                        + "&redirect_uri=" + redirectUri
                        + "&scope=repo read:user";

        response.sendRedirect(url);
    }

//    @Override
//    public String callback(String code) throws Exception {
//
//        GithubTokenRequest request = new GithubTokenRequest(
//                clientId,
//                clientSecret,
//                code,
//                redirectUri
//        );
//
//        String response = restClient.post()
//                .uri("https://github.com/login/oauth/access_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .body(request)
//                .retrieve()
//                .body(String.class);
//
//        JsonNode json = objectMapper.readTree(response);
//
//        return json.get("access_token").asText();
//    }

    @Override
    public void callback(String code, HttpServletResponse response) throws Exception {

        try {

            // Exchange authorization code for access token
            GithubTokenRequest request = new GithubTokenRequest(
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            );

            String tokenResponse = restClient.post()
                    .uri(githubTokenUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            JsonNode tokenJson = objectMapper.readTree(tokenResponse);

            if (!tokenJson.has("access_token")) {
                response.sendRedirect("http://localhost:5173/github?connected=false");
                return;
            }

            String accessToken = tokenJson.get("access_token").asText();

            // Fetch GitHub user
            JsonNode githubUser = restClient.get()
                    .uri(githubUserUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(JsonNode.class);

            // Logged-in application user
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String loggedInEmail = authentication.getName();

            UserEntity user = userRepository.findByEmail(loggedInEmail)
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            EnterpriseApplicationConnectionEntity connection =
                    enterpriseRepository
                            .findByUserAndApplication(
                                    user,
                                    EnumEnterpriseApplication.GITHUB
                            )
                            .orElse(new EnterpriseApplicationConnectionEntity());

            connection = enterpriseApplicationConnectionMapper.mapGithubConnection(
                    connection,
                    user,
                    githubUser,
                    tokenJson,
                    accessToken
            );

            enterpriseRepository.save(connection);

            response.sendRedirect(frontendUrl + "/github?connected=true");

        } catch (Exception ex) {

            log.error("GitHub connection failed", ex);

            response.sendRedirect(frontendUrl + "/github?connected=true");
        }
    }
}
