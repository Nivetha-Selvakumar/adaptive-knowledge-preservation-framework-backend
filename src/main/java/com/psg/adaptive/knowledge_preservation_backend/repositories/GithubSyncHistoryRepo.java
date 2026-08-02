package com.psg.adaptive.knowledge_preservation_backend.repositories;

import com.psg.adaptive.knowledge_preservation_backend.entities.GithubSyncHistoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GithubSyncHistoryRepo extends JpaRepository<GithubSyncHistoryEntity, UUID> {
    List<GithubSyncHistoryEntity> findByUserOrderBySyncedAtDesc(
            UserEntity user
    );

    List<GithubSyncHistoryEntity> findTop5ByUserOrderBySyncedAtDesc(
            UserEntity user
    );

    Optional<GithubSyncHistoryEntity> findByUserAndRepositoryId(
            UserEntity user,
            Long repositoryId
    );

    Long countByUser(
            UserEntity user
    );

}
