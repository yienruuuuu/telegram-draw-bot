package io.github.yienruuuuu.service.application.telegram.channel_manage;

import io.github.yienruuuuu.bean.entity.PointLog;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.PointType;
import io.github.yienruuuuu.service.business.PointLogService;
import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/25
 */
@Component
public class NewMemberHandler {
    //常數
    private static final Integer NEW_MEMBER_POINT = 100;
    //service
    private final UserService userService;

    public NewMemberHandler(UserService userService) {
        this.userService = userService;
    }

    public void handleNewMember(Update update) {
        var chatMember = update.getChatMember();
        var userId = String.valueOf(chatMember.getFrom().getId());
        User user = userService.findByTelegramUserId(userId);
        //user未曾註冊
        if (user == null) return;
        //曾經加入過channel
        if (user.isHasAddInChannel()) return;
        if (chatMember.getNewChatMember().getStatus().equals("member") &&
                chatMember.getOldChatMember().getStatus().equals("left") &&
                !user.isHasAddInChannel()) {
            userService.addPointAndSavePointLog(user, NEW_MEMBER_POINT, PointType.FREE, "channel new member", null, null);
        }
    }
}
