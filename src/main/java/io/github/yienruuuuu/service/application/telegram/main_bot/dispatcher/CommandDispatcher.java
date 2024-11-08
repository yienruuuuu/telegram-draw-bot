package io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher;

import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class CommandDispatcher {
    private final Map<String, MainBotCommand> commandMap = new HashMap<>();

    @Autowired
    public CommandDispatcher(List<MainBotCommand> commands) {
        for (MainBotCommand command : commands) {
            commandMap.put(command.getCommandName(), command);
        }
    }

    public void dispatch(Update update) {
        String messageText = update.getMessage().getText();
        MainBotCommand command = commandMap.get(messageText.split(" ")[0]);

        if (command != null) {
            command.execute(update);
        }
    }
}
