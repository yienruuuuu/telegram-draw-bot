package io.github.yienruuuuu.service.application.telegram.channel_manage.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令類適配收到的telegram命令並執行(execute())相對應的功能
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class ChannelCommandDispatcher {
    private final Map<String, MainBotCommand> commandMap = new HashMap<>();

    @Autowired
    public ChannelCommandDispatcher(List<MainBotCommand> commands) {
        for (MainBotCommand command : commands) {
            commandMap.put(command.getCommandName(), command);
        }
    }

    public void dispatch(Update update, Bot mainBotEntity) {
        String messageText = update.getMessage().getText();
        MainBotCommand command = commandMap.get(messageText.split(" ")[0]);

        if (command != null) {
            command.execute(update, mainBotEntity);
        }
    }

    public void dispatchForOtherUpdateType(Update update, String message, Bot mainBotEntity) {
        MainBotCommand command = commandMap.get(message.split(" ")[0]);
        if (command != null) {
            command.execute(update, mainBotEntity);
        }
    }
}
