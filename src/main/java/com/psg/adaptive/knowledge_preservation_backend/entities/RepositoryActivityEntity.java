package com.psg.adaptive.knowledge_preservation_backend.entities;

import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumGithubActivityType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "repository_activity",
        indexes = {
                @Index(
                        name = "idx_repository_activity_repository",
                        columnList = "repository_id"
                ),
                @Index(
                        name = "idx_repository_activity_type",
                        columnList = "activity_type"
                )
        }
)
public class RepositoryActivityEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private RepositoryEntity repository;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 50)
    private EnumGithubActivityType activityType;

    /**
     * GitHub activity ID.
     * Stored as String because GitHub APIs can expose
     * different identifier formats.
     */
    @Column(name = "external_id", length = 255)
    private String externalId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "actor", length = 255)
    private String actor;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "activity_time")
    private LocalDateTime activityTime;

    /**
     * Original GitHub JSON.
     * This will be useful later for knowledge processing.
     */
    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public RepositoryActivityEntity() {
    }

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RepositoryEntity getRepository() {
        return repository;
    }

    public void setRepository(RepositoryEntity repository) {
        this.repository = repository;
    }

    public EnumGithubActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(EnumGithubActivityType activityType) {
        this.activityType = activityType;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}