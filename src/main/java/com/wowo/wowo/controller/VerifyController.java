package com.wowo.wowo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wowo.wowo.data.dto.VerifyDto;
import com.wowo.wowo.exception.UserNotFoundException;
import com.wowo.wowo.model.User;
import com.wowo.wowo.model.Verify;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.service.VerifyService;

@RestController
@RequestMapping(value = "v1/verifications", produces = "application/json; charset=utf-8")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private UserRepository userRepository;
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createVerify(
        @RequestParam("fontImage") MultipartFile frontImage,
        @RequestParam("behindImage") MultipartFile behindImage,
        @RequestParam("userImage") MultipartFile userImage,
        @RequestParam("customerId") String customerId,
        @RequestParam("numberCard") Long numberCard,
        @RequestParam("openDay") String openDayStr,
        @RequestParam("type") String type,
        @RequestParam("closeDay") String closeDayStr) throws java.io.IOException {
    
        if (frontImage.isEmpty() || behindImage.isEmpty() || userImage.isEmpty()) {
            return ResponseEntity.badRequest().body("Tệp hình ảnh không được để trống.");
        }
    
        LocalDateTime openDay = LocalDateTime.parse(openDayStr); 
        LocalDateTime closeDay = LocalDateTime.parse(closeDayStr);


        String frontImagePath = saveFile(frontImage);
        String behindImagePath = saveFile(behindImage);
        String userImagePath = saveFile(userImage);

        VerifyDto verifyDto = new VerifyDto();
        verifyDto.setCustomer(new VerifyDto.Customer(customerId)); 
        verifyDto.setFontImage(frontImagePath);
        verifyDto.setBehindImage(behindImagePath);
        verifyDto.setUserImage(userImagePath);
        verifyDto.setNumberCard(numberCard); 
        verifyDto.setOpenDay(openDay);        
        verifyDto.setCloseDay(closeDay);      
        verifyDto.setType(type);
    
        String validationError = verifyService.validateVerifyData(verifyDto);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }
    
        try {
            if (customerId == null || customerId.isEmpty()) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin khách hàng.");
            }
    
            User user = userRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
    
            if (user.getIsVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Người dùng đã được chứng thực, không thể gửi yêu cầu chứng thực.");
            }
    
            List<Verify> existingVerifications = verifyService.getUserVerify(customerId);
            if (!existingVerifications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Bạn đã gửi yêu cầu chứng thực rồi vui lòng đợi.");
            }
    
            verifyService.createVerify(verifyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đã gửi yêu cầu chứng thực");
    
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadsDir = Paths.get("uploads");  
        if (!Files.exists(uploadsDir)) {
            Files.createDirectories(uploadsDir);
        }
        String fileName = file.getOriginalFilename();
        Path path = uploadsDir.resolve(fileName);
        Files.write(path, file.getBytes());
        
        return path.toString();  
    }
    
    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<Verify>> getUserVerifications(@PathVariable String customerId) {
        try {
            List<Verify> verifications = verifyService.getUserVerify(customerId);
            return ResponseEntity.ok(verifications);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVerificationById(@PathVariable Long id) {
        Optional<Verify> verify = verifyService.getVerifyById(id);
        if (verify.isPresent()) {
            return ResponseEntity.ok(verify.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Không tìm thấy yêu cầu xác thực với ID: " + id);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VerifyDto>> getAllVerifications() {
        List<VerifyDto> verificationDtos = verifyService.getAllVerify().stream()
            .map(verify -> {
                VerifyDto dto = new VerifyDto();
                dto.setId(verify.getId());
                VerifyDto.Customer customerDto = new VerifyDto.Customer(
                    verify.getCustomer() != null ? verify.getCustomer().getId() : null
                );
                dto.setCustomer(customerDto);

                dto.setType(verify.getType());
                dto.setNumberCard(verify.getNumberCard());
                dto.setOpenDay(verify.getOpenDay());
                dto.setCloseDay(verify.getCloseDay());
                dto.setFontImage(verify.getFontImage());
                dto.setBehindImage(verify.getBehindImage());
                dto.setUserImage(verify.getUserImage());
                return dto;
            })
            .toList();

        return ResponseEntity.ok(verificationDtos);
    }

    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> approveVerification(@PathVariable String userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setIsVerified(true);  
        userRepository.save(user); 
            return ResponseEntity.ok("Xác thực người dùng thành công.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVerify(@PathVariable Long id) {
    Optional<Verify> verify = verifyService.getVerifyById(id);
    if (verify.isPresent()) {
        verifyService.deleteVerify(id);

        String customerId = verify.get().getCustomer().getId();
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
        
        user.setIsVerified(false);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Đã xóa yêu cầu chứng thực.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy yêu cầu chứng thực.");
    }
}

    
}

