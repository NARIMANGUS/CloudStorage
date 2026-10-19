package com.cloudstorage.cloudstrorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {
    public String saveFiles(MultipartFile multipartFile) throws IOException {
        Path directory = Path.of("storage");
        Files.createDirectories(directory);
        String filename = UUID.randomUUID().toString();
        Path destination = directory.resolve(filename);

        try (InputStream input = multipartFile.getInputStream()) {
            Files.copy(input, destination);
        }
        catch (IOException e) {
            return "Ошибка файловой системы";
        }

        return filename;
    }

    public Path getFilePath(String storageKey) {
        UUID.fromString(storageKey);
        return Path.of("storage").resolve(storageKey);
    }
}
