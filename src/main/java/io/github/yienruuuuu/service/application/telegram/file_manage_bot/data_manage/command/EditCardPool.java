package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.EditCardPoolRequest;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import io.github.yienruuuuu.utils.TemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class EditCardPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;

    public EditCardPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        if (update.hasCallbackQuery()) {
            var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
            var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            super.checkUsersPermission(userId, chatId, fileManageBot);

            //處理callback
            this.handleEditCardCallBack(chatId, update, fileManageBot);
        } else if (update.hasMessage()) {
            var userId = String.valueOf(update.getMessage().getFrom().getId());
            var chatId = String.valueOf(update.getMessage().getChat().getId());
            super.checkUsersPermission(userId, chatId, fileManageBot);

            //處理Text指令
            this.handleEditCardCommand(chatId, update, fileManageBot);
        } else {
            log.error("Invalid update type");
        }
    }

    @Override
    public String getCommandName() {
        return "/edit_card_pool";
    }

    /**
     * callback處理器
     */
    private void handleEditCardCallBack(String chatId, Update update, Bot fileManageBot) {
        var cardPoolId = update.getCallbackQuery().getData().split(" ")[1];
        CardPool cardPool = cardPoolService.findById(Integer.valueOf(cardPoolId)).orElseThrow(() -> new IllegalArgumentException("CardPool not found"));
        createCardPoolMessageAndSend(cardPool, chatId, fileManageBot);
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(update.getCallbackQuery().getId()).build(), fileManageBot));
    }

    /**
     * Text指令處理器
     */
    private void handleEditCardCommand(String chatId, Update update, Bot fileManageBot) {
        EditCardPoolRequest editCardPoolRequest = this.parseJsonToDto(update);
        if (editCardPoolRequest == null || editCardPoolRequest.getId() == null) return;
        CardPool cardPool = cardPoolService.findById(Integer.parseInt(editCardPoolRequest.getId()))
                .orElseThrow(() -> new IllegalArgumentException("CardPool not found"));
        //新增卡池
        String startAtIso = editCardPoolRequest.getStartAt() + "T08:00:00+08:00";
        String endAtIso = editCardPoolRequest.getEndAt() + "T08:00:00+08:00";
        cardPool.setStartAt(Instant.parse(startAtIso));
        cardPool.setEndAt(Instant.parse(endAtIso));
        cardPool.setOpen(editCardPoolRequest.isOpen());
        cardPool.setTexts(super.convertToTextEntities(editCardPoolRequest.getTexts()));
        cardPoolService.save(cardPool);

        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("已儲存, 卡池 id = " + cardPool.getId())
                        .build(),
                fileManageBot
        );
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createCardPoolMessageAndSend(CardPool cardPool, String chatId, Bot fileManageBot) {
        Resource cardPoolMedia = cardPool.getResource();
        String editCardPoolDetail = getCommandName() + " " + TemplateGenerator.generateCardPoolTemplate(cardPool, languageService.findAllLanguages());

        if (cardPoolMedia != null) {
            switch (cardPoolMedia.getFileType()) {
                case PHOTO -> telegramBotClient.send(
                        SendPhoto.builder()
                                .chatId(chatId)
                                .photo(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption("`" + editCardPoolDetail + "`")
                                .parseMode("MarkdownV2")
                                .build(),
                        fileManageBot
                );
                case VIDEO -> telegramBotClient.send(
                        SendVideo.builder()
                                .chatId(chatId)
                                .video(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption("`" + editCardPoolDetail + "`")
                                .parseMode("MarkdownV2")
                                .build(),
                        fileManageBot
                );
                case GIF -> telegramBotClient.send(
                        SendAnimation.builder()
                                .chatId(chatId)
                                .animation(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption("`" + editCardPoolDetail + "`")
                                .parseMode("MarkdownV2").build(),
                        fileManageBot
                );
                default -> throw new IllegalArgumentException("Unsupported FileType: " + cardPoolMedia.getFileType());
            }
        } else {
            telegramBotClient.send(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text("`" + editCardPoolDetail + "`")
                            .parseMode("MarkdownV2").build(),
                    fileManageBot);
        }
    }

    /**
     * 將 text.JSON 字串轉換為 EditResourceRequest DTO
     */
    private EditCardPoolRequest parseJsonToDto(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring(getCommandName().length()).trim();
        // 檢查 JSON 是否為空
        if (jsonString.isEmpty()) {
            return null;
        }
        return JsonUtils.parseJsonToTargetDto(jsonString, EditCardPoolRequest.class);
    }
}