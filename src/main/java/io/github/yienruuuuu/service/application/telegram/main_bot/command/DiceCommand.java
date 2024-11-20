package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.PointLog;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.PointLogService;
import io.github.yienruuuuu.service.business.UserService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 色子遊戲響應
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class DiceCommand extends BaseCommand implements MainBotCommand {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    private final PointLogService pointLogService;

    public DiceCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, PointLogService pointLogService) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.pointLogService = pointLogService;
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = String.valueOf(update.getMessage().getChatId());
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var diceValue = update.getMessage().getDice().getValue();
        //檢查使用者是否註冊
        super.checkUserIfExists(update, mainBotEntity);
        //查詢必要資訊
        User user = userService.findByTelegramUserId(userId);
        Language language = languageService.findLanguageByCodeOrDefault(user.getLanguage().getLanguageCode());
        //檢查當天是否有玩過色子
        if (checkHasPlayDiceToday(chatId, user, language, mainBotEntity)) return;
        //更新使用者的免費點數
        updateFreePoints(user, chatId, diceValue, language, mainBotEntity);
    }

    @Override
    public String getCommandName() {
        return "/dice";
    }

    /**
     * 檢查使用者是否今天已經玩過色子
     */
    private boolean checkHasPlayDiceToday(String chatId, User user, Language language, Bot mainBotEntity) {
        Instant lastPlayDiceTime = user.getLastPlayDiceTime();
        Instant startOfToday = LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toInstant();
        if (lastPlayDiceTime != null && lastPlayDiceTime.isAfter(startOfToday)) {
            String text = super.getAnnouncementMessage(AnnouncementType.HAS_PLAY_DICE_TODAY_MESSAGE, language).orElse("has play dice today");
            telegramBotClient.send(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build(), mainBotEntity);
            return true;
        }
        return false;
    }

    /**
     * 更新使用者的免費點數
     */
    private void updateFreePoints(User user, String chatId, Integer diceValue, Language language, Bot mainBotEntity) {
        Integer balanceBefore = user.getFreePoints();
        Integer balanceAfter = user.getFreePoints() + diceValue;
        PointLog pointLog = PointLog.builder()
                .user(user)
                .pointType(PointType.FREE)
                .amount(diceValue)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .reason(getCommandName())
                .build();
        user.setFreePoints(user.getFreePoints() + diceValue);
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
                .replace("{FREE_POINT}", balanceBefore + "+" + diceValue)
                .replace("{PAID_POINT}", String.valueOf(user.getPurchasedPoints()))
                .replace("{REGISTER_TIME}", DATE_FORMATTER.format(user.getCreatedAt()));
        //傳送
        telegramBotClient.send(SendMessage.builder().chatId(chatId).text(filledText).build(), mainBotEntity);
    }
}