package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubConnectionStatusResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubRepositoryResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubRepositorySyncResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface GithubService {
//    void connect(HttpServletResponse response) throws IOException;
//
//    void callback(String code, HttpServletResponse response) throws Exception;

    Map<String, String> connect(String authorizationHeader) throws Exception;

    void callback(
            String code,
            String state,
            HttpServletResponse response
    ) throws Exception;

    Map<String, Object> sync(String authorizationHeader) throws CommonException;

    GithubConnectionStatusResponseDto getGithubStatus(UserDataDto userDataDto) throws CommonException;

    void disconnectGithub(UserDataDto userDataDto) throws CommonException;

    List<GithubRepositoryResponseDto> getRepositories(UserDataDto userDataDto) throws CommonException;

    GithubRepositorySyncResponseDto syncRepository(UserDataDto userDataDto, String repositoryId) throws Exception;
}
