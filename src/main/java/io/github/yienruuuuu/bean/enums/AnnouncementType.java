package io.github.yienruuuuu.bean.enums;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public enum AnnouncementType {
    //開始訊息
    START_MESSAGE,
    FILE_MANAGE_START_MESSAGE,
    //邀請訊息前後綴
    INVITE_MESSAGE_PREFIX,
    INVITE_MESSAGE_SUFFIX,
    //尚未註冊
    NOT_REGISTERED,
    //用戶狀態查詢
    USER_STATUS_QUERY,
    //無可用卡池訊息
    NO_POOL_OPEN_MESSAGE,
    //抽卡訊息
    PICK_CARD,
    //GET_POINT公告訊息
    GET_POINT_ANNOUNCEMENT,
    //GAME 說明
    GAME_DESCRIPTION,
    //今日已玩過骰子說明
    HAS_PLAY_DICE_TODAY_MESSAGE,
    //使用規約回答同意
    TERM_MESSAGE,
    //積分不足提示
    POINT_NOT_ENOUGH_MESSAGE,
    //取得下載權限_按鍵
    GET_DOWNLOAD_PERMISSION_BUTTON,
    //付費公告
    PAYMENT_ANNOUNCEMENT,
    //作弊碼公告
    CHEAT_CODE_ANNOUNCEMENT,
    //再抽一次
    PICK_AGAIN,
    //猜測接近
    HINT_SUSPECT,
    //猜測失敗
    HINT_FAIL,
    OTHER
}
