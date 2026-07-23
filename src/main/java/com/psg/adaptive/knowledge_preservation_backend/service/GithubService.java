package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

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
}
