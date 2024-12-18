package ru.itmo.m34051.filesalad.filesaladbackendapi.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.m34051.filesalad.filesaladbackendapi.model.KeyWord;
import ru.itmo.m34051.filesalad.filesaladbackendapi.model.SavedFile;
import ru.itmo.m34051.filesalad.filesaladbackendapi.repository.KeyWordRepository;
import ru.itmo.m34051.filesalad.filesaladbackendapi.repository.SavedFileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.storage.path}")
    private String fileStoragePath;

    private final KeyWordRepository keyWordRepository;
    private final SavedFileRepository savedFileRepository;

    private final List<String> keyWords;

    @PostConstruct
    public void init() throws IOException {
        // Ensure file storage directory exists
        Path storagePath = Paths.get(System.getProperty("user.dir") + "/" + fileStoragePath);
        if (!Files.exists(storagePath)) {
            Files.createDirectory(storagePath);
        }

        keyWords.add("tomato");
        keyWords.add("cucumber");
        keyWords.add("apple");
        keyWords.add("banana");
        keyWords.add("orange");
        keyWords.add("grape");
        keyWords.add("watermelon");
        keyWords.add("peach");
        keyWords.add("cherry");
        keyWords.add("strawberry");
        keyWords.add("blueberry");
        keyWords.add("raspberry");
        keyWords.add("blackberry");
        keyWords.add("kiwi");
        keyWords.add("mango");
        keyWords.add("pineapple");
        keyWords.add("pear");
        keyWords.add("plum");
        keyWords.add("apricot");
        keyWords.add("fig");
        keyWords.add("papaya");
        keyWords.add("pomegranate");
        keyWords.add("lemon");
        keyWords.add("lime");
        keyWords.add("grapefruit");
        keyWords.add("melon");
        keyWords.add("nectarine");
        keyWords.add("date");
        keyWords.add("coconut");
        keyWords.add("avocado");
        keyWords.add("spinach");
        keyWords.add("lettuce");
        keyWords.add("broccoli");
        keyWords.add("cauliflower");
        keyWords.add("carrot");
        keyWords.add("beet");
        keyWords.add("potato");
        keyWords.add("onion");
        keyWords.add("garlic");
        keyWords.add("zucchini");
        keyWords.add("pumpkin");
        keyWords.add("radish");
        keyWords.add("peas");
        keyWords.add("bean");
        keyWords.add("corn");
        keyWords.add("eggplant");
        keyWords.add("bellpepper");
        keyWords.add("chili");
        keyWords.add("mushroom");
        keyWords.add("celery");
        keyWords.add("asparagus");

        keyWordRepository.saveAll(keyWords.stream().map(x -> KeyWord.builder().keyWord(x).build()).toList());


    }

    public Map<String, Object> handleFileUpload(MultipartFile file) throws IOException {
        // Save file to storage
        UUID uuid = UUID.randomUUID();
        Path filePath = Paths.get(System.getProperty("user.dir") + "/" + fileStoragePath,
                uuid + "_" + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        // Get unique keywords
        List<String> keyWords = keyWordRepository.getRandomKeywords(3);

        // Save metadata to database
        String key = String.join("_", keyWords);
        SavedFile savedFile = SavedFile.builder()
                .id(UUID.randomUUID())
                .key(String.join("_", keyWords))
                .filePath(filePath.toString())
                .fileName(file.getOriginalFilename())
                .createdAt(LocalDateTime.now())
                .build();
        savedFileRepository.save(savedFile);

        // Prepare response
        return Map.of(
                "key", key,
                "downloadLink", "/download?first_word=" + keyWords.get(0) + "&second_word=" + keyWords.get(1) + "&third_word=" + keyWords.get(2),
                "fileName", Objects.requireNonNull(file.getOriginalFilename())
        );
    }

    public Map<String, Object> handleFileDownload(String firstWord, String secondWord, String thirdWord) {
        String key = String.join("_", List.of(firstWord, secondWord, thirdWord));
        Optional<SavedFile> savedFileOpt = savedFileRepository.findByKey(key);
        if (savedFileOpt.isEmpty()) {
            return Collections.emptyMap();
        }
        SavedFile savedFile = savedFileOpt.get();
        return Map.of(
                "downloadLink", savedFile.getFilePath(),
                "fileName", savedFile.getFileName()
        );
    }

    @Scheduled(fixedRate = 600_000) // Every 10 minutes
    public void cleanUpOldFiles() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(3);
        List<SavedFile> oldFiles = savedFileRepository.findByCreatedAtBefore(threshold);
        oldFiles.forEach(file -> {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
                savedFileRepository.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}