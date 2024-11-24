package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.business.BotService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric.Lee
 * Date: 2024/11/12
 */
@Service("botService")
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;
    private final ConcurrentHashMap<BotType, Bot> botCache = new ConcurrentHashMap<>();

    public BotServiceImpl(BotRepository botRepository) {
        this.botRepository = botRepository;
        initializeCache();
    }

    @Override
    public Bot findByBotType(BotType type) {
        return botCache.computeIfAbsent(type, botRepository::findBotByType);
    }

    /**
     * 初始化緩存
     */
    private void initializeCache() {
        botRepository.findAll().forEach(bot -> botCache.put(bot.getType(), bot));
    }
}
