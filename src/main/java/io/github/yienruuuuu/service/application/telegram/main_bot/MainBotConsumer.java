package io.github.yienruuuuu.service.application.telegram.main_bot;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CallbackDispatcher;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CommandDispatcher;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee Date: 2024/10/16
 */

@Component
@Slf4j
public class MainBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotRepository botRepository;

    //Dispatcher
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    @Autowired
    public MainBotConsumer(BotRepository botRepository, CommandDispatcher commandDispatcher, CallbackDispatcher callbackDispatcher) {
        this.botRepository = botRepository;
        this.commandDispatcher = commandDispatcher;
        this.callbackDispatcher = callbackDispatcher;
    }

    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("MAIN BOT CONSUMER收到Update訊息", update);
        Bot mainBotEntity = botRepository.findBotByType(BotType.MAIN);

        if (update.hasMessage() && update.getMessage().hasText()) {
            commandDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasCallbackQuery()) {
            callbackDispatcher.dispatch(update, mainBotEntity);
        } else {
            log.warn("MAIN BOT CONSUMER收到不支援的更新類型");
        }
    }
}

