package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubAgentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class GithubAgentClientServiceImpl {

    private final RestClient restClient;

    @Value("${python.agent.url}")
    private String pythonAgentUrl;

    @Value("${python.agent.base-url}")
    private String pythonAgentBaseUrl;

    public GithubAgentClientServiceImpl(
            RestClient restClient
    ) {
        this.restClient = restClient;
    }

    // ==========================================================
    // SYNC / ACTIVATE GITHUB REPOSITORY AGENT
    // ==========================================================

    public GithubAgentResponseDto syncRepositoryAgent(
            String repositoryId,
            String repositoryName,
            String owner,
            String token
    ) {

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "GITHUB AGENT CLIENT"
        );

        System.out.println(
                "Repository ID   : "
                        + repositoryId
        );

        System.out.println(
                "Repository Name : "
                        + repositoryName
        );

        System.out.println(
                "Owner           : "
                        + owner
        );

        // ======================================================
        // WEBHOOK URL
        // ======================================================

        String webhookUrl =
                pythonAgentBaseUrl
                        + "/github/webhook";

        System.out.println(
                "Webhook URL     : "
                        + webhookUrl
        );

        // ======================================================
        // REQUEST BODY
        // ======================================================

        Map<String, Object> request =
                new HashMap<>();

        request.put(
                "repositoryId",
                repositoryId
        );

        request.put(
                "repositoryName",
                repositoryName
        );

        request.put(
                "owner",
                owner
        );

        request.put(
                "token",
                token
        );

        // IMPORTANT:
        // Send webhook URL to Python agent

        request.put(
                "webhookUrl",
                webhookUrl
        );

        // ======================================================
        // DEBUG REQUEST
        // ======================================================

        System.out.println(
                "Request repositoryId : "
                        + request.get("repositoryId")
        );

        System.out.println(
                "Request repositoryName : "
                        + request.get("repositoryName")
        );

        System.out.println(
                "Request owner : "
                        + request.get("owner")
        );

        System.out.println(
                "Request webhookUrl : "
                        + request.get("webhookUrl")
        );

        // DO NOT PRINT TOKEN

        // ======================================================
        // PYTHON AGENT URL
        // ======================================================

        String syncUrl =
                pythonAgentUrl
                        + "/github/repositories/"
                        + repositoryId
                        + "/sync";

        System.out.println(
                "Python Agent URL: "
                        + syncUrl
        );

        System.out.println(
                "=================================================="
        );

        // ======================================================
        // CALL PYTHON AGENT
        // ======================================================

        return restClient
                .post()
                .uri(syncUrl)
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(request)
                .retrieve()
                .body(
                        GithubAgentResponseDto.class
                );
    }
}