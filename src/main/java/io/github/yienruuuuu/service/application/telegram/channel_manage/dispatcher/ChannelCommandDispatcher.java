package io.github.yienruuuuu.service.application.telegram.channel_manage.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.channel_manage.ChannelManageBotCommand;
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
    private final Map<String, ChannelManageBotCommand> commandMap = new HashMap<>();

    @Autowired
    public ChannelCommandDispatcher(List<ChannelManageBotCommand> commands) {
        for (ChannelManageBotCommand command : commands) {
            commandMap.put(command.getCommandName(), command);
        }
    }

    public void dispatch(Update update, Bot botEntity) {
        String messageText = update.getMessage().getText();
        ChannelManageBotCommand command = commandMap.get(messageText.split(" ")[0]);

        if (command != null) {
            command.execute(update, botEntity);
        }
    }
}
