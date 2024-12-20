package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddCardPoolPicDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * 預設查詢cardPool命令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/15
 */
@Slf4j
@Component
public class ListCardPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;

    public ListCardPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        // 提取分頁參數，默認為第 1 頁
        int pageNumber = super.extractPageNumber(update.getMessage().getText()) - 1; // Pageable 的頁碼從 0 開始
        int pageSize = 5;

        Page<CardPool> cardPoolPage = cardPoolService.findAllByPage(PageRequest.of(pageNumber, pageSize));

        if (cardPoolPage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("No more CardPool to display.")
                    .build(), fileManageBot);
            return;
        }

        //傳送cardPool
        cardPoolPage.getContent().forEach(cardPool -> createCardPoolMessageAndSend(cardPool, chatId, fileManageBot));

        // 附加分頁信息
        String pageInfo = String.format(" %d of %d", cardPoolPage.getNumber() + 1, cardPoolPage.getTotalPages());

        // 創建按鈕行和鍵盤
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(getCommandName() + " " + (cardPoolPage.getNumber() + 2)))
                .resizeKeyboard(true)
                .build();

        // 傳送下頁按鈕
        telegramBotClient.send(SendMessage.builder()
                .chatId(chatId)
                .text("Page :" + pageInfo)
                .replyMarkup(keyboard)
                .build(), fileManageBot);
    }


    @Override
    public String getCommandName() {
        return "/list_card_pool";
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createCardPoolMessageAndSend(CardPool cardPool, String chatId, Bot fileManageBot) {
        Resource cardPoolMedia = cardPool.getResource();
        String cardPoolDetail = String.join("\n",
                "活動id : " + cardPool.getId(),
                "開始於 : " + cardPool.getStartAt(),
                "結束於 : " + cardPool.getEndAt(),
                "啟用狀態 : " + cardPool.isOpen(),
                "資源id : " + (cardPool.getResource() == null ? "N/A" : cardPool.getResource().getId())
        );

        if (cardPoolMedia != null) {
            switch (cardPoolMedia.getFileType()) {
                case PHOTO -> telegramBotClient.send(
                        SendPhoto.builder()
                                .chatId(chatId)
                                .photo(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .replyMarkup(createInlineKeyBoard(cardPool))
                                .build(),
                        fileManageBot
                );
                case VIDEO -> telegramBotClient.send(
                        SendVideo.builder()
                                .chatId(chatId)
                                .video(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .replyMarkup(createInlineKeyBoard(cardPool))
                                .build(),
                        fileManageBot
                );
                case GIF -> telegramBotClient.send(
                        SendAnimation.builder()
                                .chatId(chatId)
                                .animation(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .replyMarkup(createInlineKeyBoard(cardPool))
                                .build(),
                        fileManageBot
                );
                default -> throw new IllegalArgumentException("Unsupported FileType: " + cardPoolMedia.getFileType());
            }
        } else {
            telegramBotClient.send(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(cardPoolDetail)
                            .replyMarkup(createInlineKeyBoard(cardPool))
                            .build(), fileManageBot);
        }
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(CardPool cardPool) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton("新增卡池照片", "/add_pool_pic " + JsonUtils.parseJson(new AddCardPoolPicDto(cardPool.getId(), 1, "")));
        InlineKeyboardButton deleteCardPoolButton =
                super.createInlineButton("刪除卡池", "/delete_card_pool " + cardPool.getId());
        InlineKeyboardButton editCardPoolButton =
                super.createInlineButton("修改卡池資訊", "/edit_card_pool " + cardPool.getId());
        InlineKeyboardButton quickEditOpenStatusButton =
                super.createInlineButton("啟用/停用卡池", "/quick_edit_card_pool_status " + cardPool.getId());
        InlineKeyboardButton editCardsInPoolButton =
                super.createInlineButton("編輯卡牌", "/edit_cards_in_pool " + cardPool.getId());

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));
        rows.add(new InlineKeyboardRow(deleteCardPoolButton));
        rows.add(new InlineKeyboardRow(editCardPoolButton));
        rows.add(new InlineKeyboardRow(quickEditOpenStatusButton));
        rows.add(new InlineKeyboardRow(editCardsInPoolButton));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}
