package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.CardPoolService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class PoolCommand extends BaseCommand implements MainBotCommand {
    private final CardPoolService cardPoolService;

    public PoolCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.cardPoolService = cardPoolService;
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
        List<CardPool> cardPools = cardPoolService.findOpenCardPools();
        //檢查是否有開放卡池
        if (checkOpenPool(chatId, mainBotEntity, language, cardPools)) return;
        //傳送卡池資訊
        cardPools.forEach(cardPool -> createMediaMessageAndSendMedia(cardPool, language, chatId, mainBotEntity));
    }

    @Override
    public String getCommandName() {
        return "/pool";
    }

    /**
     * 檢查當前是否有開放卡池
     */
    private boolean checkOpenPool(String chatId, Bot mainBotEntity, Language language, List<CardPool> cardPools) {
        if (!cardPools.isEmpty()) {
            return false;
        }
        String noPoolText = super.getAnnouncementMessage(AnnouncementType.NO_POOL_OPEN_MESSAGE, language).orElse("There is no open card pool right now.");
        telegramBotClient.send(
                SendMessage.builder()
                        .text(noPoolText)
                        .chatId(chatId)
                        .build(), mainBotEntity
        );
        return true;
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(CardPool cardPool, Language language, String chatId, Bot mainBotEntity) {
        Resource cardPoolResource = cardPool.getResource();
        var inlineKeyboard = createInlineKeyBoard(cardPool, language);

        switch (cardPoolResource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(cardPoolResource.getFileIdMainBot()))
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(cardPoolResource.getFileIdMainBot()))
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(cardPoolResource.getFileIdMainBot()))
                            .replyMarkup(inlineKeyboard)
                            .protectContent(true)
                            .build(), mainBotEntity
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + cardPoolResource.getFileType());
        }
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(CardPool cardPool, Language language) {
        String pickCardText =
                super.getAnnouncementMessage(AnnouncementType.PICK_CARD, language).orElse("Pick card!");

        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton(pickCardText, "/pick_card " + cardPool.getId());

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));


        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}