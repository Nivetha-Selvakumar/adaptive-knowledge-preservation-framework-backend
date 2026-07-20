package com.psg.adaptive.knowledge_preservation_backend.exception;

public class ValidationException extends CommonException {

  public ValidationException(String message, int httpCode) {
    super(message, httpCode);
  }
}
