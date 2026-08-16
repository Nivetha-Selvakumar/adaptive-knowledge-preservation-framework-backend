package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import org.springframework.stereotype.Component;

@Component
public interface GithubRepositoryAgentService {

    int collectActivities(RepositoryEntity repository, String accessToken) throws Exception;

    /*
     * Continuous monitoring.
     *
     * Called when Python GitHub Agent
     * detects a GitHub webhook event.
     */
    int processWebhookEvent(
            RepositoryEntity repository,
            String eventType,
            String payload
    ) throws Exception;
}
