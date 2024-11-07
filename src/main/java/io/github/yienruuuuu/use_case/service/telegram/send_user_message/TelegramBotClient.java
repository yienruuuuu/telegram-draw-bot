package io.github.yienruuuuu.use_case.service.telegram.send_user_message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPaidMedia;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/10/18
 */
@Component
@Slf4j
public class TelegramBotClient {

    /**
     * 通用的 send 方法，支援所有 BotApiMethod 的子類別
     */
    public <T extends Serializable, Method extends BotApiMethod<T>> T send(Method method, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
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
    public Message send(SendAnimation animation, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
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
    public Message send(SendPhoto photo, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
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
    public File getFile(GetFile getFile, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
        try {
            return telegramClient.execute(getFile);
        } catch (TelegramApiException e) {
            handleException(e, "getFile");
            return null;
        }
    }

    /**
     * 下載文件的通用方法
     */
    public java.io.File downloadFile(File file, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
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
    public java.io.File downloadFileByFilePath(String filePath, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
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
    public List<Message> send(SendPaidMedia sendPaidMedia, String token) {
        TelegramClient telegramClient = createTelegramClient(token);
        try {
            return telegramClient.execute(sendPaidMedia);
        } catch (TelegramApiException e) {
            handleException(e, "send paid media");
            return null;
        }
    }

    /**
     * 根據 botId 創建 TelegramClient
     */
    private TelegramClient createTelegramClient(String botToken) {
        return new OkHttpTelegramClient(botToken);
    }

    /**
     * 將錯誤處理統一管理
     */
    private void handleException(TelegramApiException e, String action) {
        log.error("{} 操作失敗: ", action, e);
    }
}
