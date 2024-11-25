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
    private final PointLogService pointLogService;

    public NewMemberHandler(UserService userService, PointLogService pointLogService) {
        this.userService = userService;
        this.pointLogService = pointLogService;
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
            Integer balanceBefore = user.getFreePoints();
            Integer balanceAfter = balanceBefore + NEW_MEMBER_POINT;
            user.setFreePoints(balanceAfter);
            user.setHasAddInChannel(true);
            userService.save(user);
            pointLogService.save(addPointLog(user, balanceBefore, balanceAfter));
        }
    }

    /**
     * 為新會員新增積分日誌。
     */
    private PointLog addPointLog(User user, Integer balanceBefore, Integer balanceAfter) {
        return PointLog.builder()
                .user(user)
                .balanceAfter(balanceAfter)
                .balanceBefore(balanceBefore)
                .reason("channel new member")
                .pointType(PointType.FREE)
                .amount(NEW_MEMBER_POINT)
                .build();
    }
}
