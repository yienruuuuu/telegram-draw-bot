package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class GetPointCommand extends BaseCommand implements MainBotCommand {

    public GetPointCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        //檢查使用者是否註冊
        super.checkUserIfExists(update, mainBotEntity);
        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        Language language = languageService.findLanguageByCodeOrDefault(user.getLanguage().getLanguageCode());
        //傳送說明訊息
        String getPointDescription = super.getAnnouncementMessage(AnnouncementType.GET_POINT_ANNOUNCEMENT, language).orElse("NOT_FOUND");
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(getPointDescription)
                        .build(), mainBotEntity
        );
    }

    @Override
    public String getCommandName() {
        return "/get_point";
    }
}