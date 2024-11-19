package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddCardToPoolDto;
import io.github.yienruuuuu.bean.dto.ListCardInPoolDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增卡池圖片指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class EditCardsInPoolCallback extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;

    public EditCardsInPoolCallback(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        //取得卡池
        var cardPoolId = Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
        CardPool cardPool = cardPoolService.findById(cardPoolId).orElseThrow(() -> new IllegalArgumentException("CardPool not found"));

        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(createInlineKeyBoard(cardPool))
                .build();
        telegramBotClient.send(
                editMessageReplyMarkup,
                fileManageBot
        );
    }

    @Override
    public String getCommandName() {
        return "/edit_cards_in_pool";
    }


    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(CardPool cardPool) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton("新增卡牌", "/add_card_to_pool " + JsonUtils.parseJson(new AddCardToPoolDto(cardPool.getId(), 1, null)));
        InlineKeyboardButton editCardPoolButton =
                super.createInlineButton("查詢池內卡牌", "/list_card_in_pool " + JsonUtils.parseJson(new ListCardInPoolDto(cardPool.getId(), 1)));

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));
        rows.add(new InlineKeyboardRow(editCardPoolButton));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}