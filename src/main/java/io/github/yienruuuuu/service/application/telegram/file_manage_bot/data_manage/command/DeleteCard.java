package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;

/**
 * 新增卡池圖片指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class DeleteCard extends DataManageBaseCommand implements DataManageCommand {
    private final CardService cardService;

    public DeleteCard(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardService cardService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardService = cardService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var callbackQueryId = update.getCallbackQuery().getId();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        //取得卡
        var cardId = Integer.parseInt(update.getCallbackQuery().getData().split(" ")[1]);
        cardService.deleteById(cardId);
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
        CompletableFuture.runAsync(() -> telegramBotClient.send(DeleteMessage.builder().messageId(messageId).chatId(chatId).build(), fileManageBot));
    }

    @Override
    public String getCommandName() {
        return "/delete_card";
    }
}