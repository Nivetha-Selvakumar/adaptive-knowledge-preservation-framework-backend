package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryAgentEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
@Repository
public interface RepositoryAgentRepo extends JpaRepository<RepositoryAgentEntity, UUID> {

    Optional<RepositoryAgentEntity> findByRepository(RepositoryEntity repository);

}