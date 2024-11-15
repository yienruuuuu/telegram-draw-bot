package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.CardPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
public interface CardPoolService {
    CardPool save(CardPool cardPool);

    Page<CardPool> findAllByPage(Pageable pageable);
}
