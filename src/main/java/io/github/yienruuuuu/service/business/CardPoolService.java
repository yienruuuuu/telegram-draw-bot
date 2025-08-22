package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.enums.CardPoolType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
public interface CardPoolService {
    List<CardPool> findOpenCardPoolsByPoolType(CardPoolType cardPoolType);

    CardPool save(CardPool cardPool);

    Page<CardPool> findAllByPage(Pageable pageable);

    Optional<CardPool> findById(Integer id);

    Optional<CardPool> findByIdIsOpen(Integer id);

    void delete(CardPool cardPool);

    void deleteById(Integer id);

    boolean existsByResourceId(Integer id);
}
