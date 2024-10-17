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
        Partner partner = new Partner();
        partner.setId(ssoData.getId());
        partner.setEmail(ssoData.getEmail());
        partnerRepository.save(partner);
    }
}
