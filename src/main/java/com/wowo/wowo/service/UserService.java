package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.data.dto.UpdateUserDTO;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

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
        if (user.isPresent())
            return user.get();

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

    // lấy tất cả người dùng
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

    /**
     * Cập nhật thông tin người dùng
     *
     * @param userId        ID của người dùng cần cập nhật
     * @param updateUserDTO Thông tin cần cập nhật
     * @return Người dùng đã được cập nhật
     */
    public User updateUser(String userId, UpdateUserDTO updateUserDTO) {
        User user = getUserByIdOrElseThrow(userId);

        if (updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }

        if (updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }

        if (updateUserDTO.getJob() != null) {
            user.setJob(updateUserDTO.getJob());
        }

        if (updateUserDTO.getAvatar() != null) {
            user.setAvatar(updateUserDTO.getAvatar());
        }

        if (updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }

        return userRepository.save(user);
    }

    /**
     * Cập nhật trạng thái xác thực email của người dùng
     *
     * @param userId ID của người dùng cần cập nhật
     * @return Người dùng đã được cập nhật trạng thái xác thực
     */
    public User verifyUserEmail(String userId) {
        User user = getUserByIdOrElseThrow(userId);
        user.setIsVerified(true);
        return userRepository.save(user);
    }
}
