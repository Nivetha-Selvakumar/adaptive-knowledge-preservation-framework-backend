package com.psg.adaptive.knowledge_preservation_backend.exception;

public class CommonException extends Exception {
  private final String message;
  private final int httpCode;


  public CommonException(String message, int httpCode) {
    super(message);
    this.message = message;
    this.httpCode = httpCode;
  }


  @Override
  public String getMessage() {
    return message;
  }
}
