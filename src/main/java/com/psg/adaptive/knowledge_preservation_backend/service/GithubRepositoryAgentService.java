package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import org.springframework.stereotype.Component;

@Component
public interface GithubRepositoryAgentService {

    int collectActivities(RepositoryEntity repository, String accessToken) throws Exception;
}
