package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class DrawCommand implements MainBotCommand {
    @Override
    public void execute(Update update) {
        //TODO 傳送卡池及抽卡按鈕
    }

    @Override
    public String getCommandName() {
        return "/draw";
    }
}