package io.github.yienruuuuu.service.application.telegram.main_bot;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CallbackDispatcher;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CommandDispatcher;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
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
    private final UserService userService;
    private final LanguageService languageService;
    //Dispatcher
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    @Autowired
    public MainBotConsumer(BotRepository botRepository, UserService userService, LanguageService languageService, CommandDispatcher commandDispatcher, CallbackDispatcher callbackDispatcher) {
        this.botRepository = botRepository;
        this.userService = userService;
        this.languageService = languageService;
        this.commandDispatcher = commandDispatcher;
        this.callbackDispatcher = callbackDispatcher;
    }

    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("收到Update訊息", update);
        Bot mainBotEntity = botRepository.findBotByType(BotType.MAIN);
        User user = getUser(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            commandDispatcher.dispatch(update);
        } else if (update.hasCallbackQuery()) {
            callbackDispatcher.dispatch(update);
        } else {
            log.warn("收到不支援的更新類型");
        }
    }


//    private

    /**
     * 從Update獲取User，並進一步查詢User或註冊User
     */
    private User getUser(Update update) {
        org.telegram.telegrambots.meta.api.objects.User user;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        } else if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
        } else {
            throw new IllegalArgumentException("Update type not supported");
        }
        return userService.findByTelegramUserIdOrSaveNewUser(
                String.valueOf(user.getId()),
                update.getMessage().getFrom().getFirstName(),
                languageService.findLanguageByCodeOrDefault(update.getMessage().getFrom().getLanguageCode())
        );

    }

}

