package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.EditResourceRequest;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
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
import io.github.yienruuuuu.utils.TemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class EditResourceCommand extends DataManageBaseCommand implements DataManageCommand {


    public EditResourceCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        //JSON 轉換為DTO
        EditResourceRequest request = parseJsonToEditResourceRequest(update);
        if (request == null) {
            sendEditResourceTemplate(new Resource(), chatId, fileManageBot);
            return;
        }
        Resource resource = resourceService.findByUniqueId(request.getUniqueId()).orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUNT));
        resource.setRarityType(RarityType.valueOf(request.getRarityType()));
        resource.setTags(request.getTags());
        resource.setTexts(super.convertToTextEntities(request.getTexts()));
        Resource res = resourceService.save(resource);
        telegramBotClient.send(
                SendMessage.builder().chatId(chatId).text("已儲存, id = " + res.getId()).build(),
                fileManageBot
        );
    }

    @Override
    public String getCommandName() {
        return "/edit_resource";
    }

    /**
     * 傳送完善resource的json模板
     */
    private void sendEditResourceTemplate(Resource newResource, String chatId, Bot fileBotEntity) {
        String template = TemplateGenerator.generateResourceTemplate(newResource, languageService.findAllLanguages());
        SendMessage askForCompleteResourceSetting = SendMessage.builder()
                .chatId(chatId)
                .text("/edit_resource " + template)
                .build();
        telegramBotClient.send(askForCompleteResourceSetting, fileBotEntity);
    }

    /**
     * 將 text.JSON 字串轉換為 EditResourceRequest DTO
     */
    private EditResourceRequest parseJsonToEditResourceRequest(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring("/edit_resource".length()).trim();
        // 檢查 JSON 是否為空
        if (jsonString.isEmpty()) {
            return null;
        }
        return JsonUtils.parseJsonToTargetDto(jsonString, EditResourceRequest.class);
    }

}