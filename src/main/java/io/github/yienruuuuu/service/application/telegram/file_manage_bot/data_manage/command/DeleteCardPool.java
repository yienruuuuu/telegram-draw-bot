package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    public DeleteCardPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        var cardPoolId = update.getCallbackQuery().getData().split(" ")[1];
        cardPoolService.deleteById(Integer.valueOf(cardPoolId));
        telegramBotClient.send(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(update.getCallbackQuery().getId())
                        .text("已刪除 id = " + cardPoolId)
                        .build(), fileManageBot);
    }

    @Override
    public String getCommandName() {
        return "/delete_card_pool";
    }

}