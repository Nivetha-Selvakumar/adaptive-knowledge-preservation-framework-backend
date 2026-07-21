package com.psg.adaptive.knowledge_preservation_backend.mapper;

import com.psg.adaptive.knowledge_preservation_backend.dtos.CreateUserRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.dtos.UserDataDto;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumRole;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumSex;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserDataDto mapUserEntityToUserDataDto(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        UserDataDto dto = new UserDataDto();

        dto.setId(entity.getId() != null ? entity.getId().toString() : null);
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setPassword(entity.getPassword());
        dto.setAddress(entity.getAddress());

        dto.setDob(entity.getDob() != null
                ? entity.getDob().toString()
                : null);

        dto.setSex(entity.getSex() != null
                ? entity.getSex().name()
                : null);

        dto.setRole(entity.getRole() != null
                ? entity.getRole().name()
                : null);

        dto.setStatus(entity.getStatus() != null
                ? entity.getStatus().name()
                : null);

        dto.setCreatedAt(entity.getCreatedAt() != null
                ? entity.getCreatedAt().toString()
                : null);

        dto.setCreatedBy(entity.getCreatedBy());

        dto.setUpdatedAt(entity.getUpdatedAt() != null
                ? entity.getUpdatedAt().toString()
                : null);

        dto.setUpdatedBy(entity.getUpdatedBy());

        dto.setResetToken(entity.getResetToken());

        dto.setResetTokenExpiry(entity.getResetTokenExpiry() != null
                ? entity.getResetTokenExpiry().toString()
                : null);

        return dto;
    }

    public UserEntity mapCreateUserRequestDtoToUserEntity(CreateUserRequestDto dto) {

        UserEntity user = new UserEntity();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());

        if (dto.getDob() != null && !dto.getDob().isBlank()) {
            user.setDob(LocalDate.parse(dto.getDob()));
        }

        if (dto.getSex() != null && !dto.getSex().isBlank()) {
            user.setSex(EnumSex.valueOf(dto.getSex().toUpperCase()));
        }

        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            user.setRole(EnumRole.valueOf(dto.getRole().toUpperCase()));
        }

        user.setStatus(EnumStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("SYSTEM");

        return user;
    }
}