package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface RepositoryRepo extends JpaRepository<RepositoryEntity, UUID> {
    Optional<RepositoryEntity> findByGithubRepositoryIdAndUser(String repositoryId, UserEntity user);

    Optional<RepositoryEntity> findByGithubRepositoryId(String repositoryId);
}
