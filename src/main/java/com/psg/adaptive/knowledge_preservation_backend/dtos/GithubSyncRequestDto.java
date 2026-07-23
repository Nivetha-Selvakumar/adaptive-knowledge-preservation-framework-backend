package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class GithubSyncRequestDto {
    private String userId;
    private String githubToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGithubToken() {
        return githubToken;
    }

    public void setGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }
}
