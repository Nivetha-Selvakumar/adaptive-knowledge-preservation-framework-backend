package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import org.springframework.stereotype.Component;

@Component
public interface DashboardService {

    DashboardSummaryResponseDto getDashboardSummary(UserDataDto userDataDto) throws CommonException;
}
