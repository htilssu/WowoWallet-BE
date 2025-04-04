package com.wowo.wowo.designPattern.Strategy;

import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/user", produces = "application/json; charset=UTF-8")
@Tag(name = "User", description = "Người dùng")
@IsAuthenticated
public class UserController2 {

    private final UserService2 userService;
    private final UserMapper userMapper;

    @GetMapping("/search/{keyword}")
    public Page<UserDTO> searchUser(
            @PathVariable String keyword,
            @RequestParam(name = "type", defaultValue = "username") String searchType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<User> users = userService.getUserByKeyword(keyword, searchType);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        return userPage.map(userMapper::toDto);
    }
}