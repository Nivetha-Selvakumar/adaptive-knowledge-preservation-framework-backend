package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class GithubAgentResponseDto {

    private String status;

    private String agentStatus;

    private String agentId;

    private String repositoryId;

    private String repository;

    private Integer commits;

    private Integer issues;

    private Integer pullRequests;

    private Boolean readmeCollected;

    private Object knowledge;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public Integer getCommits() {
        return commits;
    }

    public void setCommits(Integer commits) {
        this.commits = commits;
    }

    public Integer getIssues() {
        return issues;
    }

    public void setIssues(Integer issues) {
        this.issues = issues;
    }

    public Integer getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(Integer pullRequests) {
        this.pullRequests = pullRequests;
    }

    public Boolean getReadmeCollected() {
        return readmeCollected;
    }

    public void setReadmeCollected(Boolean readmeCollected) {
        this.readmeCollected = readmeCollected;
    }

    public Object getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Object knowledge) {
        this.knowledge = knowledge;
    }
}