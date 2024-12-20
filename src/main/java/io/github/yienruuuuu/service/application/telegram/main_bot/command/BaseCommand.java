package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class BaseCommand {
    protected final UserService userService;
    protected final LanguageService languageService;
    protected final TelegramBotClient telegramBotClient;
    protected final AnnouncementService announcementService;


    public BaseCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        this.userService = userService;
        this.languageService = languageService;
        this.telegramBotClient = telegramBotClient;
        this.announcementService = announcementService;
    }

    /**
     * 檢查使用者是否註冊
     */
    protected User checkAndGetUserIfExists(String userId, Bot mainBotEntity, Long chatId, String languageCode) {
        User user = userService.findByTelegramUserId(userId);
        //已註冊則返回
        if (user != null) return user;
        //未註冊則發送註冊提示且拋錯
        Language language = languageService.findLanguageByCodeOrDefault(languageCode);
        // 獲取 NOT_REGISTERED 的公告消息
        String notRegisteredMessage = getAnnouncementMessage(AnnouncementType.NOT_REGISTERED, language)
                .orElse("can't found register data_") + userId;
        // 發送消息
        telegramBotClient.send(SendMessage.builder()
                .chatId(chatId)
                .text(notRegisteredMessage)
                .build(), mainBotEntity);
        throw new ApiException(SysCode.NOT_REGISTER_ERROR);
    }


    /**
     * 比對語言並獲取公告消息
     */
    protected Optional<String> getAnnouncementMessage(AnnouncementType type, Language language) {
        return announcementService.findAnnounceContentByTypeAndLanguage(type, language);
    }

    /**
     * 創建 InlineKeyboardButton
     */
    protected InlineKeyboardButton createInlineButton(String text, String callBackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBackData).build();
    }

    /**
     * 檢查使用者是否有足夠的點數
     */
    protected boolean isPointNotEnough(User user, String chatId, Integer pointUsed, Bot mainBotEntity) {
        if (user.getFreePoints() < pointUsed && user.getPurchasedPoints() < pointUsed) {
            String pointNotEnoughResponse = getAnnouncementMessage(AnnouncementType.POINT_NOT_ENOUGH_MESSAGE, user.getLanguage())
                    .orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
            String filledText = pointNotEnoughResponse
                    .replace("{POINT}", pointUsed.toString());
            telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
            return true;
        }
        return false;
    }

    /**
     * 創建並返回更換語系按鈕行
     */
    protected InlineKeyboardMarkup createInlineKeyBoardForLanguageSetting() {
        InlineKeyboardButton button =
                createInlineButton("\uD83C\uDF0D change language ", "/change_language ");

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(button));
        return new InlineKeyboardMarkup(rows);
    }
}
