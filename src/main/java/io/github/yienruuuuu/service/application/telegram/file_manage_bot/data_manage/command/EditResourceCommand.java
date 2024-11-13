package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yienruuuuu.bean.dto.EditResourceRequest;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.entity.Text;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class EditResourceCommand extends DataManageBaseCommand implements DataManageCommand {

    public EditResourceCommand(ObjectMapper objectMapper, UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        super(objectMapper, userService, languageService, telegramBotClient, announcementService, resourceService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, mainBotEntity);
        //JSON 轉換為DTO
        EditResourceRequest request = parseJsonToEditResourceRequest(update);
        Resource resource = resourceService.findByUniqueId(request.getUniqueId()).orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUNT));
        resource.setRarityType(RarityType.valueOf(request.getRarityType()));
        resource.setTags(request.getTags());
        resource.setTexts(convertToTextEntities(request.getTexts()));
        resourceService.save(resource);
    }

    @Override
    public String getCommandName() {
        return "/edit_resource";
    }

    /**
     * 將 text.JSON 字串轉換為 EditResourceRequest DTO
     */
    private EditResourceRequest parseJsonToEditResourceRequest(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring("/edit_resource".length()).trim();
        return JsonUtils.parseJsonToTargetDto(jsonString, EditResourceRequest.class);
    }

    /**
     * 將 DTO 內的 texts 轉換為 Text 實體
     */
    private List<Text> convertToTextEntities(List<Map<String, String>> dtoTexts) {
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

}