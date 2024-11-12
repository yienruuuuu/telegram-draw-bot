package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface BotService {
    Bot findByBotType(BotType type);
}
