package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.DashboardSummaryMapper;
import com.psg.adaptive.knowledge_preservation_backend.mapper.GithubSyncHistoryMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.EnterpriseApplicationConnectionRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.GithubSyncHistoryRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.DashboardService;
import com.psg.adaptive.knowledge_preservation_backend.validations.businessValidations.BusinessValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    GithubSyncHistoryRepo githubSyncHistoryRepo;

    @Autowired
    DashboardSummaryMapper dashboardSummaryMapper;

    @Autowired
    EnterpriseApplicationConnectionRepo enterpriseApplicationConnectionRepo;

    @Autowired
    BusinessValidation businessValidation;

    @Override
    public DashboardSummaryResponseDto getDashboardSummary(
            UserDataDto userDataDto) throws CommonException {

        log.info("Fetching dashboard summary");

        UserEntity user = businessValidation.getUser(userDataDto);

        long totalRepositories =
                githubSyncHistoryRepo.countByUser(user);

        long totalHistoryItems =
                githubSyncHistoryRepo.findByUserOrderBySyncedAtDesc(user).size();

        long connectedApps =
                enterpriseApplicationConnectionRepo
                        .countByUserAndConnectedTrue(user);

        long totalApps =
                enterpriseApplicationConnectionRepo
                        .countByUser(user);

        long pendingApps =
                totalApps - connectedApps;

        return dashboardSummaryMapper.mapDashboardSummary(
                connectedApps,
                totalHistoryItems,
                pendingApps,
                totalRepositories
        );
    }
}
