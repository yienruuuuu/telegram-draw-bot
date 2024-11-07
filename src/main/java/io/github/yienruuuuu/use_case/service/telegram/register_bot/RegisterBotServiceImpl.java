package io.github.yienruuuuu.use_case.service.telegram.register_bot;

import io.github.yienruuuuu.use_case.service.telegram.DrawBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Eric.Lee
 * Date: 2024/11/7
 */
@Service("registerBotService")
@Slf4j
public class RegisterBotServiceImpl implements RegisterBotService {
    // TG長輪巡物件
    private TelegramBotsLongPollingApplication botsApplication;
    private final DrawBot drawBot;

    public RegisterBotServiceImpl(DrawBot drawBot) {
        this.drawBot = drawBot;
    }

    public void registerBot(String token) {
        try {
            botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(token, this.drawBot);
        } catch (TelegramApiException e) {
            log.error("機器人註冊發生錯誤 , 錯誤訊息 : ", e);
        }
    }
}
