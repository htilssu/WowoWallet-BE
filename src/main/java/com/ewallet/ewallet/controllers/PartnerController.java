package com.ewallet.ewallet.controllers;

import com.ewallet.ewallet.dto.mapper.PartnerMapperImpl;
import com.ewallet.ewallet.data.dto.response.PartnerDto;
import com.ewallet.ewallet.data.dto.response.PartnerRequest;
import com.ewallet.ewallet.data.dto.response.ResponseMessage;
import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.repositories.PartnerRepository;
import com.ewallet.ewallet.util.ApiKeyUtil;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/partner", produces = "application/json; charset=UTF-8")
public class PartnerController {

    private final PartnerRepository partnerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PartnerMapperImpl partnerMapperImpl;

    @PostMapping("/register")
    public ResponseEntity<?> createPartner(@RequestBody PartnerRequest newPartner) {
        if (newPartner == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }

        if (partnerRepository.existsByEmail(newPartner.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Email đã tồn tại"));
        }

        final Partner partnerEntity = partnerMapperImpl.toEntity(newPartner);

        partnerEntity.setPassword(bCryptPasswordEncoder.encode(partnerEntity.getPassword()));
        partnerEntity.setApiKey(ApiKeyUtil.generateApiKey());

        try {
            Partner savedEntity = partnerRepository.save(partnerEntity);
            String token = JwtUtil.generateToken(partnerEntity);
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=9999999;")
                    .body(ObjectUtil.mergeObjects(
                            ObjectUtil.wrapObject("partner", partnerMapperImpl.toDto(savedEntity)),
                            new ResponseMessage("Đăng ký thành công"),
                            ObjectUtil.wrapObject("token", token)));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Đăng ký thất bại"));
        }
    }

    @GetMapping("/{service}")
    public ResponseEntity<?> getPartner(Authentication authentication, @PathVariable String service) {
        Partner partner = (Partner) authentication.getPrincipal();
        return ResponseEntity.ok(partnerMapperImpl.toDto(partner));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPartner() {
        final List<Partner> partnerList = partnerRepository.findAll();
        List<PartnerDto> partnerDtoList = new ArrayList<>();
        for (Partner partner : partnerList) {
            partnerDtoList.add(partnerMapperImpl.toDto(partner));
        }
        return ResponseEntity.ok(partnerDtoList);
    }
}
