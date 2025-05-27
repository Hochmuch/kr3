package org.example.filestoringservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
public class FileStoringController {

    private final FileStorageService storageService;

    public FileStoringController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String result = storageService.storeFile(file);
            return ResponseEntity.ok(result);
        } catch (IOException | NoSuchAlgorithmException e) {
            return ResponseEntity.internalServerError().body("Failed to store file: " + e.getMessage());
        }
    }

    @GetMapping("/file/download/{id}")
    public ResponseEntity<String> downloadFile(@PathVariable String id) throws IOException {
        Optional<FileMetadata> metadata = storageService.findById(id);
        if (metadata.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FileMetadata fileMetadata = metadata.get();

        String location = fileMetadata.getLocation();
        if (location == null || location.isBlank()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File location is missing.");
        }

        Path path = Paths.get(location);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        String content = Files.readString(path);
        return ResponseEntity.ok(content);
    }
}
