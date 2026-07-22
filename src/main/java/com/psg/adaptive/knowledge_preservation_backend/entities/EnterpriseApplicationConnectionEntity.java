package com.psg.adaptive.knowledge_preservation_backend.entities;

import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "enterprise_application_connection")
public class EnterpriseApplicationConnectionEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Logged in employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "application")
    @Enumerated(EnumType.STRING)
    private EnumEnterpriseApplication application;

    // GitHub User ID
    @Column(name = "account_id")
    private String accountId;

    // GitHub Login
    @Column(name = "username")
    private String username;

    // Display Name
    @Column(name = "display_name")
    private String displayName;

    // Email
    @Column(name = "email")
    private String email;

    // Profile Image
    @Column(name = "avatar_url")
    private String avatarUrl;

    // OAuth Token

    @Column(name = "access_token", length = 4000)
    private String accessToken;

    // OAuth Refresh Token

    @Column(name = "refresh_token", length = 4000)
    private String refreshToken;

    // OAuth Scope
    @Column(name = "scope")
    private String scope;

    // Token Type
    @Column(name = "token_type")
    private String tokenType;

    // Connected?
    @Column(name = "connected")
    private Boolean connected;

    // Connection Time
    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    // Last Sync
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public EnterpriseApplicationConnectionEntity() {

    }

    public EnterpriseApplicationConnectionEntity(UUID id, UserEntity user, EnumEnterpriseApplication application,
                                                 String accountId, String username, String displayName, String email,
                                                 String avatarUrl, String accessToken, String refreshToken,
                                                 String scope, String tokenType, Boolean connected,
                                                 LocalDateTime connectedAt, LocalDateTime lastSyncedAt,
                                                 LocalDateTime expiresAt) {
        this.id = id;
        this.user = user;
        this.application = application;
        this.accountId = accountId;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.connected = connected;
        this.connectedAt = connectedAt;
        this.lastSyncedAt = lastSyncedAt;
        this.expiresAt = expiresAt;
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

    public EnumEnterpriseApplication getApplication() {
        return application;
    }

    public void setApplication(EnumEnterpriseApplication application) {
        this.application = application;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }

    public LocalDateTime getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void setLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}