package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
public class DeleteCardPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;
    private final CardService cardService;

    public DeleteCardPool(
            UserService userService,
            LanguageService languageService,
            TelegramBotClient telegramBotClient,
            AnnouncementService announcementService,
            ResourceService resourceService,
            CardPoolService cardPoolService,
            CardService cardService
    ) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
        this.cardService = cardService;
    }

    @Transactional
    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var callbackQueryId = update.getCallbackQuery().getId();
        //檢查操作權限
        super.checkUsersPermission(userId, chatId, fileManageBot);

        var cardPoolId = update.getCallbackQuery().getData().split(" ")[1];
        cardPoolService.deleteById(Integer.valueOf(cardPoolId));
        cardService.deleteByCardPoolId(Integer.valueOf(cardPoolId));

        //回傳訊息
        CompletableFuture.runAsync(() -> telegramBotClient.send(
                DeleteMessage.builder().chatId(chatId).messageId(messageId).build(), fileManageBot)
        );
        telegramBotClient.send(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQueryId)
                        .text("已刪除卡池 id = " + cardPoolId)
                        .build(),
                fileManageBot
        );
    }

    @Override
    public String getCommandName() {
        return "/delete_card_pool";
    }
}