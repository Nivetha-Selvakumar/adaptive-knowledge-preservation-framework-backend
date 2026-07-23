package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.service.GithubService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    @Autowired
    GithubService githubService;


    //    @GetMapping("/connect")
//    public void connect(HttpServletResponse response) throws IOException {
//        githubService.connect(response);
//    }
//
//    @GetMapping("/callback")
//    public void callback(
//            @RequestParam("code") String code,
//            HttpServletResponse response
//    ) throws Exception {
//
//        githubService.callback(code, response);
//    }
    @GetMapping("/connect")
    public Map<String, String> connect(

            @RequestHeader("Authorization") String authorizationHeader

    ) throws Exception {

        return githubService.connect(authorizationHeader);

    }

    @GetMapping("/callback")
    public void callback(

            @RequestParam("code") String code,

            @RequestParam("state") String state,

            HttpServletResponse response

    ) throws Exception {

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
}