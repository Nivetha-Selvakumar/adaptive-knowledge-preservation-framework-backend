package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryActivityEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumGithubActivityType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class GithubActivityMapper {

    private final ObjectMapper objectMapper;

    public GithubActivityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RepositoryActivityEntity mapCommit(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);
        activity.setActivityType(EnumGithubActivityType.COMMIT);
        activity.setExternalId(getText(json, "sha"));
        JsonNode commit = json.path("commit");
        activity.setTitle(getText(commit, "message"));
        activity.setDescription(getText(commit, "message"));
        activity.setActor(getNestedText(json, "author", "login"));
        activity.setUrl(getText(json, "html_url"));
        String date = getNestedText(commit, "author", "date");
        activity.setActivityTime(parseDate(date));
        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    public RepositoryActivityEntity mapIssue(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);

        activity.setActivityType(EnumGithubActivityType.ISSUE);

        activity.setExternalId(getText(json, "id"));

        activity.setTitle(getText(json, "title"));

        activity.setDescription(getText(json, "body"));

        activity.setActor(getNestedText(json, "user", "login"));

        activity.setUrl(getText(json, "html_url"));

        activity.setActivityTime(parseDate(getText(json, "updated_at")));

        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    public RepositoryActivityEntity mapPullRequest(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);

        activity.setActivityType(EnumGithubActivityType.PULL_REQUEST);

        activity.setExternalId(getText(json, "id"));

        activity.setTitle(getText(json, "title"));

        activity.setDescription(getText(json, "body"));

        activity.setActor(getNestedText(json, "user", "login"));

        activity.setUrl(getText(json, "html_url"));

        activity.setActivityTime(parseDate(getText(json, "updated_at")));

        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    public RepositoryActivityEntity mapReadme(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);

        activity.setActivityType(EnumGithubActivityType.README);

        activity.setExternalId(getText(json, "sha"));

        activity.setTitle("Repository README");

        activity.setDescription(getText(json, "content"));

        activity.setUrl(getText(json, "html_url"));

        activity.setActivityTime(LocalDateTime.now());

        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    public RepositoryActivityEntity mapBranch(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);

        activity.setActivityType(EnumGithubActivityType.BRANCH);

        activity.setExternalId(getText(json, "name"));

        activity.setTitle(getText(json, "name"));

        JsonNode commit = json.path("commit");

        activity.setDescription(getText(commit, "sha"));

        activity.setActivityTime(LocalDateTime.now());

        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    public RepositoryActivityEntity mapRelease(
            RepositoryActivityEntity activity,
            RepositoryEntity repository,
            JsonNode json
    ) throws Exception {

        activity.setRepository(repository);

        activity.setActivityType(EnumGithubActivityType.RELEASE);

        activity.setExternalId(getText(json, "id"));

        activity.setTitle(getText(json, "name"));

        activity.setDescription(getText(json, "body"));

        activity.setActor(getNestedText(json, "author", "login"));

        activity.setUrl(getText(json, "html_url"));

        activity.setActivityTime(parseDate(getText(json, "published_at")));

        activity.setRawData(objectMapper.writeValueAsString(json));

        return activity;
    }

    private String getText(
            JsonNode node,
            String field
    ) {

        JsonNode value = node.path(field);

        if (value.isMissingNode() ||
                value.isNull()) {

            return null;
        }

        return value.asText();
    }

    private String getNestedText(
            JsonNode node,
            String parent,
            String child
    ) {

        JsonNode parentNode =
                node.path(parent);

        if (parentNode.isMissingNode() ||
                parentNode.isNull()) {

            return null;
        }

        return getText(
                parentNode,
                child
        );
    }

    private LocalDateTime parseDate(
            String value
    ) {

        if (value == null ||
                value.isBlank()) {

            return null;
        }

        try {

            return OffsetDateTime
                    .parse(value)
                    .toLocalDateTime();

        } catch (Exception ignored) {

            return null;
        }
    }
}