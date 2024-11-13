package io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.RoleType;
import io.github.yienruuuuu.config.AppConfig;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import io.github.yienruuuuu.utils.ResourceTemplateGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class UploadFileBaseCommand {
    protected final UserService userService;
    protected final TelegramBotClient telegramBotClient;
    protected final ResourceService resourceService;
    protected final AppConfig appConfig;
    protected final AnnouncementService announcementService;
    protected final LanguageService languageService;


    public UploadFileBaseCommand(UserService userService, TelegramBotClient telegramBotClient, ResourceService resourceService, AppConfig appConfig, AnnouncementService announcementService, LanguageService languageService) {
        this.userService = userService;
        this.telegramBotClient = telegramBotClient;
        this.resourceService = resourceService;
        this.appConfig = appConfig;
        this.announcementService = announcementService;
        this.languageService = languageService;
    }


    /**
     * 檢查使用者權限
     */
    protected void checkUsersPermission(String userId, String chatId, Bot mainBotEntity) {
        User user = userService.findByTelegramUserId(userId);
        if (user != null && user.getRole().hasPermission(RoleType.MANAGER)) {
            return;
        }
        SendMessage message = SendMessage.builder().chatId(chatId).text(SysCode.PERMISSION_DENIED_ERROR.getMessage()).build();
        telegramBotClient.send(message, mainBotEntity);
        throw new ApiException(SysCode.PERMISSION_DENIED_ERROR);
    }


    /**
     * 傳送完善resource的json模板
     */
    protected void sendEditResourceTemplate(Resource newResource, String chatId, Bot fileBotEntity) {
        String template = ResourceTemplateGenerator.generateTemplateWithData(newResource, languageService.findAllLanguages());
        SendMessage askForCompleteResourceSetting = SendMessage.builder()
                .chatId(chatId)
                .text("/edit_resource " + template)
                .build();
        telegramBotClient.send(askForCompleteResourceSetting, fileBotEntity);
    }
}
