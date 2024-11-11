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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class StartCommand extends BaseCommand implements MainBotCommand {
    private final TelegramBotClient botClient;

    public StartCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, TelegramBotClient botClient) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.botClient = botClient;
    }


    @Override
    public void execute(Update update, Bot mainBotEntity) {
        String inviteeUserId = update.getMessage().getFrom().getId().toString();
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        String chatId = String.valueOf(update.getMessage().getChatId());
        String[] messageParts = update.getMessage().getText().trim().split("\\s+");
        String inviteCode = messageParts.length > 1 ? messageParts[1] : null;

        // 邀請積分邏輯
        int initialFreePoints = (inviteCode != null) ? processInvitation(inviteCode, inviteeUserId) : 0;
        // 查詢使用者，若尚未註冊則賦予初始積分
        User user = registerUser(inviteeUserId, userFirstName, languageCode, initialFreePoints);
        // start訊息響應
        SendMessage msgToTelegram = createSendMessageOfAnnouncement(user, chatId);
        botClient.send(msgToTelegram, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }


    /**
     * 處理邀請邏輯，根據邀請碼增加積分
     */
    private int processInvitation(String inviteCode, String inviteeUserId) {
        if (!isValidInviteCode(inviteCode)) {
            log.warn("無效的邀請碼: {}", inviteCode);
            return 0;
        }

        // 解析邀請者的 userId
        String inviterUserId = inviteCode.split("_")[1];
        User inviter = userService.findByTelegramUserId(inviterUserId);
        if (inviter == null) {
            log.warn("找不到邀請者 ID: {}", inviterUserId);
            return 0;
        }

        // 確認被邀請者是否已經註冊
        if (userService.existsByTelegramUserId(inviteeUserId)) {
            log.warn("被邀請者 {} 已註冊，無法再次接受邀請", inviteeUserId);
            return 0;
        }

        // 更新邀請者的積分
        inviter.setFreePoints(inviter.getFreePoints() + 10);
        userService.save(inviter);

        log.info("邀請成功，邀請者 {} 和被邀請者 {} 都增加 10 積分", inviter.getId(), inviteeUserId);
        return 10; // 返回被邀請者的初始積分
    }


    /**
     * 驗證邀請碼的格式
     */
    private boolean isValidInviteCode(String inviteCode) {
        return inviteCode.matches("invite_\\w+");
    }


    /**
     * 查詢使用者，若未註冊則賦予初始積分
     */
    private User registerUser(String inviteeUserId, String userFirstName, String languageCode, int initialFreePoints) {
        Language language = languageService.findLanguageByCodeOrDefault(languageCode);
        return userService.findByTelegramUserIdOrSaveNewUser(inviteeUserId, userFirstName, language, initialFreePoints);
    }

    /**
     * 根據使用者的語言，選擇對應的開始訊息並傳送給使用者
     */
    private SendMessage createSendMessageOfAnnouncement(User newUser, String chatId) {
        Language language = newUser.getLanguage();
        String mess = super.getAnnouncementMessage(AnnouncementType.START_MESSAGE, language)
                .orElse("can't found start message");
        return SendMessage.builder()
                .chatId(chatId)
                .text(mess)
                .build();
    }
}