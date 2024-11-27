package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.RoleDTO;
import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final YearAnalysisService yearAnalysisService;

    @NotNull
    public User getUserByIdOrUsernameOrEmail(String id, String username, String email) {
        return userRepository.findFirstByIdOrEmailOrUsername(id, username, email)
                .orElseThrow(
                        () -> new NotFoundException("Người dùng không tồn tại"));
    }

    public User createUser(SSOData ssoData) {
        var user = userRepository.findById(ssoData.getId());
        if (user.isPresent()) return user.get();

        var newUser = new User();
        newUser.setId(ssoData.getId());
        newUser.setEmail(ssoData.getEmail());
        newUser.setUsername(ssoData.getEmail());
        newUser.setFirstName(ssoData.getFirstName());
        newUser.setLastName(ssoData.getLastName());

        try {
            final UserWallet wallet = new UserWallet();
            newUser.setWallet(wallet);
            wallet.setUser(newUser);
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo người dùng");
        }
    }

    //lấy tất cả người dùng
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Phân tích dữ liệu người dùng trong vòng {@code month} tháng
     *
     * @param userId id của người dùng
     *
     * @return dữ liệu phân tích
     */
    public MonthAnalysis analysis(String userId) {
        final YearAnalysis analysis = yearAnalysisService.getAnalysis(userId, LocalDate.now()
                .getYear());
        return analysis.getCurrentMonthAnalysis()
                .getCurrentMonthAnalysisFromFistToNow();
    }

    public Collection<Application> getApplications(String userId) {
        final User user = getUserByIdOrElseThrow(userId);
        return user.getApplications();
    }

    public User getUserByIdOrElseThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Người dùng không tồn tại"));
    }

    //phân quyền
    public List<UserDTO> getUsersByRoleId(int roleId) {
        List<User> users = userRepository.findByRoleId(roleId);
        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setIsVerified(user.getIsVerified());

            // Map Role to RoleDTO
            if (user.getRole() != null) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setId(user.getRole().getId().toString());
                roleDTO.setUsername(user.getRole().getName()); // Ensure you're using the correct field to set role name
                roleDTO.setIsActive(user.getIsActive());
                roleDTO.setIsVerified(user.getIsVerified());
                userDTO.setRole(roleDTO);
            }

            return userDTO;
        }).collect(Collectors.toList());
    }

    public void checkAdminAccess(User user) {
        if (user.getRole() == null || !"Admin".equals(user.getRole().getName())) {
            throw new AccessDeniedException("Bạn không có quyền truy cập vào trang này.");
        }
    }

    public void checkManagerAccess(User user) {
        if (user.getRole() != null && ("Admin".equals(user.getRole().getName()) || "Manager".equals(user.getRole().getName()))) {
            return;
        }
        throw new AccessDeniedException("Bạn không có quyền truy cập vào trang này.");
    }

    public void assignRoleToUser(User user, Role role) {
        user.setRole(role);
        userRepository.save(user);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setIsVerified(user.getIsVerified());

            // Map Role to RoleDTO
            if (user.getRole() != null) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setId(user.getRole().getId().toString());
                roleDTO.setUsername(user.getRole().getName());
                roleDTO.setIsActive(user.getIsActive());
                roleDTO.setIsVerified(user.getIsVerified());
                userDTO.setRole(roleDTO);
            }

            return userDTO;
        });
    }
}
