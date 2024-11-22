package io.github.yienruuuuu.service.application.telegram.channel_manage;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.channel_manage.dispatcher.ChannelCommandDispatcher;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;

/**
 * @author Eric.Lee Date: 2024/10/16
 */

@Component
@Slf4j
public class ChannelManageBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotRepository botRepository;
    //Dispatcher
    private final ChannelCommandDispatcher channelCommandDispatcher;

    public ChannelManageBotConsumer(BotRepository botRepository, ChannelCommandDispatcher channelCommandDispatcher) {
        this.botRepository = botRepository;
        this.channelCommandDispatcher = channelCommandDispatcher;
    }


    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("CHANNEL BOT CONSUMER收到Update訊息", update);
        Bot botEntity = botRepository.findBotByType(BotType.CHANNEL);

        if (update.hasMessage() && update.getMessage().hasText()) {
            channelCommandDispatcher.dispatch(update, botEntity);
        } else {
            log.warn("CHANNEL BOT CONSUMER收到不支援的更新類型");
        }
    }
}

