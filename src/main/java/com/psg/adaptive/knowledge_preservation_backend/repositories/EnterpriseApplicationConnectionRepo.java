package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.EnterpriseApplicationConnectionEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumEnterpriseApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnterpriseApplicationConnectionRepo extends
        JpaRepository<EnterpriseApplicationConnectionEntity, UUID> {

    Optional<EnterpriseApplicationConnectionEntity> findByUserAndApplication(
            UserEntity user,
            EnumEnterpriseApplication application
    );
}