package ru.itmo.m34051.filesalad.filesaladbackendapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.m34051.filesalad.filesaladbackendapi.service.FileService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> response = fileService.handleFileUpload(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "File upload failed."));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Map<String, Object>> downloadFile(@RequestParam String firstWord, @RequestParam String secondWord, @RequestParam String thirdWord) {
        Map<String, Object> response = fileService.handleFileDownload(firstWord, secondWord, thirdWord);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "File not found."));
        }
        return ResponseEntity.ok(response);
    }
}
