package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsPartner;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.models.PartnerApiKey;
import com.wowo.wowo.services.PartnerApiKeyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Partner Api Key", description = "Quản lý api key của đối tác")
@RequestMapping("/v1/partner/api-key")
@AllArgsConstructor
@IsPartner
public class PartnerApiKeyController {

    private final PartnerApiKeyService partnerApiKeyService;

    @GetMapping
    public Collection<PartnerApiKey> getApiKey(Authentication authentication) {
        var partnerId = (String) authentication.getPrincipal();
        return partnerApiKeyService.getPartnerApiKeys(partnerId);
    }

    @PostMapping
    public PartnerApiKey createApiKey(Authentication authentication, @RequestBody String name) {
        var partnerId = (String) authentication.getPrincipal();
        return partnerApiKeyService.createApiKey(partnerId, name);
    }

    @DeleteMapping("/{apiKeyId}")
    public ResponseEntity<?> deleteApiKey(Authentication authentication,
            @PathVariable @NotNull @Validated String apiKeyId) {
        var partnerId = (String) authentication.getPrincipal();
        partnerApiKeyService.deleteApiKey(partnerId, apiKeyId);
        return ResponseEntity.ok(new ResponseMessage("Xóa api key thành công"));
    }
}
