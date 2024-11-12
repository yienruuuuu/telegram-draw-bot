package io.github.yienruuuuu.service.application.telegram.file_manage_bot;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.dispatcher.FileManageBotCallbackDispatcher;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.dispatcher.FileManageBotCommandDispatcher;
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
public class FileManageBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotRepository botRepository;

    //Dispatcher
    private final FileManageBotCommandDispatcher fileManageBotCommandDispatcher;
    private final FileManageBotCallbackDispatcher fileManageBotCallbackDispatcher;

    @Autowired
    public FileManageBotConsumer(BotRepository botRepository, FileManageBotCommandDispatcher fileManageBotCommandDispatcher, FileManageBotCallbackDispatcher fileManageBotCallbackDispatcher) {
        this.botRepository = botRepository;
        this.fileManageBotCommandDispatcher = fileManageBotCommandDispatcher;
        this.fileManageBotCallbackDispatcher = fileManageBotCallbackDispatcher;
    }

    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("FILE MANAGE BOT CONSUMER收到Update訊息", update);
        Bot mainBotEntity = botRepository.findBotByType(BotType.FILE_MANAGE);

        if (update.hasMessage() && update.getMessage().hasText()) {
            fileManageBotCommandDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasCallbackQuery()) {
            fileManageBotCallbackDispatcher.dispatch(update, mainBotEntity);
        } else {
            log.warn("MAIN BOT CONSUMER收到不支援的更新類型");
        }
    }
}

