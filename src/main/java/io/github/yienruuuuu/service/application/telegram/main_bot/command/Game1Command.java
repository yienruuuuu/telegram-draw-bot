package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class Game1Command implements MainBotCommand {
    @Override
    public void execute(Update update) {
        //TODO 傳送遊戲1說明
    }

    @Override
    public String getCommandName() {
        return "/game1";
    }
}