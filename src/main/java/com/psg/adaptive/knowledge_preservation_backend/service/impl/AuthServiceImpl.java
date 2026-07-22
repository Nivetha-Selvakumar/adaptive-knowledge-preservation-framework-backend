package com.psg.adaptive.knowledge_preservation_backend.service.impl;


import com.psg.adaptive.knowledge_preservation_backend.dtos.CreateUserRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.LoginRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.AuthTokenMapper;
import com.psg.adaptive.knowledge_preservation_backend.mapper.UserMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.AuthTokenRepo;
import com.psg.adaptive.knowledge_preservation_backend.repositories.UserRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.AuthService;
import com.psg.adaptive.knowledge_preservation_backend.service.EmailService;
import com.psg.adaptive.knowledge_preservation_backend.utils.JwtUtils;
import com.psg.adaptive.knowledge_preservation_backend.validations.businessValidations.BusinessValidation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    BusinessValidation businessValidation;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthTokenRepo authTokenRepo;

    @Autowired
    AuthTokenMapper authTokenMapper;

    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public AuthTokenEntity loggingUser(LoginRequestDto loginRequestDto) throws CommonException {

        UserEntity user = businessValidation.checkUserEmailExist(loginRequestDto.getEmail());

        businessValidation.checkPassword(user.getPassword(), loginRequestDto.getPassword());

        List<AuthTokenEntity> activeTokens =
                authTokenRepo.findAllByUserAndStatus(user, EnumStatus.ACTIVE);

        if (!activeTokens.isEmpty()) {
            activeTokens.forEach(token -> token.setStatus(EnumStatus.INACTIVE));
            authTokenRepo.saveAll(activeTokens);
        }

        String newToken = jwtUtils.generateToken(
                user.getEmail(),
                user.getFirstName() + user.getLastName()
        );

        AuthTokenEntity tokenEntity =
                authTokenMapper.maptoAuthTokenEntity(user, newToken);

        authTokenRepo.save(tokenEntity);

        return tokenEntity;
    }

    @Override
    public void logoutUser(String authHeader) throws CommonException {

        logger.info("Logout request received");

        if (authHeader == null || authHeader.isBlank()) {
            throw new CommonException(
                    "Authorization header missing",
                    HttpStatus.BAD_REQUEST.value());
        }

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        try {

            Claims claims = jwtUtils.validateToken(token);

            String email = claims.getSubject();

            UserEntity user =
                    businessValidation.checkUserEmailExist(email);

            List<AuthTokenEntity> activeTokens =
                    authTokenRepo.findAllByUserAndStatus(
                            user,
                            EnumStatus.ACTIVE
                    );

            if (activeTokens.isEmpty()) {

                throw new CommonException(
                        "No active session found",
                        HttpStatus.BAD_REQUEST.value());

            }

            activeTokens.forEach(t ->
                    t.setStatus(EnumStatus.INACTIVE));

            authTokenRepo.saveAll(activeTokens);

        } catch (ExpiredJwtException e) {

            throw new CommonException(
                    "Token expired",
                    HttpStatus.UNAUTHORIZED.value());

        } catch (JwtException e) {

            throw new CommonException(
                    "Invalid token",
                    HttpStatus.UNAUTHORIZED.value());

        }
    }

    @Transactional
    @Override
    public void processForgotPassword(String email) throws CommonException {

        UserEntity user = userRepo.findByEmailAndStatus(email,EnumStatus.ACTIVE)
                .orElseThrow(() ->
                        new CommonException(
                                "Email not found",
                                HttpStatus.BAD_REQUEST.value()));

        String resetToken = UUID.randomUUID().toString();

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(
                LocalDateTime.now().plusMinutes(15));

        userRepo.save(user);

        emailService.sendResetPasswordEmail(
                email,
                resetToken
        );
    }

    @Override
    public void resetPassword(String token,
                              String newPassword)
            throws CommonException {

        UserEntity user =
                userRepo.findByResetToken(token)
                        .orElseThrow(() ->
                                new CommonException(
                                        "Invalid token",
                                        HttpStatus.BAD_REQUEST.value()));

        if (user.getResetTokenExpiry()
                .isBefore(LocalDateTime.now())) {

            throw new CommonException(
                    "Token expired",
                    HttpStatus.BAD_REQUEST.value());
        }

        user.setPassword(
                passwordEncoder.encode(newPassword));

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepo.save(user);
    }

    @Override
    public UserDataDto createUser(CreateUserRequestDto dto) throws CommonException {

        businessValidation.checkEmailAlreadyExists(dto.getEmail());

        UserEntity user = userMapper.mapCreateUserRequestDtoToUserEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepo.save(user);

        return userMapper.mapUserEntityToUserDataDto(user);
    }

}
