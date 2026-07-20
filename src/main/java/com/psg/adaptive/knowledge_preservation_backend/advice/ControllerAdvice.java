package com.psg.adaptive.knowledge_preservation_backend.advice;

import com.psg.adaptive.knowledge_preservation_backend.dtos.ApiResponseDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.BadRequestException;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleApiResponseDtoValidationErrors(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(new ApiResponseDto(400, errorMessage));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseDto> handleMaxSize(
            MaxUploadSizeExceededException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiResponseDto(
                        400,
                        "File size exceeds the allowed limit (Max 10MB)"
                ));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponseDto> handleCommonException(
            CommonException ex) {

        return new ResponseEntity<>(
                new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDto> handleBadRequestException(
            BadRequestException ex) {

        return new ResponseEntity<>(
                new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(
            ValidationException ex) {

        return new ResponseEntity<>(
                new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponseDto> handleIOException(
            IOException ex) {

        return new ResponseEntity<>(
                new ApiResponseDto(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleGeneric(
            Exception ex) {

        return new ResponseEntity<>(
                new ApiResponseDto(500, "Internal Server Error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}