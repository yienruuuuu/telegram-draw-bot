package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.CardPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
public interface CardPoolService {
    CardPool save(CardPool cardPool);

    Page<CardPool> findAllByPage(Pageable pageable);

    Optional<CardPool> findById(Integer id);

    void deleteById(Integer id);
}
