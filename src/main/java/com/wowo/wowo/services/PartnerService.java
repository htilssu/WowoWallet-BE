package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final WalletService walletService;

    public PartnerService(PartnerRepository partnerRepository, WalletService walletService) {
        this.partnerRepository = partnerRepository;
        this.walletService = walletService;
    }

    @Transactional
    public void createPartner(SSOData ssoData) {
        var partner = partnerRepository.findById(ssoData.getId());
        if (partner.isPresent()) return;

        Partner newPartner = new Partner();
        newPartner.setId(ssoData.getId());
        newPartner.setEmail(ssoData.getEmail());
        newPartner.setApiKey(ssoData.getId());
        try {
            newPartner = partnerRepository.save(newPartner);
            walletService.createWallet(newPartner);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo đối tác");
        }
    }

    public Optional<Partner> getPartnerById(String partnerId) {
        return partnerRepository.findById(partnerId);
    }
}
