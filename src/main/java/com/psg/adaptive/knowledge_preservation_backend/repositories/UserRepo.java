package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity>  findByResetToken(String token);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndStatus(String email, EnumStatus enumStatus);
}
