package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubRepositoryResponseDto;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class GithubRepositoryMapper {

    public GithubRepositoryResponseDto map(JsonNode repository) {

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

        return dto;

    }
}
