package io.github.yienruuuuu.service.application.telegram.main_bot;

import io.github.yienruuuuu.bean.entity.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface MainBotCommand {
    void execute(Update update, Bot mainBotEntity);

    String getCommandName();
}
