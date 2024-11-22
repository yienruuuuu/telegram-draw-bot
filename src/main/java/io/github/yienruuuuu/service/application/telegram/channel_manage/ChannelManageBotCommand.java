package io.github.yienruuuuu.service.application.telegram.channel_manage;

import io.github.yienruuuuu.bean.entity.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface ChannelManageBotCommand {
    void execute(Update update, Bot botEntity);

    String getCommandName();
}
