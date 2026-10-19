package com.cloudstorage.cloudstrorage.controller;

import com.cloudstorage.cloudstrorage.dto.FileDownload;
import com.cloudstorage.cloudstrorage.repository.FileRepository;
import com.cloudstorage.cloudstrorage.repository.UserRepository;
import com.cloudstorage.cloudstrorage.service.FileStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;

@Controller
public class FileController {
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @PostMapping("/files/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            return "redirect:/files?error=empty";
        }
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getSize());
            try {
                Long userId = userRepository.findIdByUsername(principal.getName());
                String storageKey = fileStorageService.saveFiles(file);
                fileRepository.addFile(userId, file.getOriginalFilename(), storageKey, file.getSize());
            }
            catch (IOException e) {
                System.err.println("Ошибка сохранения файла: " + e.getMessage());
                return "redirect:/files?error=storage";
            }
            return "redirect:/files";
    }
    public FileController (FileStorageService fileStorageService, UserRepository userRepository, FileRepository fileRepository) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable long id,
            Principal principal
    ) throws IOException {

        Long userId = userRepository.findIdByUsername(principal.getName());

        FileDownload file = fileRepository
                .findDownloadByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        Path path = fileStorageService.getFilePath(file.storageKey());

        if (!Files.isRegularFile(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(path))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.originalName(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .body(resource);
    }

}
