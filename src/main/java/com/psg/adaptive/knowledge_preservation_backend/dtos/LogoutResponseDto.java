package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class LogoutResponseDto {

    private String message;
    private int status;

    public LogoutResponseDto() {
    }

    public LogoutResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
