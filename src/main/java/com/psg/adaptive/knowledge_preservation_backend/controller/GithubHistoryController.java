package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.config.HeaderConfig;
import com.psg.adaptive.knowledge_preservation_backend.dtos.DashboardSummaryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubHistoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.ResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubSyncHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/")
public class GithubHistoryController {

    @Autowired
    GithubSyncHistoryService githubHistoryService;

    @Autowired
    HeaderConfig headerConfig;

    @GetMapping("/github/history")
    public ResponseEntity<ResponseDto> getGithubHistory(
            @RequestHeader("Authorization") String token)
            throws CommonException {

        log.info("Fetching GitHub history");

        UserDataDto userDataDto = headerConfig.getAuthorizationAdminHeader(token);

        List<GithubHistoryResponseDto> history = githubHistoryService.getRecentHistory(userDataDto);

        ResponseDto response = new ResponseDto("GitHub history fetched successfully", HttpStatus.OK.value(), history);

        return ResponseEntity.ok(response);
    }


}
