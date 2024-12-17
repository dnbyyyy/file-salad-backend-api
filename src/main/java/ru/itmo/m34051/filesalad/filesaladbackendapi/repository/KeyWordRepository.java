package ru.itmo.m34051.filesalad.filesaladbackendapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.m34051.filesalad.filesaladbackendapi.model.KeyWord;

import java.util.List;

@Repository
public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {
    @Query(value = "SELECT key_word FROM key_word ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<String> getRandomKeywords(int limit);
}
