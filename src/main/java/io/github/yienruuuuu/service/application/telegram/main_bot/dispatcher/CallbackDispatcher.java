package io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher;

import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class CallbackDispatcher {
    private final Map<String, MainBotCommand> callbackMap = new HashMap<>();

    public CallbackDispatcher() {
    }

    public void dispatch(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        MainBotCommand command = callbackMap.get(callbackData);

        if (command != null) {
            command.execute(update);
        }
    }
}
