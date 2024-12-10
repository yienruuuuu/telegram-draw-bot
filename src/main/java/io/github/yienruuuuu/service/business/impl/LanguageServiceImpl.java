package io.github.yienruuuuu.service.business.impl;

import com.github.benmanes.caffeine.cache.Cache;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.repository.LanguageRepository;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Service("languageService")
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final Cache<String, Language> languageCache;

    public LanguageServiceImpl(LanguageRepository languageRepository, Cache<String, Language> languageCache) {
        this.languageRepository = languageRepository;
        this.languageCache = languageCache;
    }

    @Override
    public Language findById(Integer id) {
        return languageRepository.findById(id).orElseThrow(() -> new ApiException(SysCode.LANGUAGE_NOT_FOUND));
    }

    @Override
    public Language findLanguageByCodeOrDefault(String code) {
        return languageCache.get(code, key ->
                languageRepository.findByLanguageCode(key)
                        .orElseGet(() -> languageRepository.findByLanguageCode("en").orElse(null))
        );
    }

    @Override
    public List<Language> findAllLanguages() {
        return languageRepository.findAll();
    }
}
