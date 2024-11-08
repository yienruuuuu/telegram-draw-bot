package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class StartCommand extends BaseCommand implements MainBotCommand {
    private final TelegramBotClient botClient;

    public StartCommand(UserService userService, TelegramBotClient botClient) {
        super(userService);
        this.botClient = botClient;
    }


    @Override
    public void execute(Update update) {
        String messageText = update.getMessage().getText();
        String[] messageParts = messageText.trim().split("\\s+");
        String inviteCode = messageParts.length > 1 ? messageParts[1] : null;

        // 處理邀請者加分
        if (inviteCode != null) {
            inviterAddPoint(inviteCode);
        }
    }

    @Override
    public String getCommandName() {
        return "/start";
    }


    // 給邀請者加分
    private void inviterAddPoint(String inviteCode) {
        // 驗證邀請碼的有效性
        if (!isValidInviteCode(inviteCode)) {
            return;
        }
        // 獲取邀請者的信息
        User inviter = userService.findByTelegramUserId(inviteCode);
        if (inviter == null) {
            return;
        }
        // 給邀請者免費積分
        inviter.setFreePoints(inviter.getFreePoints() + 10);
        userService.save(inviter);
        log.warn("新使用者透過邀請連結來訪，邀請者Id={}, 邀請者名稱={}", inviter.getId(), inviter.getFirstName());
    }

    // 驗證邀請碼的格式
    private boolean isValidInviteCode(String inviteCode) {
        return inviteCode.matches("init\\w+");
    }
}