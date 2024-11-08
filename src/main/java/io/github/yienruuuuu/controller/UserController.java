package io.github.yienruuuuu.controller;

import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.service.business.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Tag(name = "User controller")
@RestController
@RequestMapping("admin/user/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "查詢全部使用者資訊")
    @GetMapping(value = "findAll")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok(userService.findAll());
    }
}
