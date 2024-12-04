package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.business.impl.PointLogServiceImpl;
import io.github.yienruuuuu.service.business.impl.ResourceServiceImpl;
import io.github.yienruuuuu.service.business.impl.UserServiceImpl;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class DownloadCommand extends BaseCommand implements MainBotCommand {
    private final ResourceServiceImpl resourceService;

    public DownloadCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceServiceImpl resourceService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.resourceService = resourceService;
    }

    @Transactional
    @Override
    public void execute(Update update, Bot mainBotEntity) {
        if (checkIsIllegal(update)) return;
        var callbackQueryId = update.getCallbackQuery().getId();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var data = update.getCallbackQuery().getData().split(" ")[1];
        var pointUsed = 20;
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), mainBotEntity));
        // 查詢用戶
        User user = userService.findByTelegramUserId(userId);
        // 檢查用戶點數
        if (super.isPointNotEnough(user, chatId, pointUsed, mainBotEntity)) return;
        // 查詢資源
        Resource resource = resourceService.findByUniqueId(data).orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
        // 扣款
        deductPoints(user, user.getFreePoints() > pointUsed, -pointUsed);
        // 發送抽卡結果
        CompletableFuture.runAsync(() -> telegramBotClient.send(DeleteMessage.builder().chatId(chatId).messageId(messageId).build(), mainBotEntity));
        createMediaMessageAndSendMedia(resource, chatId, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/download";
    }

    /**
     * 檢查是否為非法命令
     */
    private boolean checkIsIllegal(Update update) {
        if (update.hasMessage()) {
            log.warn("Illegal command occurred user = {} ", update.getMessage().getFrom());
            return true;
        }
        return false;
    }

    /**
     * 扣款
     */
    private void deductPoints(User user, boolean isFree, Integer pointUsed) {
        PointType pointType = isFree ? PointType.FREE : PointType.PAID;
        userService.addPointAndSavePointLog(user, pointUsed, pointType, getCommandName(), null, null);
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(Resource resource, String chatId, Bot mainBotEntity) {
        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(resource.getFileIdMainBot()))
                            .build(), mainBotEntity
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(resource.getFileIdMainBot()))
                            .build(), mainBotEntity
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(resource.getFileIdMainBot()))
                            .build(), mainBotEntity
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }
}