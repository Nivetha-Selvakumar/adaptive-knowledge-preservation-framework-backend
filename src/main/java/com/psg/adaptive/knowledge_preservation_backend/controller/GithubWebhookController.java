package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;

import com.psg.adaptive.knowledge_preservation_backend.repositories.RepositoryRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubRepositoryAgentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/github/webhook")
public class GithubWebhookController {

    private final GithubRepositoryAgentService
            githubRepositoryAgentService;

    private final RepositoryRepo repositoryRepo;


    public GithubWebhookController(
            GithubRepositoryAgentService githubRepositoryAgentService,
            RepositoryRepo repositoryRepo
    ) {

        this.githubRepositoryAgentService =
                githubRepositoryAgentService;

        this.repositoryRepo =
                repositoryRepo;
    }


    @PostMapping("/{repositoryId}")
    public ResponseEntity<?> receiveWebhook(

            @PathVariable String repositoryId,

            @RequestHeader(
                    value = "X-GitHub-Event",
                    required = false
            )
            String eventType,

            @RequestBody String payload

    ) throws Exception {


        if (eventType == null ||
                eventType.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "status",
                                    "FAILED",
                                    "message",
                                    "GitHub event type missing"
                            )
                    );
        }


        RepositoryEntity repository =
                repositoryRepo
                        .findByGithubRepositoryId(
                                repositoryId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Repository not found: "
                                                + repositoryId
                                )
                        );


        int activities =
                githubRepositoryAgentService
                        .processWebhookEvent(
                                repository,
                                eventType,
                                payload
                        );


        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "status",
                "SUCCESS"
        );

        response.put(
                "repositoryId",
                repositoryId
        );

        response.put(
                "event",
                eventType
        );

        response.put(
                "activitiesCollected",
                activities
        );


        return ResponseEntity.ok(
                response
        );
    }
}