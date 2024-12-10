package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * language指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class LanguageCommand extends BaseCommand implements MainBotCommand {

    public LanguageCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }


    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = update.getMessage().getChatId();
        var languageCode = update.getMessage().getFrom().getLanguageCode();
        User user = super.checkAndGetUserIfExists(userId, mainBotEntity, chatId, languageCode);
        createLanguageSettingMessage(user, String.valueOf(chatId), mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/language";
    }

    /**
     * 根據使用者的語言，選擇對應的開始訊息並傳送給使用者
     */
    private void createLanguageSettingMessage(User user, String chatId, Bot mainBotEntity) {
        String mess = "Language :" + user.getLanguage().getFlagPattern() + user.getLanguage().getOriginalLanguageName();
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(mess)
                        .replyMarkup(super.createInlineKeyBoardForLanguageSetting())
                        .build(), mainBotEntity);
    }

}