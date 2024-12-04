package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.PointType;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface UserService {
    List<User> findAll();

    User save(User user);

    User addPointAndSavePointLog(User user, int point, PointType pointType, String reason, String telegramPaymentChargeId, String providerPaymentChargeId);

    /**
     * 根據telegramId查詢用戶，如果不存在則保存用戶並返回
     */
    User findByTelegramUserIdOrSaveNewUser(String telegramId, String firstName, Integer initialFreePoint, String originalLanguageCode);

    User findByTelegramUserId(String telegramId);

    boolean existsByTelegramUserId(String telegramId);
}
