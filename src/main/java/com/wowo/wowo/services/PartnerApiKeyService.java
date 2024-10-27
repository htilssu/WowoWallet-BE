package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.models.PartnerApiKey;
import com.wowo.wowo.repositories.PartnerApiKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PartnerApiKeyService {

    private final PartnerApiKeyRepository partnerApiKeyRepository;
    private final PartnerService partnerService;

    public Collection<PartnerApiKey> getPartnerApiKeys(String partnerId) {
        partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));
        return partnerApiKeyRepository.findAllByPartner_Id(partnerId);
    }

    public PartnerApiKey createApiKey(String partnerId) {
        final Partner partner = partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));
        PartnerApiKey partnerApiKey = new PartnerApiKey();
        partnerApiKey.setPartner(partner);
        return partnerApiKeyRepository.save(partnerApiKey);
    }

    public void deleteApiKey(String partnerId, String apiKeyId) {
        partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));
        partnerApiKeyRepository.deleteById(apiKeyId);
    }
}
