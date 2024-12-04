package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class MyStatusCommand extends BaseCommand implements MainBotCommand {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public MyStatusCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        String userId = String.valueOf(update.getMessage().getFrom().getId());
        String chatId = String.valueOf(update.getMessage().getChatId());
        var languageCode = update.getMessage().getFrom().getLanguageCode();
        //檢查使用者是否註冊
        User user = super.checkAndGetUserIfExists(userId, mainBotEntity, Long.parseLong(chatId), languageCode);
        //模板查詢
        String userStatusTemplate = super.getAnnouncementMessage(AnnouncementType.USER_STATUS_QUERY, user.getLanguage())
                .orElseThrow(() -> new ApiException(SysCode.ANNOUNCE_UNEXPECTED_ERROR));
        //替換填充模板
        String filledText = userStatusTemplate
                .replace("{USER_ID}", user.getTelegramUserId())
                .replace("{USER_ROLE}", user.getRole().name())
                .replace("{FREE_POINT}", String.valueOf(user.getFreePoints()))
                .replace("{PAID_POINT}", String.valueOf(user.getPurchasedPoints()))
                .replace("{REGISTER_TIME}", DATE_FORMATTER.format(user.getCreatedAt()));
        //傳送
        telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/my_status";
    }
}