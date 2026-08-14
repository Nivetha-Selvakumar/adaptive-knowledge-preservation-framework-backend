package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.config.HeaderConfig;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubConnectionStatusResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubRepositoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    @Autowired
    GithubService githubService;

    @Autowired
    HeaderConfig headerConfig;

    @GetMapping("/connect")
    public Map<String, String> connect(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        return githubService.connect(authorizationHeader);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") String state,
                         HttpServletResponse response) throws Exception {

        githubService.callback(
                code,
                state,
                response
        );

    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncGithub(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {

        return ResponseEntity.ok(
                githubService.sync(authorizationHeader)
        );
    }

    @GetMapping("/status")
    public ResponseEntity<GithubConnectionStatusResponseDto> githubStatus(
            @RequestHeader("Authorization") String token) throws CommonException {

        UserDataDto userDataDto = headerConfig.getAuthorizationAdminHeader(token);
        GithubConnectionStatusResponseDto githubConnectionStatusResponseDto = githubService.getGithubStatus(userDataDto);

        return ResponseEntity.ok(githubConnectionStatusResponseDto);

    }

    @GetMapping("/repositories")
    public ResponseEntity<List<GithubRepositoryResponseDto>> getRepositories(
            @RequestHeader("Authorization") String token
    ) throws CommonException {

        UserDataDto userDataDto =
                headerConfig.getAuthorizationAdminHeader(token);

        List<GithubRepositoryResponseDto> repositoryResponseDtos = githubService.getRepositories(userDataDto);

        return ResponseEntity.ok(repositoryResponseDtos);
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<?> disconnectGithub(
            @RequestHeader("Authorization") String token
    ) throws CommonException {

        UserDataDto userDataDto =
                headerConfig.getAuthorizationAdminHeader(token);

        githubService.disconnectGithub(userDataDto);

        return ResponseEntity.ok(
                Map.of(
                        "message", "GitHub disconnected successfully."
                )
        );

    }


    @PostMapping("/repositories/sync/{repositoryId}")
    public ResponseEntity<?> syncRepository(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("repositoryId") String repositoryId
    ) throws Exception {

        UserDataDto userDataDto =
                headerConfig.getAuthorizationAdminHeader(authorizationHeader);
        return ResponseEntity.ok(
                githubService.syncRepository(
                        userDataDto,
                        repositoryId
                )
        );

    }
}