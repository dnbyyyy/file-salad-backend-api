package ru.itmo.m34051.filesalad.filesaladbackendapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.m34051.filesalad.filesaladbackendapi.service.FileService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileController {

    @Value("${file.storage.path}")
    private String fileStoragePath;

    private final FileService fileService;

    @PostMapping("/files/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> response = fileService.handleFileUpload(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "   d."));
        }
    }

    @GetMapping("/files/download")
    public ResponseEntity<Map<String, Object>> downloadFile(@RequestParam String firstWord, @RequestParam String secondWord, @RequestParam String thirdWord) {
        Map<String, Object> response = fileService.handleFileDownload(firstWord, secondWord, thirdWord);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "File not found."));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String param) throws IOException {

        InputStreamResource resource = new InputStreamResource(new FileInputStream(
                Paths.get("").toAbsolutePath() + "/" + fileStoragePath + "/" + param));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
