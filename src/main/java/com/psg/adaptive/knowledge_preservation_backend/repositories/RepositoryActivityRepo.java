package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryActivityEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumGithubActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositoryActivityRepo
        extends JpaRepository<RepositoryActivityEntity, UUID> {

    Optional<RepositoryActivityEntity> findByRepositoryAndActivityTypeAndExternalId(RepositoryEntity repository,
                                                                                    EnumGithubActivityType activityType,
                                                                                    String externalId);

    long countByRepository(
            RepositoryEntity repository
    );

}