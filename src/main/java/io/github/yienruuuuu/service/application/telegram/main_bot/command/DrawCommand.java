package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class DrawCommand extends BaseCommand implements MainBotCommand {
    private final CardPoolService cardPoolService;
    private final UserDrawStatusService userDrawStatusService;
    private final UserDrawLogService userDrawLogService;

    public DrawCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, CardPoolService cardPoolService, UserDrawStatusService userDrawStatusService, UserDrawLogService userDrawLogService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.cardPoolService = cardPoolService;
        this.userDrawStatusService = userDrawStatusService;
        this.userDrawLogService = userDrawLogService;
    }

    @Transactional
    @Override
    public void execute(Update update, Bot mainBotEntity) {
        if (checkIsIllegal(update)) return;
        var callbackQueryId = update.getCallbackQuery().getId();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var data = Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
        var pointUsed = 5;
        // 將兩個發送請求異步執行
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), mainBotEntity));
        CompletableFuture.runAsync(() -> telegramBotClient.send(DeleteMessage.builder().chatId(chatId).messageId(messageId).build(), mainBotEntity));

        // 查詢用戶
        User user = userService.findByTelegramUserId(userId);
        // 檢查用戶點數
        if (isPointNotEnough(user, chatId, pointUsed, mainBotEntity)) return;
        boolean isFree = user.getFreePoints() > pointUsed;
        // 扣款
        deductPoints(user, isFree, pointUsed);
        // 取得卡池
        CardPool cardPool = cardPoolService.findById(data).orElseThrow(() -> new ApiException(SysCode.CARD_POOL_NOT_EXIST));
        // 確保用戶抽卡狀態存在
        UserDrawStatus drawStatus = queryOrCreateDrawStatus(user, cardPool);
        // 準備權重
        Map<String, Double> weights = prepareWeights(cardPool, drawStatus);

        // 執行加權隨機選擇
        String selectedCardId = getWeightedRandom(weights);
        Card selectedCard = cardPool.getCards().stream()
                .filter(card -> String.valueOf(card.getId()).equals(selectedCardId))
                .findFirst()
                .orElseThrow(() -> new ApiException(SysCode.CARD_NOT_FOUND));

        // 更新抽卡狀態
        updateDrawStatus(drawStatus, selectedCard, weights);
        // 紀錄日誌
        saveDrawLog(pointUsed, isFree, user, cardPool, selectedCard);
        // 發送抽卡結果
        createMediaMessageAndSendMedia(selectedCard, user.getLanguage(), chatId, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/draw";
    }

    /**
     * 檢查是否為非法命令
     */
    private boolean checkIsIllegal(Update update) {
        if (update.hasMessage()) {
            log.warn("Illegal command occurred user = {} ", update.getMessage().getFrom());
            return true;
        }
        return false;
    }

    private boolean isPointNotEnough(User user, String chatId, Integer pointUsed, Bot mainBotEntity) {
        if (user.getFreePoints() < pointUsed && user.getPurchasedPoints() < pointUsed) {
            String pointNotEnoughResponse = super.getAnnouncementMessage(AnnouncementType.POINT_NOT_ENOUGH_MESSAGE, user.getLanguage())
                    .orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
            String filledText = pointNotEnoughResponse
                    .replace("{POINT}", pointUsed.toString());
            telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
            return true;
        }
        return false;
    }

    /**
     * 扣款
     */
    private void deductPoints(User user, boolean isFree, Integer pointUsed) {
        if (isFree) {
            user.setFreePoints(user.getFreePoints() - pointUsed);
        } else {
            user.setPurchasedPoints(user.getPurchasedPoints() - pointUsed);
        }
        userService.save(user);
    }

    /**
     * 查詢或建立用戶卡池抽卡狀態
     */
    private UserDrawStatus queryOrCreateDrawStatus(User user, CardPool cardPool) {
        return userDrawStatusService.findByUserAndCardPool(user, cardPool).orElseGet(() -> {
            UserDrawStatus newStatus = UserDrawStatus.builder()
                    .user(user)
                    .cardPool(cardPool)
                    .totalDrawCount(0)
                    .urDrawCount(0)
                    .ssrDrawCount(0)
                    .dynamicDropRate(new HashMap<>())
                    .lastDrawTime(null)
                    .build();
            return userDrawStatusService.save(newStatus);
        });
    }

    /**
     * 準備權重
     */
    private Map<String, Double> prepareWeights(CardPool cardPool, UserDrawStatus drawStatus) {
        Map<String, Double> weights = new HashMap<>();
        for (Card card : cardPool.getCards()) {
            String cardId = String.valueOf(card.getId());
            double weight = drawStatus.getDynamicDropRate()
                    .getOrDefault(cardId, card.getDropRate().doubleValue());
            weights.put(cardId, weight);
        }
        return weights;
    }

    /**
     * 執行加權隨機選擇
     */
    private String getWeightedRandom(Map<String, Double> weights) {
        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        double random = Math.random() * totalWeight;
        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            random -= entry.getValue();
            if (random <= 0) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Weighted random selection failed.");
    }

    /**
     * 更新抽卡狀態
     */
    private void updateDrawStatus(UserDrawStatus drawStatus, Card selectedCard, Map<String, Double> weights) {
        // 更新抽卡總次數
        drawStatus.setTotalDrawCount(drawStatus.getTotalDrawCount() + 1);

        // 根據卡片稀有度更新計數與保底
        if (selectedCard.getResource().getRarityType().equals(RarityType.UR)) {
            drawStatus.setUrDrawCount(drawStatus.getUrDrawCount() + 1);
        } else if (selectedCard.getResource().getRarityType().equals(RarityType.SSR)) {
            drawStatus.setSsrDrawCount(drawStatus.getSsrDrawCount() + 1);
        }

        // 動態調整權重（減少已抽到卡片的權重）
        String cardId = String.valueOf(selectedCard.getId());
        double adjustedWeight = Math.max(weights.get(cardId) * 0.8, 1.0); // 最低權重為 1.0
        drawStatus.getDynamicDropRate().put(cardId, adjustedWeight);
        userDrawStatusService.save(drawStatus);
    }

    /**
     * 紀錄抽卡日誌
     */
    private void saveDrawLog(Integer pointsUsed, boolean isFree, User user, CardPool cardPool, Card selectedCard) {
        UserDrawLog userDrawLog = UserDrawLog.builder()
                .pointsUsed(pointsUsed)
                .isFree(isFree)
                .card(selectedCard)
                .user(user)
                .cardPool(cardPool)
                .build();
        userDrawLogService.save(userDrawLog);
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(Card card, Language language, String chatId, Bot mainBotEntity) {
        Resource cardResource = card.getResource();
        var text = cardResource.getTexts().stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR))
                .getContent();
        var inlineKeyboard = createInlineKeyBoard(card, language);

        switch (cardResource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(cardResource.getFileIdMainBot()))
                            .caption(text)
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(cardResource.getFileIdMainBot()))
                            .caption(text)
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(cardResource.getFileIdMainBot()))
                            .caption(text)
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + cardResource.getFileType());
        }
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(Card card, Language language) {
        String pickCardText = super.getAnnouncementMessage(AnnouncementType.GET_DOWNLOAD_PERMISSION_BUTTON, language)
                .orElse("get Download/share Permission by 20 point");

        InlineKeyboardButton button =
                super.createInlineButton(pickCardText, "/download " + card.getResource().getUniqueId());

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(button));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }

}