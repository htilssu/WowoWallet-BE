package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
import org.springframework.stereotype.Service;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public void createPartner(SSOData ssoData) {
        var partner = partnerRepository.findById(ssoData.getId());
        if (partner.isPresent()) return;

        Partner newPartner = new Partner();
        newPartner.setId(ssoData.getId());
        newPartner.setEmail(ssoData.getEmail());
        newPartner.setApiKey(ssoData.getId());
        try {
            partnerRepository.save(newPartner);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo đối tác");
        }
    }
}
