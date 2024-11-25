package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
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
 * 邀請推廣處理類
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class InviteCommand extends BaseCommand implements MainBotCommand {

    public InviteCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        //檢查使用者是否註冊
        super.checkUserIfExists(userId, mainBotEntity, Long.parseLong(chatId), languageCode);
        User user = userService.findByTelegramUserId(userId);
        //取得消息
        String prefixAnnounce = getAnnouncementMessage(AnnouncementType.INVITE_MESSAGE_PREFIX, user.getLanguage()).orElse(null);
        String suffixAnnounce = getAnnouncementMessage(AnnouncementType.INVITE_MESSAGE_SUFFIX, user.getLanguage()).orElse(null);
        String inviteUrl = "https://t.me/" + mainBotEntity.getBotTelegramUserName() + "?start=invite_" + update.getMessage().getFrom().getId();
        //組裝消息並使用 %n 作為換行符
        String fullMessage = String.format("%s%n%s%n%s", prefixAnnounce, inviteUrl, suffixAnnounce);
        //發送消息
        SendMessage message = SendMessage.builder().chatId(update.getMessage().getChatId()).text(fullMessage).build();
        telegramBotClient.send(message, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/invite";
    }

}