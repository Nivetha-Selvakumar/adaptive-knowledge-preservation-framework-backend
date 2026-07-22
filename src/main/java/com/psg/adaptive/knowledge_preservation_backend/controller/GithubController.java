package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    @Autowired
    GithubService githubService;


    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws IOException {
        githubService.connect(response);
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws Exception {

        githubService.callback(code, response);
    }
}