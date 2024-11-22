package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.RoleType;
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

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByTelegramUserIdOrSaveNewUser(String telegramId, String firstName, Language language,Integer initialFreePoint) {
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
