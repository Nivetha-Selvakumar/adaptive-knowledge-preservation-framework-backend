package com.psg.adaptive.knowledge_preservation_backend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface GithubService {
    void connect(HttpServletResponse response) throws IOException;

    void callback(String code, HttpServletResponse response) throws Exception;

}
