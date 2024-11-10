package com.wowo.wowo.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.annotations.authorized.IsAdmin;
import com.wowo.wowo.annotations.authorized.IsAuthenticated;
import com.wowo.wowo.data.dto.UserDto;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.WalletRepository;
import com.wowo.wowo.services.PartnerService;
import com.wowo.wowo.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/user", produces = "application/json; charset=UTF-8")
@Tag(name = "User", description = "Người dùng")
@IsAuthenticated
public class UserController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserMapper userMapperImpl;
    private final WalletMapper walletMapperImpl;
    private final PartnerService partnerService;
    private final UserService userService;

    @GetMapping()
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin")
    public Object getUser(Authentication authentication) {
        DecodedJWT decodedJWT = (DecodedJWT) authentication.getDetails();
        String role = decodedJWT.getClaim("role").asString();
        String id = authentication.getPrincipal().toString();
        if (role.contains("partner")) {
            return partnerService.getPartnerById(id).orElseThrow(
                    () -> new NotFoundException("Không tìm thấy thông tin"));
        }
        return userService.getUserById(id);
    }

    @GetMapping("wallet")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        Optional<Wallet> wallet = walletRepository.findByOwnerIdAndOwnerType(
                (String) authentication.getPrincipal(), WalletOwnerType.USER);
        if (wallet.isPresent()) {
            return ResponseEntity.ok(walletMapperImpl.toResponse(wallet.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @IsAdmin
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
                 useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin")
    @ApiResponse(responseCode = "400", description = "Id không hợp lệ")
    @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    public UserDto getUserById(@PathVariable String id) {
        if (id != null) {
            final Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                return userMapperImpl.toDto(byId.get());
            }
        }
        return null;
    }


    @GetMapping("/analysis")
    public MonthAnalysis analysis(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();

        return userService.analysis(userId);
    }
}
