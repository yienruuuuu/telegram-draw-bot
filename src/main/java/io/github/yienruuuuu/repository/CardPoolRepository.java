package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.enums.CardPoolType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CardPoolRepository extends JpaRepository<CardPool, Integer> {
    Page<CardPool> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT c FROM CardPool c WHERE c.isOpen = true AND c.cardPoolType = :cardPoolType AND c.endAt > :endAt ORDER BY c.startAt ASC")
    List<CardPool> findOpenPoolsAfter(CardPoolType cardPoolType, Instant endAt);

    @Query("SELECT c FROM CardPool c WHERE c.id = :id AND c.isOpen = true AND c.endAt > :endAt")
    Optional<CardPool> findOpenPoolByIdAndEndTime(Integer id, Instant endAt);

    boolean existsByResource_Id(Integer resourceId);
}