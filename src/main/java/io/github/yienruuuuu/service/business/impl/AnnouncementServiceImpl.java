package io.github.yienruuuuu.service.business.impl;

import com.github.benmanes.caffeine.cache.Cache;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.Text;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.repository.AnnouncementRepository;
import io.github.yienruuuuu.service.business.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {
    private final Cache<String, String> announceContentCache;
    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(Cache<String, String> announceContentCache, AnnouncementRepository announcementRepository) {
        this.announceContentCache = announceContentCache;
        this.announcementRepository = announcementRepository;
    }

    public Optional<String> findAnnounceContentByTypeAndLanguage(AnnouncementType type, Language language) {
        String cacheKey = type.name() + "_" + language.getLanguageCode();
        return Optional.ofNullable(announceContentCache.get(cacheKey, key -> fetchFromDatabase(type, language)));
    }

    private String fetchFromDatabase(AnnouncementType type, Language language) {
        return announcementRepository.findAnnouncementByType(type)
                .getTexts()
                .stream()
                .filter(text -> text.getLanguage().equals(language))
                .findFirst()
                .map(Text::getContent)
                .orElseGet(() -> {
                    log.warn("缺少 {} 類型的公告內容 (語言: {})", type.name(), language.getLanguageCode());
                    return null;
                });
    }
}
