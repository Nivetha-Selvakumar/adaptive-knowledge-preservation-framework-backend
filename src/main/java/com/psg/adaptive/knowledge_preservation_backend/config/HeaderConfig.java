package com.psg.adaptive.knowledge_preservation_backend.config;

import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.mapper.UserMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.AuthTokenRepo;
import com.psg.adaptive.knowledge_preservation_backend.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeaderConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(HeaderConfig.class);

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthTokenRepo authTokenRepo;

    public UserDataDto getAuthorizationAdminHeader(String authHeader) throws CommonException {
        logger.info("Check for Auth Header");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CommonException("Authorization header missing", HttpStatus.UNAUTHORIZED.value());
        }

        String token = authHeader.substring(7);

        logger.info("Check for Validate Token");
        jwtUtils.validateToken(token);

        logger.info("Check for Active Token");
        AuthTokenEntity authToken = authTokenRepo
                .findByAuthTokenAndStatus(token, EnumStatus.ACTIVE)
                .orElseThrow(() ->
                        new CommonException("Session expired. Please login again", HttpStatus.UNAUTHORIZED.value())
                );

        logger.info("Check for Active Faculty");
        if(!authToken.getUser().getStatus().equals(EnumStatus.ACTIVE)){
            throw new CommonException("You are not an Active Faculty",HttpStatus.UNAUTHORIZED.value());
        }

        logger.info("Map to Faculty Data from Entity");
        return userMapper.mapUserEntityToUserDataDto(authToken.getUser());
    }
}
