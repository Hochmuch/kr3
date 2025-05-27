package org.example.filestoringservice;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
public class FileStorageService {

    private final FileMetadataRepository repository;

    public FileStorageService(FileMetadataRepository repository) {
        this.repository = repository;
    }

    public String storeFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hash = computeHash(file.getInputStream());


        if (repository.existsByHash(hash)) {
            return "File already exists. ID - " + repository.findByHash(hash).map(FileMetadata::getId).orElse(null);
        }

        Path dir = Paths.get("storage");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        Path path = dir.resolve(hash);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        FileMetadata meta = new FileMetadata();
        meta.setHash(hash);
        meta.setName(file.getOriginalFilename());
        meta.setLocation(path.toString());


        repository.save(meta);

        return "File uploaded successfully. ID - " + repository.findByHash(hash).map(FileMetadata::getId).orElse(null);
    }

    private String computeHash(InputStream is) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = is.readAllBytes();
        byte[] hash = digest.digest(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    public Optional<FileMetadata> findById(String id) {
        return repository.findById(id);
    }
}
