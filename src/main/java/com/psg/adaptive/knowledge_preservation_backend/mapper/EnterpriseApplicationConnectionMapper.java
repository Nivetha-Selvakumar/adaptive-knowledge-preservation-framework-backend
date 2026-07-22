package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.psg.adaptive.knowledge_preservation_backend.entities.EnterpriseApplicationConnectionEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EnterpriseApplicationConnectionMapper {

    public EnterpriseApplicationConnectionEntity mapGithubConnection(
            EnterpriseApplicationConnectionEntity connection,
            UserEntity user,
            JsonNode githubUser,
            JsonNode tokenJson,
            String accessToken
    ) {

        if (connection == null) {
            connection = new EnterpriseApplicationConnectionEntity();
        }

        connection.setUser(user);
        connection.setApplication(EnumEnterpriseApplication.GITHUB);

        connection.setAccountId(
                githubUser.get("id").asText()
        );

        connection.setUsername(
                githubUser.get("login").asText()
        );

        connection.setDisplayName(
                githubUser.hasNonNull("name")
                        ? githubUser.get("name").asText()
                        : githubUser.get("login").asText()
        );

        connection.setEmail(
                githubUser.hasNonNull("email")
                        ? githubUser.get("email").asText()
                        : null
        );

        connection.setAvatarUrl(
                githubUser.hasNonNull("avatar_url")
                        ? githubUser.get("avatar_url").asText()
                        : null
        );

        connection.setAccessToken(accessToken);

        connection.setRefreshToken(
                tokenJson.has("refresh_token")
                        ? tokenJson.get("refresh_token").asText()
                        : null
        );

        connection.setScope(
                tokenJson.has("scope")
                        ? tokenJson.get("scope").asText()
                        : null
        );

        connection.setTokenType(
                tokenJson.has("token_type")
                        ? tokenJson.get("token_type").asText()
                        : "Bearer"
        );

        connection.setConnected(true);

        if (connection.getConnectedAt() == null) {
            connection.setConnectedAt(LocalDateTime.now());
        }

        connection.setLastSyncedAt(LocalDateTime.now());

        // GitHub OAuth Apps generally don't provide expiry
        connection.setExpiresAt(null);

        return connection;
    }
}