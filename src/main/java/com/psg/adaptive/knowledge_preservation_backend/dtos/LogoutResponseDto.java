package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;

@Data
public class LogoutResponseDto {

    private String message;
    private int code;

    public LogoutResponseDto() {
    }

    public LogoutResponseDto(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
