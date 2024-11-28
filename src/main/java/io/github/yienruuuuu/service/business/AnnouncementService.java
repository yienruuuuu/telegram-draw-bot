package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.enums.AnnouncementType;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface AnnouncementService {
    Optional<String> findAnnounceContentByTypeAndLanguage(AnnouncementType type, Language language);
}
