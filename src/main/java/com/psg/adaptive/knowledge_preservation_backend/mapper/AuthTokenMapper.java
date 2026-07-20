package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthTokenMapper {

    public AuthTokenEntity maptoAuthTokenEntity(UserEntity user, String newToken) {
        AuthTokenEntity tokenEntity = new AuthTokenEntity();
        tokenEntity.setUser(user);
        tokenEntity.setAuthToken(newToken);
        tokenEntity.setStatus(EnumStatus.ACTIVE);
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setUpdatedAt(LocalDateTime.now());
        return tokenEntity;
    }
}
