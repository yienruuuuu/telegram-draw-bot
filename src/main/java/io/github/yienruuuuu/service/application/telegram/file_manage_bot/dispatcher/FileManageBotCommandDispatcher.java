package io.github.yienruuuuu.service.application.telegram.file_manage_bot.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.FileManageBotCommand;
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
public class FileManageBotCommandDispatcher {
    private final Map<String, FileManageBotCommand> commandMap = new HashMap<>();

    @Autowired
    public FileManageBotCommandDispatcher(List<FileManageBotCommand> commands) {
        for (FileManageBotCommand command : commands) {
            commandMap.put(command.getCommandName(), command);
        }
    }

    public void dispatch(Update update, Bot fileManageBotEntity) {
        String messageText = update.getMessage().getText();
        FileManageBotCommand command = commandMap.get(messageText.split(" ")[0]);

        if (command != null) {
            command.execute(update, fileManageBotEntity);
        }
    }
}
