package io.github.yienruuuuu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Tag(name = "Administrator controller")
@RestController
@RequestMapping("admin")
public class AdminController {

    @Operation(summary = "Change restaurant visible for POS")
    @PostMapping(value = "telegram/bot/register")
    public void registerBot() {

    }
}
