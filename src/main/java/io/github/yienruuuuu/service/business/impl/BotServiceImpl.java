package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.business.BotService;
import org.springframework.stereotype.Service;

/**
 * @author Eric.Lee
 * Date: 2024/11/12
 */
@Service("botService")
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;

    public BotServiceImpl(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    @Override
    public Bot findByBotType(BotType type) {
        return botRepository.findBotByType(type);
    }
}
