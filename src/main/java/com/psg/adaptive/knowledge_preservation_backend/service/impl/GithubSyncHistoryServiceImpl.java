package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubHistoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.GithubSyncHistoryMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.EnterpriseApplicationConnectionRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.GithubSyncHistoryRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.UserRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubSyncHistoryService;
import com.psg.adaptive.knowledge_preservation_backend.validations.businessValidations.BusinessValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GithubSyncHistoryServiceImpl implements GithubSyncHistoryService {

    @Autowired
    GithubSyncHistoryRepo githubSyncHistoryRepo;

    @Autowired
    GithubSyncHistoryMapper githubSyncHistoryMapper;

    @Autowired
    EnterpriseApplicationConnectionRepo enterpriseApplicationConnectionRepo;

    @Autowired
    BusinessValidation businessValidation;


    @Override
    public List<GithubHistoryResponseDto> getRecentHistory(
            UserDataDto userDataDto) throws CommonException {

        log.info("Fetching recent GitHub history");

        UserEntity user = businessValidation.getUser(userDataDto);

        return githubSyncHistoryRepo
                .findTop5ByUserOrderBySyncedAtDesc(user)
                .stream()
                .map(entity -> githubSyncHistoryMapper.mapHistory(entity, userDataDto))
                .toList();
    }
}
