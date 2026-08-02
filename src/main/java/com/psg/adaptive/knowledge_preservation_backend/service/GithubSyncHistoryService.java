package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubHistoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GithubSyncHistoryService {
    List<GithubHistoryResponseDto> getRecentHistory(
            UserDataDto userDataDto
    ) throws CommonException;

}
