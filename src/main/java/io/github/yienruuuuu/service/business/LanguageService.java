package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Language;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface LanguageService {
    Language findLanguageByCodeOrDefault(String code);
}
