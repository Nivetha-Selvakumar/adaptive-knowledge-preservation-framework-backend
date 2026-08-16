package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class GithubAgentRequestDto {

    private String repositoryId;
    private String repositoryName;
    private String owner;
    private String token;

    public GithubAgentRequestDto() {
    }

    public GithubAgentRequestDto(
            String repositoryId,
            String repositoryName,
            String owner,
            String token
    ) {
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
        this.owner = owner;
        this.token = token;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
