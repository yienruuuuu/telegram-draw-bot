package io.github.yienruuuuu.service.application.telegram.channel_manage;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.service.application.telegram.channel_manage.dispatcher.ChannelCommandDispatcher;
import io.github.yienruuuuu.service.business.BotService;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee Date: 2024/10/16
 */

@Component
@Slf4j
public class ChannelManageBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotService botService;
    //Dispatcher
    private final ChannelCommandDispatcher channelCommandDispatcher;
    //handler
    private final NewMemberHandler newMemberHandler;

    public ChannelManageBotConsumer(BotService botService, ChannelCommandDispatcher channelCommandDispatcher, NewMemberHandler newMemberHandler) {
        this.botService = botService;
        this.channelCommandDispatcher = channelCommandDispatcher;
        this.newMemberHandler = newMemberHandler;
    }


    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("CHANNEL BOT CONSUMER收到Update訊息", update);
        Bot botEntity = botService.findByBotType(BotType.CHANNEL);

        if (update.hasMessage()) {
            channelCommandDispatcher.dispatch(update, botEntity);
        } else if (update.hasChatMember()) {
            newMemberHandler.handleNewMember(update);
        } else {
            log.warn("CHANNEL BOT CONSUMER收到不支援的更新類型");
        }
    }
}

