package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.EditCardRequest;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import io.github.yienruuuuu.utils.JsonUtils;
import io.github.yienruuuuu.utils.TemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class EditCard extends DataManageBaseCommand implements DataManageCommand {
    private final CardService cardService;

    public EditCard(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardService cardService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardService = cardService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        if (update.hasMessage()) {
            handleCommand(update, fileManageBot);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update, fileManageBot);
        }
    }


    @Override
    public String getCommandName() {
        return "/edit_card";
    }

    /**
     * 處理callback query
     */
    private void handleCallbackQuery(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var callbackQueryId = update.getCallbackQuery().getId();
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        //取得卡片id
        var cardId = Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
        Card card = cardService.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        String editCard = getCommandName() + " " + TemplateGenerator.generateCardTemplate(card);
        //傳送模板
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .parseMode("MarkdownV2")
                        .text("`" + editCard + "`")
                        .build(), fileManageBot
        );
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
    }

    /**
     * 處理一般訊息
     */
    private void handleCommand(Update update, Bot fileManageBot) {
        EditCardRequest editCardRequest = parseJsonToDto(update);
        Card card = cardService.findById(editCardRequest.getId()).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setDropRate(editCardRequest.getDropRate());
        cardService.save(card);
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("已儲存")
                        .build(), fileManageBot
        );
    }

    /**
     * 將 text.JSON 字串轉換為 EditCardRequest DTO
     */
    private EditCardRequest parseJsonToDto(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring(getCommandName().length()).trim();
        // 檢查 JSON 是否為空
        if (jsonString.isEmpty()) {
            throw new ApiException(SysCode.PARAMETER_ERROR);
        }
        return JsonUtils.parseJsonToTargetDto(jsonString, EditCardRequest.class);
    }
}