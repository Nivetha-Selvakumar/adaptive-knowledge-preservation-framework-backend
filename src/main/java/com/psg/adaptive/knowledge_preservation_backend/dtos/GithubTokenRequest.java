package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubTokenRequest {

    private String client_id;

    private String client_secret;

    private String code;

    private String redirect_uri;

}