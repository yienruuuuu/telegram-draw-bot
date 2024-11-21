package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.entity.UserDrawStatus;
import io.github.yienruuuuu.repository.UserDrawStatusRepository;
import io.github.yienruuuuu.service.business.UserDrawStatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/21
 */
@Service("userDrawStatusService")
public class UserDrawStatusServiceImpl implements UserDrawStatusService {
    private final UserDrawStatusRepository userDrawStatusRepository;

    public UserDrawStatusServiceImpl(UserDrawStatusRepository userDrawStatusRepository) {
        this.userDrawStatusRepository = userDrawStatusRepository;
    }

    @Override
    public Optional<UserDrawStatus> findByUserAndCardPool(User user, CardPool cardPool) {
        return userDrawStatusRepository.findByUserAndCardPool(user, cardPool);
    }

    @Override
    public UserDrawStatus save(UserDrawStatus userDrawStatus) {
        return userDrawStatusRepository.save(userDrawStatus);
    }
}
