package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class StartCommand extends BaseCommand implements MainBotCommand {

    public StartCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }


    @Override
    public void execute(Update update, Bot mainBotEntity) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        var text = update.getMessage().getText();
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        User user = userService.findByTelegramUserId(userId);
        if (user == null) {
            user = handleRegister(update, text, userId);
            createLanguageSettingMessage(user, chatId, mainBotEntity);
        }
        // start訊息響應
        createAndSendMessage(user, chatId, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    /**
     * 處理註冊
     */
    private User handleRegister(Update update, String text, String newUserId) {
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String[] messageParts = text.trim().split("\\s+");

        // 邀請積分邏輯
        String inviterCode = messageParts.length > 1 ? messageParts[1] : null;
        int initialFreePoints = (inviterCode != null) ? addInviterPointAndCalculateNewUserPoint(inviterCode, newUserId) : 20;
        // 查詢使用者，若尚未註冊則賦予初始積分
        return userService.findByTelegramUserIdOrSaveNewUser(newUserId, userFirstName, initialFreePoints, languageCode);
    }


    /**
     * 處理邀請邏輯，增加邀請者的積分，計算新用戶初始積分
     */
    private int addInviterPointAndCalculateNewUserPoint(String inviterCode, String newUserId) {
        int inviteRewardPoint = 100; // 邀請者新增的積分數量
        int newUserFreePoint = 20; // 新用戶初始積分數量

        // 確認被邀請者是否已經註冊
        if (userService.existsByTelegramUserId(newUserId)) {
            log.warn("被邀請者 {} 已註冊，無法再次接受邀請", newUserId);
            return 0;
        }

        if (!isValidInviteCode(inviterCode)) {
            log.warn("新用戶 {} 使用了無效的邀請碼: {}", newUserId, inviterCode);
            return newUserFreePoint;
        }

        // 解析邀請者的 userId
        String inviterUserId = inviterCode.split("_")[1];
        User inviter = userService.findByTelegramUserId(inviterUserId);
        if (inviter == null) {
            log.warn("新用戶 {} 查無邀請者 ID: {}", newUserId, inviterUserId);
            return newUserFreePoint;
        }

        // 更新邀請者的積分
        userService.addPointAndSavePointLog(inviter, inviteRewardPoint, PointType.FREE, "邀請 " + newUserId, null, null);
        log.info("邀請成功，邀請者 {} 增加 100 積分", inviter.getId());
        return inviteRewardPoint + newUserFreePoint;
    }


    /**
     * 驗證邀請碼的格式
     */
    private boolean isValidInviteCode(String inviteCode) {
        return inviteCode.matches("invite_\\w+");
    }


    /**
     * 根據使用者的語言，選擇對應的開始訊息並傳送給使用者
     */
    private void createAndSendMessage(User newUser, String chatId, Bot mainBotEntity) {
        String mess = super.getAnnouncementMessage(AnnouncementType.START_MESSAGE, newUser.getLanguage())
                .orElse("can't found start message");
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(mess)
                        .build(), mainBotEntity
        );
    }

    /**
     * 根據使用者的語言，選擇對應的開始訊息並傳送給使用者
     */
    private void createLanguageSettingMessage(User newUser, String chatId, Bot mainBotEntity) {
        String mess = "Language :" + newUser.getLanguage().getFlagPattern() + newUser.getLanguage().getOriginalLanguageName();
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(mess)
                        .replyMarkup(super.createInlineKeyBoardForLanguageSetting())
                        .build(), mainBotEntity);
    }
}