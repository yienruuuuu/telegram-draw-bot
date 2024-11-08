package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Optional<Language> findByLanguageCode(String languageCode);
}