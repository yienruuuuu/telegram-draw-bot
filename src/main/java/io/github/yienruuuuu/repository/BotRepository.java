package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Integer> {
    Bot findBotById(Integer id);

    Bot findBotByType(BotType type);
}