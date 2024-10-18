package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.PartnerDto;
import com.wowo.wowo.data.dto.PartnerRequest;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.mapper.PartnerMapper;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
import com.wowo.wowo.services.JwtService;
import com.wowo.wowo.util.ApiKeyUtil;
import com.wowo.wowo.util.ObjectUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(value = "v1/partner", produces = "application/json; charset=UTF-8")
@Tag(name = "Partner", description = "Đối tác")
public class PartnerController {

    private final PartnerRepository partnerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PartnerMapper partnerMapperImpl;

    @PostMapping("/register")
    public ResponseEntity<?> createPartner(@RequestBody PartnerRequest newPartner) {
        if (newPartner == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }



        final Partner partnerEntity = partnerMapperImpl.toEntity(newPartner);

        partnerEntity.setApiKey(ApiKeyUtil.generateApiKey());

        try {
            Partner savedEntity = partnerRepository.save(partnerEntity);
            String token = JwtService.generateToken(partnerEntity);
            return ResponseEntity.ok()
                    .header("Set-Cookie",
                            "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=9999999;")
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
    public ResponseEntity<?> getPartner(Authentication authentication,
            @PathVariable String service) {
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
