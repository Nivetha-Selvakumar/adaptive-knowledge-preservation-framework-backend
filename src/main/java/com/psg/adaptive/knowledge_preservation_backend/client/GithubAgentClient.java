package com.psg.adaptive.knowledge_preservation_backend.client;

import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubAgentRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.GithubAgentResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubAgentClient {

    private final RestClient restClient;

    private final String pythonAgentBaseUrl;

    public GithubAgentClient(
            RestClient restClient,
            @Value("${python.agent.base-url}") String pythonAgentBaseUrl
    ) {

        this.restClient = restClient;

        this.pythonAgentBaseUrl =
                pythonAgentBaseUrl;
    }

    public GithubAgentResponseDto activateAgent(
            GithubAgentRequestDto request
    ) throws CommonException {

        if (request == null) {

            throw new CommonException(
                    "GitHub agent request is null",
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        if (request.getRepositoryId() == null ||
                request.getRepositoryId().isBlank()) {

            throw new CommonException(
                    "Repository ID is required",
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        String url =
                pythonAgentBaseUrl
                        + "/github/repositories/"
                        + request.getRepositoryId()
                        + "/sync";

        System.out.println(
                "Calling Python GitHub Agent: "
                        + url
        );

        System.out.println(
                "Repository ID: "
                        + request.getRepositoryId()
        );

        System.out.println(
                "Repository Name: "
                        + request.getRepositoryName()
        );

        System.out.println(
                "Owner: "
                        + request.getOwner()
        );

        GithubAgentResponseDto response;

        try {

            response =
                    restClient
                            .post()
                            .uri(url)
                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )
                            .accept(
                                    MediaType.APPLICATION_JSON
                            )
                            .body(request)
                            .retrieve()
                            .body(
                                    GithubAgentResponseDto.class
                            );

        } catch (Exception exception) {

            throw new CommonException(
                    "Unable to communicate with "
                            + "GitHub Python Agent: "
                            + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }

        if (response == null) {

            throw new CommonException(
                    "GitHub Python Agent returned "
                            + "an empty response",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }

        System.out.println(
                "Python Agent Response:"
        );

        System.out.println(
                "Status: "
                        + response.getStatus()
        );

        System.out.println(
                "Agent Status: "
                        + response.getAgentStatus()
        );

        System.out.println(
                "Agent ID: "
                        + response.getAgentId()
        );

        if (response.getAgentId() == null ||
                response.getAgentId().isBlank()) {

            throw new CommonException(
                    "GitHub Python Agent did not "
                            + "return an agent ID",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }

        return response;
    }
}
