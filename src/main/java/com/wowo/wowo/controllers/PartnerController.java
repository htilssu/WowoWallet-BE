package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsAdmin;
import com.wowo.wowo.data.dto.PartnerDto;
import com.wowo.wowo.data.dto.PartnerRequest;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.mapper.PartnerMapper;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
import com.wowo.wowo.services.JwtService;
import com.wowo.wowo.services.PartnerService;
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
    private final PartnerService partnerService;

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

    //@IsAdmin
    @PostMapping("/approve/{partnerId}")
    public ResponseEntity<String> approvePartner(@PathVariable String partnerId) {
        try {
            partnerService.approvePartner(partnerId);
            return ResponseEntity.ok("Đối tác đã được Phê Duyệt thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //@IsAdmin
    @PostMapping("/suspend/{partnerId}")
    public ResponseEntity<String> suspendPartner(@PathVariable String partnerId) {
        try {
            partnerService.suspendPartner(partnerId);
            return ResponseEntity.ok("Đối tác đã bị Tạm Ngưng thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //@IsAdmin
    @PostMapping("/restore/{partnerId}")
    public ResponseEntity<String> restorePartner(@PathVariable String partnerId) {
        try {
            partnerService.restorePartner(partnerId);
            return ResponseEntity.ok("Đối tác đã được Hoạt Động trở lại");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //@IsAdmin
    @PostMapping("/delete/{partnerId}")
    public ResponseEntity<String> rejectPartner(@PathVariable String partnerId) {
        try {
            partnerService.deletePartner(partnerId);
            return ResponseEntity.ok("Đối tác đã được xóa hoặc bị từ chối phê duyệt thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
