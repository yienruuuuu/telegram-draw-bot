package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Page<Card> findAllByOrderByIdDesc(Pageable pageable);

    Page<Card> findAllByCardPoolOrderByIdDesc(CardPool cardPool, Pageable pageable);

    void deleteAllByCardPool_Id(Integer cardPoolId);

    boolean existsByResource_Id(Integer resourceId);
}