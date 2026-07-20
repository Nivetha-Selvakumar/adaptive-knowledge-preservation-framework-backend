package com.psg.adaptive.knowledge_preservation_backend.exception;

public class IOException extends CommonException {
  public IOException(String message, int httpCode) {
    super(message,httpCode);
  }
}
