package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class DataManageCallbackDispatcher {
    private final Map<String, DataManageCommand> callbackMap = new HashMap<>();

    public DataManageCallbackDispatcher() {
    }

    public void dispatch(Update update, Bot fileManageBotEntity) {
        String callbackData = update.getCallbackQuery().getData();
        DataManageCommand command = callbackMap.get(callbackData);

        if (command != null) {
            command.execute(update, fileManageBotEntity);
        }
    }
}
