package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.Text;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.RoleType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class DataManageBaseCommand {
    protected final ObjectMapper objectMapper;
    protected final UserService userService;
    protected final LanguageService languageService;
    protected final TelegramBotClient telegramBotClient;
    protected final AnnouncementService announcementService;
    protected final ResourceService resourceService;


    public DataManageBaseCommand(ObjectMapper objectMapper, UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.languageService = languageService;
        this.telegramBotClient = telegramBotClient;
        this.announcementService = announcementService;
        this.resourceService = resourceService;
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
     * 比對語言並獲取公告消息
     */
    protected Optional<String> getAnnouncementMessage(AnnouncementType type, Language language) {
        return announcementService.findAnnouncementByType(type)
                .getTexts()
                .stream()
                .filter(text -> text.getLanguage().equals(language))
                .findFirst()
                .map(Text::getContent);
    }
}
