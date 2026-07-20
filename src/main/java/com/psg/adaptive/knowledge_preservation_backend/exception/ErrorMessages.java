package com.psg.adaptive.knowledge_preservation_backend.exception;

public class ErrorMessages {

    private ErrorMessages(){
    }
    public static final String MSG_MANDATORY = "Mandatory field validation error : '{}' should not be empty";

    public static final String MSG_FIELD_SIZE = "Field size validation error : '{}'";

    public static final String MSG_FIELD_FORMAT = "Field format validation error : '{}'";

    public static final String MSG_VALIDATION = "Validation Error : '{}'";

    public static final String ERROR_DURING_CONVERSION = "Error during conversion: {}";
}
