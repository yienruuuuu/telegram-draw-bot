package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class UserCollectionCommand implements MainBotCommand {
    @Override
    public void execute(Update update, Bot mainBotEntity) {
        //TODO 傳送玩家持有卡牌
    }

    @Override
    public String getCommandName() {
        return "/user_collection";
    }
}