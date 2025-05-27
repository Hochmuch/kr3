package org.example.filestoringservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {
    boolean existsByHash(String hash);
    Optional<FileMetadata> findByHash(String hash);
}
