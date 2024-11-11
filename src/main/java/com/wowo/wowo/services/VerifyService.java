package com.wowo.wowo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wowo.wowo.data.dto.VerifyDto;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Verify;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.VerifyRepository;

@Service
public class VerifyService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerifyRepository verifyRepository;

    public String validateVerifyData(VerifyDto verifyDto) {
        if (verifyDto.getNumberCard() == null || String.valueOf(verifyDto.getNumberCard()).isEmpty()) {
            return "Số thẻ không được rỗng.";
        }
        if (String.valueOf(verifyDto.getNumberCard()).length() > 12) {
            return "Số thẻ không được quá 12 số.";
        }
    
        if (verifyDto.getType() == null || verifyDto.getType().trim().isEmpty()) {
            return "Loại xác thực không được rỗng.";
        }
    
        if (verifyDto.getFontImage() == null || verifyDto.getFontImage().trim().isEmpty() ||
            verifyDto.getBehindImage() == null || verifyDto.getBehindImage().trim().isEmpty() ||
            verifyDto.getUserImage() == null || verifyDto.getUserImage().trim().isEmpty()) {
            return "Các trường ảnh không được rỗng.";
        }
    
        if (verifyDto.getOpenDay() == null || verifyDto.getCloseDay() == null) {
            return "Ngày mở và ngày đóng không được rỗng.";
        }
        if (verifyDto.getCloseDay().isBefore(verifyDto.getOpenDay())) {
            return "Ngày đóng không được bé hơn ngày mở.";
        }
    
        return null; 
    }
    

    public void createVerify(VerifyDto verifyDto) {
        String customerId = verifyDto.getCustomer().getId();

        User user = userRepository.findById(customerId)
            .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));

        Verify verify = new Verify();
        verify.setCustomer(user);
        verify.setType(verifyDto.getType());
        verify.setNumberCard(verifyDto.getNumberCard());
        verify.setOpenDay(verifyDto.getOpenDay());
        verify.setCloseDay(verifyDto.getCloseDay());
        verify.setFontImage(verifyDto.getFontImage());
        verify.setBehindImage(verifyDto.getBehindImage());
        verify.setUserImage(verifyDto.getUserImage());

        verifyRepository.save(verify);
    }

    public List<Verify> getUserVerify(String customerId) {
        User user = userRepository.findById(customerId)
            .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
        return verifyRepository.findByCustomer_Id(user.getId());
    }

    public Optional<Verify> getVerifyById(Long id) {
        return verifyRepository.findById(id);
    }

    public List<Verify> getAllVerify() {
        return verifyRepository.findAll();
    }

    public void deleteVerify(Long id) {
        verifyRepository.deleteById(id);
    }
    
}
