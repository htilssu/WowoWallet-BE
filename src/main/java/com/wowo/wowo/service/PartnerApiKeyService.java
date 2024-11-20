package com.wowo.wowo.service;

import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.model.Partner;
import com.wowo.wowo.model.PartnerApiKey;
import com.wowo.wowo.repository.PartnerApiKeyRepository;
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

    public PartnerApiKey createApiKey(String partnerId, String name) {
        final Partner partner = partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));
        PartnerApiKey partnerApiKey = new PartnerApiKey();
        partnerApiKey.setPartner(partner);
        partnerApiKey.setName(name);
        return partnerApiKeyRepository.save(partnerApiKey);
    }

    public void deleteApiKey(String partnerId, String apiKeyId) {
        partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));
        partnerApiKeyRepository.deleteById(apiKeyId);
    }
}
