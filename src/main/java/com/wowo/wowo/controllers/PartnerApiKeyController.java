package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsPartner;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.models.PartnerApiKey;
import com.wowo.wowo.services.PartnerApiKeyService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1/partner/api-key")
public class PartnerApiKeyController {

    private final PartnerApiKeyService partnerApiKeyService;

    public PartnerApiKeyController(PartnerApiKeyService partnerApiKeyService) {
        this.partnerApiKeyService = partnerApiKeyService;
    }

    @GetMapping
    @IsPartner
    public Collection<PartnerApiKey> getApiKey(Authentication authentication) {
        var partnerId = (String) authentication.getPrincipal();
        return partnerApiKeyService.getPartnerApiKeys(partnerId);
    }

    @PostMapping
    @IsPartner
    public PartnerApiKey createApiKey(Authentication authentication) {
        var partnerId = (String) authentication.getPrincipal();
        return partnerApiKeyService.createApiKey(partnerId);
    }

    @DeleteMapping("/{apiKeyId}")
    @IsPartner
    public ResponseEntity<?> deleteApiKey(Authentication authentication,
            @PathVariable @NotNull @Validated String apiKeyId) {
        var partnerId = (String) authentication.getPrincipal();
        partnerApiKeyService.deleteApiKey(partnerId, apiKeyId);
        return ResponseEntity.ok(new ResponseMessage("Xóa api key thành công"));
    }
}
