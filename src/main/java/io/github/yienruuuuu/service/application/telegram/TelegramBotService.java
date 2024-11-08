package io.github.yienruuuuu.service.application.telegram;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotConsumer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Eric.Lee Date: 2024/10/17
 */
@Slf4j
@Service
public class TelegramBotService {
    //TG長輪巡物件
    private TelegramBotsLongPollingApplication botsApplication;
    //BOT
    private final MainBotConsumer mainBotConsumer;
    //Repo
    private final BotRepository botRepository;

    public TelegramBotService(MainBotConsumer mainBotConsumer, BotRepository botRepository) {
        this.mainBotConsumer = mainBotConsumer;
        this.botRepository = botRepository;
    }

    @PostConstruct
    public void registerBot() {
        Bot mainBotEntity = botRepository.findBotByType(BotType.MAIN);
        try {
            botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(mainBotEntity.getBotToken(), this.mainBotConsumer);
        } catch (TelegramApiException e) {
            log.error("機器人註冊發生錯誤 , 錯誤訊息 : ", e);
        }
    }

    @PreDestroy
    public void shutdownBot() throws Exception {
        if (botsApplication != null) {
            log.info("關閉機器人並釋放資源");
            botsApplication.close();
        }
    }
}
