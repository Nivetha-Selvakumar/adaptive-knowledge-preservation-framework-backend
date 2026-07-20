package com.psg.adaptive.knowledge_preservation_backend.service;

import com.psg.adaptive.knowledge_preservation_backend.dtos.CreateUserRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.LoginRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {
    AuthTokenEntity loggingUser(LoginRequestDto loginRequestDto) throws CommonException;

    void logoutUser(String token) throws CommonException;

    void processForgotPassword(String email) throws CommonException;

    void resetPassword(String token, String newPassword) throws CommonException;

    UserDataDto createUser(CreateUserRequestDto createUserRequestDto) throws CommonException;
}
