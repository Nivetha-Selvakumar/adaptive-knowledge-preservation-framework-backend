package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubHistoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.GithubSyncHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class GithubSyncHistoryMapper {

    public GithubHistoryResponseDto mapHistory(
            GithubSyncHistoryEntity entity, UserDataDto userDataDto
    ) {

        GithubHistoryResponseDto dto = new GithubHistoryResponseDto();

        dto.setId(entity.getId().toString());

        dto.setUser(userDataDto);

        dto.setRepositoryId(entity.getRepositoryId());

        dto.setRepositoryName(entity.getRepositoryName());

        dto.setRepositoryUrl(entity.getRepositoryUrl());

        dto.setDefaultBranch(entity.getDefaultBranch());

        dto.setCommitCount(entity.getCommitCount());

        dto.setIssueCount(entity.getIssueCount());

        dto.setPullRequestCount(entity.getPullRequestCount());

        dto.setKnowledgeFile(entity.getKnowledgeFile());

        dto.setSyncStatus(entity.getSyncStatus().name());

        dto.setSyncMessage(entity.getSyncMessage());

        dto.setSyncedAt(entity.getSyncedAt());

        return dto;
    }
}
