package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
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
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        if (update.hasMessage()) {
            handleMessage(update, mainBotEntity);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update, mainBotEntity);
        }
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    /**
     * 處理message
     */
    private void handleMessage(Update update, Bot mainBotEntity) {
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        String chatId = String.valueOf(update.getMessage().getChatId());
        var textInUpdate = update.getMessage().getText();
        User user = userService.findByTelegramUserId(String.valueOf(update.getMessage().getFrom().getId()));
        if (user == null) {
            sendTermsOfUse(chatId, textInUpdate, languageCode, mainBotEntity);
            return;
        }

        // start訊息響應
        SendMessage msgToTelegram = createSendMessageOfAnnouncement(user, chatId);
        telegramBotClient.send(msgToTelegram, mainBotEntity);
    }

    /**
     * 處理callback query
     */
    private void handleCallbackQuery(Update update, Bot mainBotEntity) {
        String languageCode = update.getCallbackQuery().getFrom().getLanguageCode();
        String inviteeUserId = update.getCallbackQuery().getFrom().getId().toString();
        String userFirstName = update.getCallbackQuery().getFrom().getFirstName();
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        String[] messageParts = update.getCallbackQuery().getData().trim().split("\\s+");
        CompletableFuture.runAsync(() ->
                telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(update.getCallbackQuery().getId()).build(), mainBotEntity));
        CompletableFuture.runAsync(() ->
                telegramBotClient.send(DeleteMessage.builder().messageId(update.getCallbackQuery().getMessage().getMessageId()).chatId(chatId).build(), mainBotEntity));
        //檢查是否有同意條約
        if (messageParts.length > 1 && "refuse".equals(messageParts[1])) {
            telegramBotClient.send(SendMessage.builder().chatId(chatId).text(":( see you next time!").build(), mainBotEntity);
            return;
        }

        // 邀請積分邏輯
        String inviteCode = messageParts.length > 1 ? messageParts[1] : null;
        int initialFreePoints = (inviteCode != null) ? handleInviterPointAndCalculateNewUserPoint(inviteCode, inviteeUserId) : 20;
        // 查詢使用者，若尚未註冊則賦予初始積分
        User user = registerUser(inviteeUserId, userFirstName, languageCode, initialFreePoints);
        // start訊息響應
        SendMessage msgToTelegram = createSendMessageOfAnnouncement(user, String.valueOf(chatId));
        telegramBotClient.send(msgToTelegram, mainBotEntity);
    }

    private void sendTermsOfUse(String chatId, String textInUpdate, String languageCode, Bot mainBotEntity) {
        Language language = languageService.findLanguageByCodeOrDefault(languageCode);
        String askForReadTerm = super.getAnnouncementMessage(AnnouncementType.TERM_MESSAGE, language).orElse("Please read the terms of use first.");
        String termFileId = languageCode.equals("zh-hant")
                ? "AgACAgUAAxkBAAP2Zz2nXnhBVGD-RC31nJhBYKfPDHUAAhTGMRvquPFVLYBH0PSwz54BAAMCAAN5AAM2BA"
                : "AgACAgUAAxkBAAP3Zz2nmqnJoMRqc8GQH2IKy4fg1p0AAhbGMRvquPFV8muFwNUmCwcBAAMCAAN5AAM2BA";
        telegramBotClient.send(
                SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(termFileId))
                        .caption(askForReadTerm)
                        .replyMarkup(createInlineKeyBoard(textInUpdate))
                        .build(), mainBotEntity);

    }


    /**
     * 處理邀請邏輯，增加邀請者的積分，計算新用戶初始積分
     */
    private int handleInviterPointAndCalculateNewUserPoint(String inviteCode, String newUserId) {
        int inviteRewardPoint = 100; // 邀請得的積分數量
        int newUserFreePoint = 20; // 新用戶初始積分數量

        // 確認被邀請者是否已經註冊
        if (userService.existsByTelegramUserId(newUserId)) {
            log.warn("被邀請者 {} 已註冊，無法再次接受邀請", newUserId);
            return 0;
        }

        if (!isValidInviteCode(inviteCode)) {
            log.warn("新用戶 {} 使用了無效的邀請碼: {}", newUserId, inviteCode);
            return newUserFreePoint;
        }

        // 解析邀請者的 userId
        String inviterUserId = inviteCode.split("_")[1];
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

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(String textInUpdate) {
        InlineKeyboardButton agreeIcon = super.createInlineButton("✅", textInUpdate);
        InlineKeyboardButton disagreeIcon = super.createInlineButton("❌", getCommandName() + " refuse");

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(agreeIcon, disagreeIcon));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}