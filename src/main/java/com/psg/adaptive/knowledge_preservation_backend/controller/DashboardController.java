package com.psg.adaptive.knowledge_preservation_backend.controller;


import com.psg.adaptive.knowledge_preservation_backend.config.HeaderConfig;
import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.ResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/")
public class DashboardController {


    @Autowired
    DashboardService dashboardService;

    @Autowired
    HeaderConfig headerConfig;

    private static final Logger logger =
            LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/dashboard/summary")
    public ResponseEntity<ResponseDto> getDashboardSummary(
            @RequestHeader("Authorization") String token)
            throws CommonException {

        logger.info("Fetching dashboard summary");
        UserDataDto userDataDto = headerConfig.getAuthorizationAdminHeader(token);

        DashboardSummaryResponseDto summary =
                dashboardService.getDashboardSummary(userDataDto);

        ResponseDto response = new ResponseDto(
                "Dashboard summary fetched successfully",
                HttpStatus.OK.value(),
                summary
        );

        return ResponseEntity.ok(response);
    }

}
