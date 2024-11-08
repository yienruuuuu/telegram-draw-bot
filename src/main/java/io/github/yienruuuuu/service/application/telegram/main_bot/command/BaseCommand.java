package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.service.business.UserService;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class BaseCommand {
    protected final UserService userService;

    public BaseCommand(UserService userService) {
        this.userService = userService;
    }
}
