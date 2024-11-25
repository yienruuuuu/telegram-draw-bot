package io.github.yienruuuuu.service.application.telegram.channel_manage.command;

import io.github.yienruuuuu.bean.dto.AddCheatCodeRequest;
import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.RoleType;
import io.github.yienruuuuu.config.AppConfig;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.channel_manage.ChannelManageBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.CheatCodeService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import io.github.yienruuuuu.utils.JsonUtils;
import io.github.yienruuuuu.utils.TemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class AddCheatCode implements ChannelManageBotCommand {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault());
    private final AppConfig appConfig;
    private final UserService userService;
    private final TelegramBotClient telegramBotClient;
    private final CheatCodeService cheatCodeService;
    private final AnnouncementService announcementService;

    public AddCheatCode(AppConfig appConfig, UserService userService, TelegramBotClient telegramBotClient, CheatCodeService cheatCodeService, AnnouncementService announcementService) {
        this.appConfig = appConfig;
        this.userService = userService;
        this.telegramBotClient = telegramBotClient;
        this.cheatCodeService = cheatCodeService;
        this.announcementService = announcementService;
    }


    @Override
    public void execute(Update update, Bot channelManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, channelManageBot);
        User user = userService.findByTelegramUserId(userId);
        AddCheatCodeRequest request = parseJsonToDto(update);
        if (request == null) {
            sendEditResourceTemplate(chatId, channelManageBot);
            return;
        }
        // 新增 CheatCode
        String startAtIso = request.getValidFrom() + "T08:00:00+08:00";
        String endAtIso = request.getValidTo() + "T08:00:00+08:00";
        CheatCode newCheatCode = CheatCode.builder()
                .code(request.getCode())
                .validFrom(Instant.parse(startAtIso))
                .validTo(Instant.parse(endAtIso))
                .maxUsage(request.getMaxUsage())
                .pointAmount(request.getPointAmount())
                .isActive(request.isActive())
                .build();
        CheatCode cheatCode = cheatCodeService.save(newCheatCode);
        //傳送公告
        String announcement = getAnnouncementMessage(AnnouncementType.CHEAT_CODE_ANNOUNCEMENT, user.getLanguage())
                .orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
        String validPeriod = DATE_FORMATTER.format(cheatCode.getValidFrom()) + " - " + DATE_FORMATTER.format(cheatCode.getValidTo());
        String message = announcement
                .replace("{POINT_AMOUNT}", cheatCode.getPointAmount().toString())
                .replace("{VALIDITY_PERIOD}", validPeriod);
        telegramBotClient.send(
                SendMessage.builder()
                        .text(message)
                        .chatId(appConfig.getBotPublicChannelId())
                        .protectContent(true)
                        .build(), channelManageBot);
        telegramBotClient.send(
                SendMessage.builder()
                        .text("`" + escapeMarkdownV2(cheatCode.getCode()) + "`")
                        .chatId(appConfig.getBotPublicChannelId())
                        .parseMode("MarkdownV2")
                        .build(), channelManageBot);
    }

    @Override
    public String getCommandName() {
        return "/add_cheat_code";
    }

    /**
     * 檢查使用者權限
     */
    private void checkUsersPermission(String userId, String chatId, Bot mainBotEntity) {
        User user = userService.findByTelegramUserId(userId);
        if (user != null && user.getRole().hasPermission(RoleType.MANAGER)) {
            return;
        }
        SendMessage message = SendMessage.builder().chatId(chatId).text(SysCode.PERMISSION_DENIED_ERROR.getMessage()).build();
        telegramBotClient.send(message, mainBotEntity);
        throw new ApiException(SysCode.PERMISSION_DENIED_ERROR);
    }

    /**
     * 傳送json模板
     */
    private void sendEditResourceTemplate(String chatId, Bot channelManageBotEntity) {
        String template = getCommandName() + " " + TemplateGenerator.generateCheatCodeTemplate(null);
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("`" + template + "`")
                        .parseMode("MarkdownV2")
                        .build(), channelManageBotEntity);
    }

    /**
     * 將 text.JSON 字串轉換為 EditResourceRequest DTO
     */
    private AddCheatCodeRequest parseJsonToDto(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring(getCommandName().length()).trim();
        // 檢查 JSON 是否為空
        if (jsonString.isEmpty()) {
            return null;
        }
        return JsonUtils.parseJsonToTargetDto(jsonString, AddCheatCodeRequest.class);
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

    // 特殊字符轉義方法
    private String escapeMarkdownV2(String text) {
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }
}