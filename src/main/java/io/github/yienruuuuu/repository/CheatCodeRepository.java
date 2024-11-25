package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.CheatCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheatCodeRepository extends JpaRepository<CheatCode, Integer> {
    Optional<CheatCode> findByCode(String code);
}