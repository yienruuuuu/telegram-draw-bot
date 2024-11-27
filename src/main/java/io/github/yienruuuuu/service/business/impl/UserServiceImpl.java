package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.PointLog;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.bean.enums.RoleType;
import io.github.yienruuuuu.repository.PointLogRepository;
import io.github.yienruuuuu.repository.UserRepository;
import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PointLogRepository pointLogRepository;

    public UserServiceImpl(UserRepository userRepository, PointLogRepository pointLogRepository) {
        this.userRepository = userRepository;
        this.pointLogRepository = pointLogRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addPointAndSavePointLog(User user, int point, PointType pointType, String reason, String telegramPaymentChargeId, String providerPaymentChargeId) {
        int beforePoint;
        int afterPoint;
        PointLog pointLog = PointLog.builder().user(user).amount(point).build();
        if (pointType == PointType.FREE) {
            beforePoint = user.getFreePoints();
            afterPoint = beforePoint + point;
            user.setFreePoints(afterPoint);
            pointLog.setPointType(PointType.FREE);
            pointLog.setReason(reason);
        } else if (pointType == PointType.PAID) {
            beforePoint = user.getPurchasedPoints();
            afterPoint = beforePoint + point;
            user.setPurchasedPoints(afterPoint);
            pointLog.setPointType(PointType.PAID);
            pointLog.setReason(reason);
            pointLog.setTelegramPaymentChargeId(telegramPaymentChargeId);
            pointLog.setProviderPaymentChargeId(providerPaymentChargeId);
        } else {
            throw new IllegalArgumentException("Unsupported PointType: " + pointType);
        }
        // 設置 balanceBefore 和 balanceAfter
        pointLog.setBalanceBefore(beforePoint);
        pointLog.setBalanceAfter(afterPoint);
        pointLogRepository.save(pointLog);
        return save(user);
    }

    @Override
    public User findByTelegramUserIdOrSaveNewUser(String telegramId, String firstName, Language language, Integer initialFreePoint) {
        Optional<User> user = userRepository.findByTelegramUserId(telegramId);
        if (user.isPresent()) {
            return user.get();
        }
        User newUser = User.builder()
                .telegramUserId(telegramId)
                .role(RoleType.NORMAL)
                .firstName(firstName)
                .language(language)
                .isBlock(false)
                .freePoints(initialFreePoint)
                .purchasedPoints(0)
                .hasAddInChannel(false)
                .build();
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User findByTelegramUserId(String telegramId) {
        return userRepository.findByTelegramUserId(telegramId).orElse(null);
    }

    @Override
    public boolean existsByTelegramUserId(String telegramId) {
        return userRepository.existsByTelegramUserId(telegramId);
    }
}
