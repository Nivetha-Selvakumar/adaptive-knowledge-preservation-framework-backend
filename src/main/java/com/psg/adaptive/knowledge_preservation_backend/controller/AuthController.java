package com.psg.adaptive.knowledge_preservation_backend.controller;

import com.psg.adaptive.knowledge_preservation_backend.dtos.*;
import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.service.AuthService;
import com.psg.adaptive.knowledge_preservation_backend.validations.basicValidations.CreateUserValidation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private CreateUserValidation createUserValidation;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) throws CommonException {

        //Validating logging in
        logger.info("Validating for Login user");
        AuthTokenEntity user = authService.loggingUser(loginRequestDto);

        ResponseDto response = new ResponseDto("Login Successfully", HttpStatus.OK.value(), user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logoutUser(@RequestHeader("Authorization") String token) throws CommonException {
        logger.info("Processing logout request...");

        authService.logoutUser(token);

        LogoutResponseDto response = new LogoutResponseDto(
                "Logout successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/forget-password")
    public ResponseEntity<ResponseDto> forgotPassword(
            @RequestBody ForgotPasswordRequestDto request
    ) throws CommonException {

        logger.info("Processing forgot password request for email: {}", request.getEmail());

        authService.processForgotPassword(request.getEmail());

        ResponseDto response = new ResponseDto(
                "Password reset link sent successfully",
                HttpStatus.OK.value(), null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody ResetPasswordDto dto)
            throws CommonException {

        authService.resetPassword(dto.getToken(), dto.getNewPassword());

        ResponseDto response = new ResponseDto(
                "Password reset successful",
                HttpStatus.OK.value(), null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseDto> createUser(
            @RequestBody CreateUserRequestDto createUserRequestDto)
            throws CommonException {

        logger.info("Basic validation For Creating user");
        createUserValidation.validate(createUserRequestDto);

        logger.info("Creating new user with email : {}",
                createUserRequestDto.getEmail());

        UserDataDto user = authService.createUser(createUserRequestDto);

        ResponseDto response = new ResponseDto(
                "User created successfully",
                HttpStatus.CREATED.value(),
                user
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
