package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 色子遊戲響應
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class CheatCodeCommand extends BaseCommand implements MainBotCommand {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    private final PointLogService pointLogService;
    private final CheatCodeService cheatCodeService;

    public CheatCodeCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, PointLogService pointLogService, CheatCodeService cheatCodeService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.pointLogService = pointLogService;
        this.cheatCodeService = cheatCodeService;
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var languageCode = update.getMessage().getFrom().getLanguageCode();
        var data = update.getMessage().getText();
        //檢查使用者是否註冊
        super.checkUserIfExists(userId, mainBotEntity, Long.parseLong(chatId), languageCode);
        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        Language language = languageService.findLanguageByCodeOrDefault(user.getLanguage().getLanguageCode());
        CheatCode cheatCode = cheatCodeService.findByCode(data)
                .orElseThrow(() -> new ApiException(SysCode.UNEXPECTED_ERROR));
        // 檢查時間是否有效
        Instant now = Instant.now();
        if (now.isBefore(cheatCode.getValidFrom()) || now.isAfter(cheatCode.getValidTo())) {
            throw new ApiException(SysCode.CHEAT_CODE_EXPIRED);
        }

        //更新使用者的免費點數
        updateFreePoints(user, chatId, cheatCode.getPointAmount(), language, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/cheat_code";
    }


    /**
     * 更新使用者的免費點數及傳送通知
     */
    private void updateFreePoints(User user, String chatId, Integer amount, Language language, Bot mainBotEntity) {
        Integer balanceBefore = user.getFreePoints();
        Integer balanceAfter = user.getFreePoints() + amount;
        PointLog pointLog = PointLog.builder()
                .user(user)
                .pointType(PointType.FREE)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .reason(getCommandName())
                .build();
        user.setFreePoints(user.getFreePoints() + amount);
        user.setLastPlayDiceTime(Instant.now());
        userService.save(user);
        pointLogService.save(pointLog);

        //模板查詢
        String userStatusTemplate = super.getAnnouncementMessage(AnnouncementType.USER_STATUS_QUERY, language)
                .orElseThrow(() -> new ApiException(SysCode.ANNOUNCE_UNEXPECTED_ERROR));
        //替換填充模板
        String filledText = userStatusTemplate
                .replace("{USER_ID}", user.getTelegramUserId())
                .replace("{USER_ROLE}", user.getRole().name())
                .replace("{FREE_POINT}", balanceBefore + "+" + amount)
                .replace("{PAID_POINT}", String.valueOf(user.getPurchasedPoints()))
                .replace("{REGISTER_TIME}", DATE_FORMATTER.format(user.getCreatedAt()));
        //傳送
        telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
    }
}