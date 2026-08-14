package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class GithubRepositorySyncResponseDto {

    private String repositoryId;

    private String repositoryName;

    private String syncStatus;

    private String message;

    public GithubRepositorySyncResponseDto() {

    }

    public GithubRepositorySyncResponseDto(String repositoryId, String repositoryName, String syncStatus, String message) {
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
        this.syncStatus = syncStatus;
        this.message = message;
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

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
