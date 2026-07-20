package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class ForgotPasswordRequestDto {

    private String email;

    public ForgotPasswordRequestDto(String email) {
        this.email = email;
    }

    public ForgotPasswordRequestDto() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

