package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
@Slf4j
public class DataManageBaseCommand {
    protected final UserService userService;
    protected final LanguageService languageService;
    protected final TelegramBotClient telegramBotClient;
    protected final AnnouncementService announcementService;
    protected final ResourceService resourceService;


    public DataManageBaseCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
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

    /**
     * 將 DTO 內的 texts 轉換為 Text 實體
     */
    protected List<Text> convertToTextEntities(List<Map<String, String>> dtoTexts) {
        List<Text> texts = new ArrayList<>();
        for (Map<String, String> dtoText : dtoTexts) {
            // 假設只有一個 key-value 對（例如 {"zh-hant": "內容"}）
            String languageCode = dtoText.keySet().iterator().next();
            String content = dtoText.get(languageCode);

            // 查詢 Language 實體
            Language language = languageService.findLanguageByCodeOrDefault(languageCode);

            // 創建 Text 實體
            Text text = new Text();
            text.setLanguage(language);
            text.setContent(content);
            texts.add(text);
        }
        return texts;
    }


    /**
     * 提取分頁號碼，默認返回 1
     */
    protected int extractPageNumber(String commandText) {
        try {
            String[] parts = commandText.split(" ");
            return parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
        } catch (NumberFormatException e) {
            log.warn("Invalid page number provided in command: {}", commandText, e);
            return 1;
        }
    }
}
