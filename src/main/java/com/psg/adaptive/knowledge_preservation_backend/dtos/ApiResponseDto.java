package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponseDto {
    private int code;
    private String message;

    public ApiResponseDto(int code, String message) {
        this.code = code;a
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
