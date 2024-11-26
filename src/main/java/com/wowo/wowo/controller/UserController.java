package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsAdmin;
import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.data.dto.ApplicationDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.dto.WalletDTO;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.repository.UserWalletRepository;
import com.wowo.wowo.service.UserService;
import com.wowo.wowo.service.WalletService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/user", produces = "application/json; charset=UTF-8")
@Tag(name = "User", description = "Người dùng")
@IsAuthenticated
public class UserController {

    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserMapper userMapperImpl;
    private final WalletMapper walletMapperImpl;
    private final UserService userService;
    private final ApplicationMapper applicationMapper;
    private final WalletService walletService;

    @GetMapping()
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin")
    public UserDTO getUser(Authentication authentication) {
//        TODO
        String id = authentication.getPrincipal()
                .toString();
        return userMapperImpl.toDto(userService.getUserByIdOrElseThrow(id));
    }

    @GetMapping("wallet")
    public WalletDTO getWallet(Authentication authentication) {
        final User user = userService.getUserByIdOrElseThrow(authentication.getPrincipal()
                .toString());

        return walletMapperImpl.toDto(user.getWallet());
    }

    @GetMapping("/wallet/{userId}")
    //@IsAdmin
    @ApiResponse(responseCode = "200", description = "Lấy thông tin ví của người dùng thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy ví của người dùng")
    public WalletDTO getWalletByUserId(@PathVariable String userId) {

        final UserWallet userWallet = walletService.getUserWallet(userId);

        return walletMapperImpl.toDto(userWallet);
    }

    @GetMapping("/{id}")
    @IsAdmin
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin")
    @ApiResponse(responseCode = "400", description = "Id không hợp lệ")
    @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    public UserDTO getUserById(@PathVariable String id) {
        return userMapperImpl.toDto(userService.getUserByIdOrElseThrow(id));
    }

    @GetMapping("/all")
    //@IsAdmin
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin")
    @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    public Page<UserDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.getAllUsers(pageable)
                .map(userMapperImpl::toDto);
    }

    @GetMapping("/analysis")
    public MonthAnalysis analysis(Authentication authentication) {
        String userId = authentication.getPrincipal()
                .toString();

        return userService.analysis(userId);
    }

    @GetMapping("/application")
    public @NotNull List<ApplicationDTO> getApplications(Authentication authentication) {
        final Collection<Application> applications = userService.getApplications(
                authentication.getPrincipal()
                        .toString());

        return applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }

    @IsAdmin
    @GetMapping("/{id}/application")
    public @NotNull List<ApplicationDTO> getApplications(@PathVariable String id) {
        final Collection<Application> applications = userService.getApplications(
                id);

        return applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }
}
