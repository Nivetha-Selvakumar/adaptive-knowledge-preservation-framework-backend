package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DashboardSummaryMapper {

    public DashboardSummaryResponseDto mapDashboardSummary(
            Long connectedApps,
            Long totalHistoryItems,
            Long pendingApps,
            Long totalRepositories
    ) {

        DashboardSummaryResponseDto dto =
                new DashboardSummaryResponseDto();

        dto.setConnectedApps(String.valueOf(connectedApps));

        dto.setTotalHistoryItems(String.valueOf(totalHistoryItems));

        dto.setPendingApps(String.valueOf(pendingApps));

        dto.setTotalRepositories(String.valueOf(totalRepositories));

        return dto;
    }
}
