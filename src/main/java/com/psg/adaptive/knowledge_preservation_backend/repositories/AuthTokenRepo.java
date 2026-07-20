package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.AuthTokenEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface AuthTokenRepo extends JpaRepository<AuthTokenEntity, UUID> {

    List<AuthTokenEntity> findAllByUserAndStatus(UserEntity user, EnumStatus enumStatus);


    Optional<AuthTokenEntity> findByAuthTokenAndStatus(String token, EnumStatus status);

}
