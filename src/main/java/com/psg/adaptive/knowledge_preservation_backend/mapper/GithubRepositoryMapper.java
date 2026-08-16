package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubRepositoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryAgentEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumAgentStatus;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumAgentType;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumRepositoryStatus;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Component
public class GithubRepositoryMapper {

    public GithubRepositoryResponseDto map(JsonNode repository, boolean isActive) {

        GithubRepositoryResponseDto dto =
                new GithubRepositoryResponseDto();

        dto.setId(
                repository.get("id").asText()
        );

        dto.setName(
                repository.get("name").asText()
        );

        dto.setFullName(
                repository.get("full_name").asText()
        );

        dto.setDescription(
                repository.get("description").isNull()
                        ? ""
                        : repository.get("description").asText()
        );

        dto.setLanguage(
                repository.get("language").isNull()
                        ? ""
                        : repository.get("language").asText()
        );

        dto.setIsPrivate(
                repository.get("private").asBoolean()
        );

        dto.setDefaultBranch(
                repository.get("default_branch").asText()
        );

        dto.setHtmlUrl(
                repository.get("html_url").asText()
        );

        dto.setOwner(
                repository
                        .get("owner")
                        .get("login")
                        .asText()
        );

        dto.setUpdatedAt(
                repository.get("updated_at").asText()
        );

        dto.setAgentActive(isActive);
        return dto;

    }

    public RepositoryEntity mapRepository(RepositoryEntity repository, UserEntity user, JsonNode repositoryJson,
                                          String repositoryId) {

        repository.setUser(user);
        repository.setGithubRepositoryId(repositoryId);
        repository.setRepositoryName(repositoryJson.get("name").asText());
        repository.setFullName(repositoryJson.get("full_name").asText());
        repository.setOwner(repositoryJson.get("owner").get("login").asText());
        repository.setDescription(repositoryJson.get("description").isNull() ? "" : repositoryJson.get("description").asText());
        repository.setLanguage(repositoryJson.get("language").isNull() ? "" : repositoryJson.get("language").asText());
        repository.setDefaultBranch(repositoryJson.get("default_branch").asText());
        repository.setHtmlUrl(repositoryJson.get("html_url").asText());
        repository.setPrivateRepository(repositoryJson.get("private").asBoolean());
        repository.setStatus(EnumRepositoryStatus.ACTIVE);
        if (repository.getCreatedAt() == null) {
            repository.setCreatedAt(LocalDateTime.now());
        }
        repository.setUpdatedAt(LocalDateTime.now());
        return repository;

    }

    public RepositoryAgentEntity mapRepositoryAgent(RepositoryAgentEntity repositoryAgent, RepositoryEntity repository) {

        repositoryAgent.setRepository(repository);
        repositoryAgent.setAgentName(repository.getRepositoryName() + "-Repository-Agent");
        repositoryAgent.setAgentType(EnumAgentType.REPOSITORY);
        repositoryAgent.setStatus(EnumAgentStatus.ACTIVE);
        if (repositoryAgent.getKnowledgeCount() == null) {
            repositoryAgent.setKnowledgeCount(0);
        }

        if (repositoryAgent.getCreatedAt() == null) {
            repositoryAgent.setCreatedAt(LocalDateTime.now());
        }
        repositoryAgent.setUpdatedAt(LocalDateTime.now());
        return repositoryAgent;

    }
}
