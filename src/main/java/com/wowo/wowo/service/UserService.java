package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.MonthAnalysis;
import com.wowo.wowo.model.User;
import com.wowo.wowo.model.YearAnalysis;
import com.wowo.wowo.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final YearAnalysisService yearAnalysisService;

    public User getUserByIdOrElseThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Người dùng không tồn tại"));
    }

    @NotNull
    public User getUserByIdOrUsernameOrEmail(String id, String username, String email) {
        return userRepository.findFirstByIdOrEmailOrUsername(id, username, email)
                .orElseThrow(
                        () -> new NotFoundException("Người dùng không tồn tại"));
    }

    public void createUser(SSOData ssoData) {

        var user = userRepository.findById(ssoData.getId());
        if (user.isPresent()) return;

        var newUser = new User();
        newUser.setId(ssoData.getId());
        newUser.setEmail(ssoData.getEmail());
        newUser.setUsername(ssoData.getUsername());
        newUser.setFirstName(ssoData.getFirstName());
        newUser.setLastName(ssoData.getLastName());

        try {
            userRepository.save(newUser);
            walletService.createWallet(newUser);
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
}
