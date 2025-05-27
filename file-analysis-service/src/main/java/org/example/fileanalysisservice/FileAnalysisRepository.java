package org.example.fileanalysisservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAnalysisRepository extends JpaRepository<FileAnalysis, String> {
}
