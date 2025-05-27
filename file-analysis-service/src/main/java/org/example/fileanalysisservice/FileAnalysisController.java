package org.example.fileanalysisservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
public class FileAnalysisController {

    private final FileAnalysisService analysisService;

    public FileAnalysisController(FileAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileAnalysis> analyze(@PathVariable String id) {
        try {
            FileAnalysis result = analysisService.analyzeFile(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
