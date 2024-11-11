package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface UserService {
    List<User> findAll();

    void save(User user);

    /**
     * 根據telegramId查詢用戶，如果不存在則保存用戶並返回
     */
    User findByTelegramUserIdOrSaveNewUser(String telegramId, String firstName, Language language, Integer initialFreePoint);

    User findByTelegramUserId(String telegramId);

    boolean existsByTelegramUserId(String telegramId);
}
