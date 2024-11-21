package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.entity.UserDrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDrawStatusRepository extends JpaRepository<UserDrawStatus, Integer> {
    Optional<UserDrawStatus> findByUserAndCardPool(User user, CardPool cardPool);

}