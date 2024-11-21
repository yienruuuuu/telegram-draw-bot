package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.entity.UserDrawStatus;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/21
 */
public interface UserDrawStatusService {
    Optional<UserDrawStatus> findByUserAndCardPool(User user, CardPool cardPool);

    UserDrawStatus save(UserDrawStatus userDrawStatus);
}
