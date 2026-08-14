package com.psg.adaptive.knowledge_preservation_backend.entities;

import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumRepositoryStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "repository")
public class RepositoryEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "github_repository_id", nullable = false)
    private String githubRepositoryId;

    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "owner")
    private String owner;

    @Column(length = 5000)
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "html_url", length = 1000)
    private String htmlUrl;

    @Column(name = "private_repository")
    private Boolean privateRepository;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumRepositoryStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RepositoryEntity() {
    }

    public RepositoryEntity(UUID id, UserEntity user, String githubRepositoryId, String repositoryName, String fullName,
                            String owner, String description, String language, String defaultBranch, String htmlUrl,
                            Boolean privateRepository, EnumRepositoryStatus status, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.githubRepositoryId = githubRepositoryId;
        this.repositoryName = repositoryName;
        this.fullName = fullName;
        this.owner = owner;
        this.description = description;
        this.language = language;
        this.defaultBranch = defaultBranch;
        this.htmlUrl = htmlUrl;
        this.privateRepository = privateRepository;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getGithubRepositoryId() {
        return githubRepositoryId;
    }

    public void setGithubRepositoryId(String githubRepositoryId) {
        this.githubRepositoryId = githubRepositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Boolean getPrivateRepository() {
        return privateRepository;
    }

    public void setPrivateRepository(Boolean privateRepository) {
        this.privateRepository = privateRepository;
    }

    public EnumRepositoryStatus getStatus() {
        return status;
    }

    public void setStatus(EnumRepositoryStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}