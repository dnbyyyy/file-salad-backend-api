package ru.itmo.m34051.filesalad.filesaladbackendapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ru.itmo.m34051.filesalad.filesaladbackendapi.model.SavedFile;

@Repository
public interface SavedFileRepository extends JpaRepository<SavedFile, String> {
    Optional<SavedFile> findByKey(String key);

    List<SavedFile> findByCreatedAtBefore(LocalDateTime threshold);
}