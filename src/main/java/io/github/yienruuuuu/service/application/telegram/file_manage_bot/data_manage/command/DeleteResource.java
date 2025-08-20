package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
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
public class DeleteResource extends DataManageBaseCommand implements DataManageCommand {
    private final CardService cardService;
    private final CardPoolService cardPoolService;

    public DeleteResource(
            UserService userService,
            LanguageService languageService,
            TelegramBotClient telegramBotClient,
            AnnouncementService announcementService,
            ResourceService resourceService,
            CardService cardService,
            CardPoolService cardPoolService
    ) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardService = cardService;
        this.cardPoolService = cardPoolService;
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
        String resourceUniqueId = update.getCallbackQuery().getData().split(" ")[1];
        // 檢查資源是否被卡或卡池使用
        this.checkResourceIsUsedByCardOrCardPool(resourceUniqueId, fileManageBot, callbackQueryId);

        resourceService.deleteByUniqueId(resourceUniqueId);
        CompletableFuture.runAsync(() -> telegramBotClient.send(
                AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).text("已刪除resource").build(), fileManageBot)
        );
        CompletableFuture.runAsync(() -> telegramBotClient.send(
                DeleteMessage.builder().messageId(messageId).chatId(chatId).build(), fileManageBot)
        );
    }

    @Override
    public String getCommandName() {
        return "/delete_resource";
    }


    /**
     * 檢查資源是否被卡或卡池使用
     */
    private void checkResourceIsUsedByCardOrCardPool(
            String resourceUniqueId,
            Bot fileManageBot,
            String callbackQueryId
    ) {
        Resource rs = resourceService.findByUniqueId(resourceUniqueId)
                .orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUND));

        if (cardService.existsByResourceId(rs.getId()) || cardPoolService.existsByResourceId(rs.getId())) {
            CompletableFuture.runAsync(() -> telegramBotClient.send(
                    AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).text(SysCode.RESOURCE_HAS_BEEN_CARD.getMessage()).build(), fileManageBot)
            );
            throw new ApiException(SysCode.RESOURCE_HAS_BEEN_CARD);
        }
    }
}