package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class GithubRepositoryResponseDto {

    private String id;

    private String name;

    private String fullName;

    private String description;

    private String language;

    private Boolean isPrivate;

    private String defaultBranch;

    private String htmlUrl;

    private String owner;

    private String updatedAt;

    public GithubRepositoryResponseDto() {

    }

    public GithubRepositoryResponseDto(String id, String name, String fullName, String description, String language,
                                       Boolean isPrivate, String defaultBranch, String htmlUrl, String owner, String updatedAt) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.language = language;
        this.isPrivate = isPrivate;
        this.defaultBranch = defaultBranch;
        this.htmlUrl = htmlUrl;
        this.owner = owner;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
