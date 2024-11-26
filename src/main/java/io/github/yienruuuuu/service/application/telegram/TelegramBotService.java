package io.github.yienruuuuu.service.application.telegram;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.repository.BotRepository;
import io.github.yienruuuuu.service.application.telegram.channel_manage.ChannelManageBotConsumer;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.FileManageBotConsumer;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotConsumer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * @author Eric.Lee Date: 2024/10/17
 */
@Slf4j
@Service
public class TelegramBotService {
    //TG長輪巡物件
    private TelegramBotsLongPollingApplication botsApplication;
    //Repo
    private final BotRepository botRepository;
    //Telegram Client傳訊物件
    private final TelegramBotClient telegramBotClient;
    //使用 ENUM Map 來管理 BotType 與 BotConsumer 的映射
    private final EnumMap<BotType, LongPollingUpdateConsumer> botConsumers = new EnumMap<>(BotType.class);

    public TelegramBotService(MainBotConsumer mainBotConsumer,
                              FileManageBotConsumer fileManageBotConsumer,
                              ChannelManageBotConsumer channelManageBotConsumer,
                              BotRepository botRepository,
                              TelegramBotClient telegramBotClient) {
        this.botRepository = botRepository;
        this.telegramBotClient = telegramBotClient;
        // 初始化 BotConsumer Map
        botConsumers.put(BotType.MAIN, mainBotConsumer);
        botConsumers.put(BotType.FILE_MANAGE, fileManageBotConsumer);
        botConsumers.put(BotType.CHANNEL, channelManageBotConsumer);
    }

    @PostConstruct
    public void registerBots() {
        // 初始化 TG 長輪詢應用
        botsApplication = new TelegramBotsLongPollingApplication();

        // 遍歷 botConsumers，對每個 BotType 進行註冊
        botConsumers.forEach((botType, botConsumer) -> {
            Bot botEntity = botRepository.findBotByType(botType);
            if (botEntity == null || StringUtils.isBlank(botEntity.getBotToken())) {
                log.warn("未找到對應的 BOT 實體或 BOT TOKEN ， BotType: {}", botType);
                return;
            }
            try {
                botsApplication.registerBot(botEntity.getBotToken(), botConsumer);
                log.info("機器人 {} 註冊完成", botType);
            } catch (TelegramApiException e) {
                log.error("機器人 {} 註冊發生錯誤 , 錯誤訊息 : ", botType, e);
            }
            // 更新資料庫中的 Bot 資料設定
            updateBotData(botEntity);
            // 更新BOT接收Updated資料(allowed_updates)
            updateBotUpdates(botEntity);
        });
    }

    /**
     * 更新機器人資料(userName)
     */
    private void updateBotData(Bot botEntity) {
        User botData = telegramBotClient.send(GetMe.builder().build(), botEntity);
        botEntity.setBotTelegramUserName(botData.getUserName());
        botRepository.save(botEntity);
    }

    /**
     * 更新機器人接收Updated資料(allow_updates)
     */
    private void updateBotUpdates(Bot botEntity) {
        if (!botEntity.getType().equals(BotType.CHANNEL)) return;
        List<String> allowedUpdates = Arrays.asList("update_id", "message", "callback_query", "channel_post", "chat_member");
        telegramBotClient.send(
                GetUpdates.builder()
                        .allowedUpdates(allowedUpdates)
                        .build(), botEntity);

    }

    @PreDestroy
    public void shutdownBot() throws Exception {
        if (botsApplication != null) {
            log.info("關閉機器人並釋放資源");
            botsApplication.close();
        }
    }
}
