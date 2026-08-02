package com.psg.adaptive.knowledge_preservation_backend.entities;

import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumSyncStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "github_sync_history")
public class GithubSyncHistoryEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "repository_id")
    private Long repositoryId;

    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "commit_count")
    private Integer commitCount;

    @Column(name = "issue_count")
    private Integer issueCount;

    @Column(name = "pull_request_count")
    private Integer pullRequestCount;

    @Column(name = "knowledge_file")
    private String knowledgeFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    private EnumSyncStatus syncStatus;

    @Column(name = "sync_message", length = 1000)
    private String syncMessage;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    public GithubSyncHistoryEntity(){

    }

    public GithubSyncHistoryEntity(UUID id, UserEntity user, Long repositoryId, String repositoryName,
                                   String repositoryUrl, String defaultBranch, Integer commitCount,
                                   Integer issueCount, Integer pullRequestCount, String knowledgeFile, EnumSyncStatus syncStatus,
                                   String syncMessage, LocalDateTime syncedAt) {
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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

    public EnumSyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(EnumSyncStatus syncStatus) {
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
