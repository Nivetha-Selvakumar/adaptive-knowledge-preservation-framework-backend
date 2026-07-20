package com.psg.adaptive.knowledge_preservation_backend.exception;

public class BadRequestException extends CommonException {

    public BadRequestException(String message, int httpCode) {
        super(message,httpCode);
    }
}
