package com.psg.adaptive.knowledge_preservation_backend.entities;

import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumAgentStatus;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumAgentType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "repository_agent")
public class RepositoryAgentEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private RepositoryEntity repository;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_type")
    private EnumAgentType agentType;

    @Column(name = "agent_name")
    private String agentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumAgentStatus status;

    @Column(name = "knowledge_count")
    private Integer knowledgeCount;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RepositoryAgentEntity() {
    }

    public RepositoryAgentEntity(UUID id, RepositoryEntity repository, EnumAgentType agentType, String agentName,
                                 EnumAgentStatus status, Integer knowledgeCount, LocalDateTime lastSyncedAt,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.repository = repository;
        this.agentType = agentType;
        this.agentName = agentName;
        this.status = status;
        this.knowledgeCount = knowledgeCount;
        this.lastSyncedAt = lastSyncedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public EnumAgentType getAgentType() {
        return agentType;
    }

    public void setAgentType(EnumAgentType agentType) {
        this.agentType = agentType;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public EnumAgentStatus getStatus() {
        return status;
    }

    public void setStatus(EnumAgentStatus status) {
        this.status = status;
    }

    public Integer getKnowledgeCount() {
        return knowledgeCount;
    }

    public void setKnowledgeCount(Integer knowledgeCount) {
        this.knowledgeCount = knowledgeCount;
    }

    public LocalDateTime getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void setLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
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