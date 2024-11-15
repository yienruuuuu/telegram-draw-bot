package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.CardPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPoolRepository extends JpaRepository<CardPool, Integer> {
    Page<CardPool> findAllByOrderByCreatedAtDesc(Pageable pageable);
}