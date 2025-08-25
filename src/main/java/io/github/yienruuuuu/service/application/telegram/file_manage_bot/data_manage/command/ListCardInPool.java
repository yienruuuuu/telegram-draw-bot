package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.ListCardInPoolDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
import java.util.concurrent.CompletableFuture;

/**
 * 新增卡池圖片指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class ListCardInPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;
    private final CardService cardService;

    public ListCardInPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService, CardService cardService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
        this.cardService = cardService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var callbackQueryId = update.getCallbackQuery().getId();
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        //取出參數
        ListCardInPoolDto dto =
                JsonUtils.parseJsonToTargetDto(update.getCallbackQuery().getData().substring(getCommandName().length()).trim(), ListCardInPoolDto.class);
        var cardPoolId = dto.getCpId();
        var pageNumber = dto.getPg() - 1;
        int pageSize = 10;

        // 取得卡池
        CardPool cardPool = cardPoolService.findById(cardPoolId).orElseThrow(() -> new IllegalArgumentException("Card pool not found."));
        Page<Card> cardPage = cardService.findAllByCardPool(cardPool, PageRequest.of(pageNumber, pageSize));

        if (cardPage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("No more cards to display.")
                    .build(), fileManageBot);
            return;
        }
        // 發送卡牌資訊
        cardPage.getContent().forEach(card -> createMediaMessageAndSendMedia(card, chatId, fileManageBot));
    }


    @Override
    public String getCommandName() {
        return "/list_card_in_pool";
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(Card card, String chatId, Bot fileManageBot) {
        var resource = card.getResource();
        var inlineKeyBoard = createInlineKeyBoard(card);
        var file = new InputFile(resource.getFileIdManageBot());
        String cardInfo = String.format(
                "Card ID: %d%nResource ID: %d%nDrop Rate: %.2f%%%nResource Rarity: %s%nResource Texts: %n%s",
                card.getId(),
                resource.getId(),
                card.getDropRate().doubleValue(),
                resource.getRarityType().name(),
                resource.getTexts().stream()
                        .map(text -> String.format("%s: %s", text.getLanguage().getLanguageCode(), text.getContent()))
                        .toList()
        );

        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(file)
                            .caption(cardInfo)
                            .replyMarkup(inlineKeyBoard)
                            .build(), fileManageBot
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(file)
                            .caption(cardInfo)
                            .replyMarkup(inlineKeyBoard)
                            .build(), fileManageBot
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(file)
                            .caption(cardInfo)
                            .replyMarkup(inlineKeyBoard)
                            .build(), fileManageBot
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(Card card) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton("修改卡牌掉落率", "/edit_card " + card.getId());
        InlineKeyboardButton editCardPoolButton =
                super.createInlineButton("刪除卡牌", "/delete_card " + card.getId());

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));
        rows.add(new InlineKeyboardRow(editCardPoolButton));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}