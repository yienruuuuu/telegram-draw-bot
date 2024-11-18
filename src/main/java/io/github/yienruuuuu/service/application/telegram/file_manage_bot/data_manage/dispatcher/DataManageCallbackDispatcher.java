package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.dispatcher;

import io.github.yienruuuuu.bean.entity.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class DataManageCallbackDispatcher {
    private final DataManageCommandDispatcher dataManageCommandDispatcher;

    public DataManageCallbackDispatcher(DataManageCommandDispatcher dataManageCommandDispatcher) {
        this.dataManageCommandDispatcher = dataManageCommandDispatcher;
    }


    public void dispatch(Update update, Bot fileManageBotEntity) {
        String callbackData = update.getCallbackQuery().getData();
        dataManageCommandDispatcher.dispatchForCallback(update, callbackData, fileManageBotEntity);
    }
}
