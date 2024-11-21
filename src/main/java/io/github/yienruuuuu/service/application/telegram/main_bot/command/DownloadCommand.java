package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.PointLog;
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
    private final PointLogServiceImpl pointLogService;

    public DownloadCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceServiceImpl resourceService, PointLogServiceImpl pointLogService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.resourceService = resourceService;
        this.pointLogService = pointLogService;
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
        // 將兩個發送請求異步執行
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), mainBotEntity));
        CompletableFuture.runAsync(() -> telegramBotClient.send(DeleteMessage.builder().chatId(chatId).messageId(messageId).build(), mainBotEntity));

        // 查詢用戶
        User user = userService.findByTelegramUserId(userId);
        // 檢查用戶點數
        if (super.isPointNotEnough(user, chatId, pointUsed, mainBotEntity)) return;
        boolean isFree = user.getFreePoints() > pointUsed;
        // 查詢資源
        Resource resource = resourceService.findByUniqueId(data).orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
        // 扣款
        deductPoints(user, isFree, pointUsed);
        // 發送抽卡結果
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
        PointLog pointLog = PointLog.builder().reason(getCommandName()).user(user).amount(-pointUsed).build();
        if (isFree) {
            pointLog.setPointType(PointType.FREE);
            pointLog.setBalanceBefore(user.getFreePoints());
            Integer pointAfter = user.getFreePoints() - pointUsed;
            pointLog.setBalanceAfter(pointAfter);
            user.setFreePoints(pointAfter);
        } else {
            pointLog.setPointType(PointType.PAID);
            pointLog.setBalanceBefore(user.getPurchasedPoints());
            Integer pointAfter = user.getPurchasedPoints() - pointUsed;
            pointLog.setBalanceAfter(pointAfter);
            user.setPurchasedPoints(pointAfter);
        }
        pointLogService.save(pointLog);
        userService.save(user);
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