package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Language;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface LanguageService {
    Language findById(Integer id);

    Language findLanguageByCodeOrDefault(String code);

    List<Language> findAllLanguages();
}
