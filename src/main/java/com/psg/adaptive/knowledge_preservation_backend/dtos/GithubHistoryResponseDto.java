package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GithubHistoryResponseDto {
    private String id;
    private UserDataDto user;
    private Long repositoryId;
    private String repositoryName;
    private String repositoryUrl;
    private String defaultBranch;
    private Integer commitCount;
    private Integer issueCount;
    private Integer pullRequestCount;
    private String knowledgeFile;
    private String syncStatus;
    private String syncMessage;

    private LocalDateTime syncedAt;

    public GithubHistoryResponseDto() {

    }

    public GithubHistoryResponseDto(String id, UserDataDto user, Long repositoryId, String repositoryName,
                                    String repositoryUrl, String defaultBranch, Integer commitCount, Integer issueCount,
                                    Integer pullRequestCount, String knowledgeFile, String syncStatus, String syncMessage,
                                    LocalDateTime syncedAt) {
        this.id = id;
        this.user = user;
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
        this.repositoryUrl = repositoryUrl;
        this.defaultBranch = defaultBranch;
        this.commitCount = commitCount;
        this.issueCount = issueCount;
        this.pullRequestCount = pullRequestCount;
        this.knowledgeFile = knowledgeFile;
        this.syncStatus = syncStatus;
        this.syncMessage = syncMessage;
        this.syncedAt = syncedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDataDto getUser() {
        return user;
    }

    public void setUser(UserDataDto user) {
        this.user = user;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Integer getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(Integer commitCount) {
        this.commitCount = commitCount;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Integer getPullRequestCount() {
        return pullRequestCount;
    }

    public void setPullRequestCount(Integer pullRequestCount) {
        this.pullRequestCount = pullRequestCount;
    }

    public String getKnowledgeFile() {
        return knowledgeFile;
    }

    public void setKnowledgeFile(String knowledgeFile) {
        this.knowledgeFile = knowledgeFile;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncMessage() {
        return syncMessage;
    }

    public void setSyncMessage(String syncMessage) {
        this.syncMessage = syncMessage;
    }

    public LocalDateTime getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(LocalDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }
}