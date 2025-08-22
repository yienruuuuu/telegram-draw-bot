package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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

    public DeleteCardPool(
            UserService userService,
            LanguageService languageService,
            TelegramBotClient telegramBotClient,
            AnnouncementService announcementService,
            ResourceService resourceService,
            CardPoolService cardPoolService
    ) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
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
        CardPool cardPool = cardPoolService.findById(Integer.valueOf(cardPoolId))
                .orElseThrow(() -> new ApiException(SysCode.CARD_POOL_NOT_FOUND));

        List<Resource> resources = Stream.concat(
                Stream.of(cardPool.getResource()),
                cardPool.getCards().stream().map(Card::getResource)
        ).toList();

        // 將 isInUsed 改為 false 並存回資料庫
        resources.forEach(super::markAsUnused);

        cardPoolService.delete(cardPool);

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