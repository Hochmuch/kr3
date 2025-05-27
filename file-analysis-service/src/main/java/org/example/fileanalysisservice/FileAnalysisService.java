package org.example.fileanalysisservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FileAnalysisService {

    private final FileAnalysisRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public FileAnalysisService(FileAnalysisRepository repository) {
        this.repository = repository;
    }

    public FileAnalysis analyzeFile(String fileId) {
        return repository.findById(fileId).orElseGet(() -> {
            try {
                String fileContent = restTemplate.getForObject(
                        "http://file-storing-service:8080/file/download/" + fileId,
                        String.class
                );

                assert fileContent != null;
                int wordCount = fileContent.trim().split("\\s+").length;
                int characterCount = fileContent.length();

                FileAnalysis analysis = new FileAnalysis();
                analysis.setFileId(fileId);
                analysis.setWordCount(wordCount);
                analysis.setCharacterCount(characterCount);

                return repository.save(analysis);
            } catch (Exception e) {
                throw new RuntimeException("Failed to analyze file: " + e.getMessage());
            }
        });
    }
}
