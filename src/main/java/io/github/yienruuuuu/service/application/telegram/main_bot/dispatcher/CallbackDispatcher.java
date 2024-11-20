package io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class CallbackDispatcher {
    private final CommandDispatcher commandDispatcher;

    public CallbackDispatcher(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }


    public void dispatch(Update update, Bot mainBotEntity) {
        String callbackData = update.getCallbackQuery().getData();
        commandDispatcher.dispatchForCallback(update, callbackData, mainBotEntity);
    }
}
