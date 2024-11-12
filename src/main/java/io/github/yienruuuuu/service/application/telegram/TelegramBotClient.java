package io.github.yienruuuuu.service.application.telegram;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.repository.BotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPaidMedia;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric.Lee
 * Date: 2024/10/18
 */
@Component
@Slf4j
public class TelegramBotClient {
    private final BotRepository botRepository;
    private final Map<Integer, TelegramClient> clientCache = new ConcurrentHashMap<>();

    public TelegramBotClient(BotRepository botRepository) {
        this.botRepository = botRepository;
    }


    /**
     * 通用的 send 方法，支援所有 BotApiMethod 的子類別
     */
    public <T extends Serializable, Method extends BotApiMethod<T>> T send(Method method, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(method);
        } catch (TelegramApiException e) {
            handleException(e, method.getMethod());
            return null;
        }
    }

    /**
     * send GIF
     */
    public Message send(SendAnimation animation, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(animation);
        } catch (TelegramApiException e) {
            handleException(e, "傳送GIF");
        }
        return null;
    }

    /**
     * send photo
     */
    public Message send(SendPhoto photo, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(photo);
        } catch (TelegramApiException e) {
            handleException(e, "傳送圖片");
        }
        return null;
    }

    /**
     * 取得文件的通用方法
     */
    public File getFile(GetFile getFile, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(getFile);
        } catch (TelegramApiException e) {
            handleException(e, "getFile");
            return null;
        }
    }

    /**
     * send Video
     */
    public Message send(SendVideo video, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(video);
        } catch (TelegramApiException e) {
            handleException(e, "傳送Video");
        }
        return null;
    }

    /**
     * 下載文件的通用方法
     */
    public java.io.File downloadFile(File file, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.downloadFile(file);
        } catch (TelegramApiException e) {
            handleException(e, "getFile");
            return null;
        }
    }

    /**
     * 下載文件的通用方法
     */
    public java.io.File downloadFileByFilePath(String filePath, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.downloadFile(filePath);
        } catch (TelegramApiException e) {
            handleException(e, "downloadFileByFilePath");
            return null;
        }
    }


    /**
     * 付費圖片傳送
     */
    public List<Message> send(SendPaidMedia sendPaidMedia, Bot bot) {
        TelegramClient telegramClient = getOrCreateTelegramClient(bot);
        try {
            return telegramClient.execute(sendPaidMedia);
        } catch (TelegramApiException e) {
            handleException(e, "send paid media");
            return null;
        }
    }

    /**
     * 從緩存中獲取 TelegramClient，若不存在則創建並緩存
     */
    private TelegramClient getOrCreateTelegramClient(Bot bot) {
        return clientCache.computeIfAbsent(bot.getId(), this::createTelegramClient);
    }

    /**
     * 根據 botId 創建 TelegramClient
     */
    private TelegramClient createTelegramClient(Integer botId) {
        Bot bot = botRepository.findBotById(botId);
        log.info("創建 TelegramClient for botId: {}", botId);
        return new OkHttpTelegramClient(bot.getBotToken());
    }

    /**
     * 將錯誤處理統一管理
     */
    private void handleException(TelegramApiException e, String action) {
        log.error("{} 操作失敗: ", action, e);
    }
}
