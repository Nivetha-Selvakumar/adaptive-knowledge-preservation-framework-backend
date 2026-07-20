package com.psg.adaptive.knowledge_preservation_backend.exception;

public class ApplicationError {

    private final String description;

    public ApplicationError(String description) {
        super();
        this.description = description;
    }

    public ValidationException reqValidationError(String data, Integer code) {
        String desc = String.format(description, data);
        return new ValidationException(desc,code);
    }

    public ValidationException reqValidationError(Integer code) {
        return new ValidationException(description,code);
    }

    public CommonException commonApplicationError(Integer code) {
        return new CommonException(description,code);
    }

    public CommonException commonApplicationError(String data, Integer code) {
        String desc = String.format(description, data);
        return new CommonException(desc,code);
    }
}
