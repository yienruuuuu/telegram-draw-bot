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
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 色子遊戲響應
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class PaidCommand extends BaseCommand implements MainBotCommand {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    private final List<Integer> numbers = Arrays.asList(5, 10, 20);

    public PaidCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update, mainBotEntity);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update, mainBotEntity);
        } else if (update.hasPreCheckoutQuery()) {
            handlePreCheckoutQuery(update, mainBotEntity);
        } else if (update.hasMessage() && update.getMessage().hasSuccessfulPayment()) {
            handleSuccessPayment(update, mainBotEntity);
        }
    }

    @Override
    public String getCommandName() {
        return "/paid";
    }

    /**
     * 處理支付前確認訊息
     */
    private void handleSuccessPayment(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var languageCode = update.getMessage().getFrom().getLanguageCode();
        var successfulPayment = update.getMessage().getSuccessfulPayment();
        var payAmount = successfulPayment.getTotalAmount();
        //檢查使用者是否註冊
        super.checkUserIfExists(userId, mainBotEntity, Long.parseLong(chatId), languageCode);
        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        Language language = languageService.findLanguageByCodeOrDefault(user.getLanguage().getLanguageCode());
        //檢核
        if (!numbers.contains(payAmount)) return;
        //增加付費積分
        User userAfter = userService.addPointAndSavePointLog(user, payAmount, PointType.PAID, getCommandName(), successfulPayment.getProviderPaymentChargeId(), successfulPayment.getTelegramPaymentChargeId());
        //傳送成功訊息
        sendSuccessMessage(language, chatId, mainBotEntity, user, userAfter.getPurchasedPoints(), payAmount);
    }

    /**
     * 處理支付前確認訊息
     */
    private void handlePreCheckoutQuery(Update update, Bot mainBotEntity) {
        var preCheckoutQuery = update.getPreCheckoutQuery();
        var userId = String.valueOf(preCheckoutQuery.getFrom().getId());
        var payAmount = preCheckoutQuery.getTotalAmount();

        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        //檢核
        if (user == null) return;
        if (!numbers.contains(payAmount)) return;
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery(preCheckoutQuery.getId(), true);
        telegramBotClient.send(answerPreCheckoutQuery, mainBotEntity);
    }

    /**
     * 處理訊息
     */
    private void handleMessage(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var languageCode = update.getMessage().getFrom().getLanguageCode();
        //檢查使用者是否註冊
        super.checkUserIfExists(userId, mainBotEntity, Long.parseLong(chatId), languageCode);
        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        Language language = languageService.findLanguageByCodeOrDefault(user.getLanguage().getLanguageCode());

        createMessageAndSend(language, chatId, mainBotEntity);
    }

    /**
     * 處理callback query
     */
    private void handleCallbackQuery(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var callbackQueryId = update.getCallbackQuery().getId();
        // 將兩個發送請求異步執行
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), mainBotEntity));
        CompletableFuture.runAsync(() -> telegramBotClient.send(DeleteMessage.builder().chatId(chatId).messageId(messageId).build(), mainBotEntity));
        //查詢必要資訊
        var points = Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
        if (!numbers.contains(points)) return;
        telegramBotClient.send(
                SendInvoice.builder()
                        .chatId(chatId)
                        .title("Exchange Points⭐")
                        .description("Get " + points + " Points by Telegram Star⭐")
                        .payload(userId)
                        .currency("XTR")
                        .protectContent(true)
                        .price(new LabeledPrice("Price", points))
                        .providerToken("").build(), mainBotEntity);
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMessageAndSend(Language language, String chatId, Bot mainBotEntity) {
        String pickCardText = super.getAnnouncementMessage(AnnouncementType.PAYMENT_ANNOUNCEMENT, language)
                .orElse("1 Telegram Star⭐ = 1 Paid Points\n" + "How many do you want to purchase?");
        var inlineKeyboard = createInlineKeyBoard();

        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(pickCardText)
                        .protectContent(true)
                        .replyMarkup(inlineKeyboard)
                        .build(), mainBotEntity);
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard() {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        numbers.forEach(number -> {
            InlineKeyboardButton button =
                    super.createInlineButton(number + "⭐ = " + number + " Points", "/paid " + number);
            rows.add(new InlineKeyboardRow(button));
        });

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }

    /**
     * 傳送成功訊息
     */
    private void sendSuccessMessage(Language language, String chatId, Bot mainBotEntity, User user, Integer balanceBefore, Integer payAmount) {
        //模板查詢
        String userStatusTemplate = super.getAnnouncementMessage(AnnouncementType.USER_STATUS_QUERY, language)
                .orElseThrow(() -> new ApiException(SysCode.ANNOUNCE_UNEXPECTED_ERROR));
        //替換填充模板
        String filledText = userStatusTemplate
                .replace("{USER_ID}", user.getTelegramUserId())
                .replace("{USER_ROLE}", user.getRole().name())
                .replace("{FREE_POINT}", String.valueOf(user.getPurchasedPoints()))
                .replace("{PAID_POINT}", balanceBefore + "+" + payAmount)
                .replace("{REGISTER_TIME}", DATE_FORMATTER.format(user.getCreatedAt()));
        //傳送
        telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
    }
}