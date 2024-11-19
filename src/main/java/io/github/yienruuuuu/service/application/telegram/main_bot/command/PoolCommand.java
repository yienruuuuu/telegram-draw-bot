package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.CardPoolService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class PoolCommand extends BaseCommand implements MainBotCommand {
    private final CardPoolService cardPoolService;

    public PoolCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = update.getMessage().getChatId();

        List<CardPool> cardPools = cardPoolService.findOpenCardPools();
        if (cardPools.isEmpty()) {
            telegramBotClient.send(
                    SendMessage.builder()
                            .text("There is no open card pool right now.")
                            .chatId(chatId)
                            .build(), mainBotEntity
            );
            return;
        }

    }

    @Override
    public String getCommandName() {
        return "/pool";
    }
}