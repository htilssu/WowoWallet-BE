package com.wowo.wowo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wowo.wowo.data.dto.VerifyDTO;
import com.wowo.wowo.exception.UserNotFoundException;
import com.wowo.wowo.model.User;
import com.wowo.wowo.model.Verify;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.repository.VerifyRepository;

@Service
public class VerifyService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerifyRepository verifyRepository;

    public String validateVerifyData(VerifyDTO verifyDTO) {
        if (verifyDTO.getNumberCard() == null || String.valueOf(verifyDTO.getNumberCard()).isEmpty()) {
            return "Số thẻ không được rỗng.";
        }
        if (String.valueOf(verifyDTO.getNumberCard()).length() > 12) {
            return "Số thẻ không được quá 12 số.";
        }
    
        if (verifyDTO.getType() == null || verifyDTO.getType().trim().isEmpty()) {
            return "Loại xác thực không được rỗng.";
        }
    
        if (verifyDTO.getOpenDay() == null || verifyDTO.getCloseDay() == null) {
            return "Ngày mở và ngày đóng không được rỗng.";
        }
        if (verifyDTO.getCloseDay().isBefore(verifyDTO.getOpenDay())) {
            return "Ngày đóng không được bé hơn ngày mở.";
        }
    
        return null; 
    }
    

    public void createVerify(VerifyDTO verifyDTO) {
        String customerId = verifyDTO.getCustomer().getId();

        User user = userRepository.findById(customerId)
            .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));

        Verify verify = new Verify();
        verify.setCustomer(user);
        verify.setType(verifyDTO.getType());
        verify.setNumberCard(verifyDTO.getNumberCard());
        verify.setOpenDay(verifyDTO.getOpenDay());
        verify.setCloseDay(verifyDTO.getCloseDay());
        verify.setFontImage(verifyDTO.getFontImage());
        verify.setBehindImage(verifyDTO.getBehindImage());
        verify.setUserImage(verifyDTO.getUserImage());

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
