package com.uandmy.back.user.controller;
import com.uandmy.back.common.base.BaseController;
import com.uandmy.back.user.dto.UserDto;
import com.uandmy.back.user.entity.User;
import com.uandmy.back.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController<UserDto, User, Long> {

    private final UserService userService;

    protected UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

}
