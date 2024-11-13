package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.models.PartnerStatus;
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
        newPartner.setApiKey(ssoData.getEmail());
        newPartner.setName(ssoData.getName());
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

    @Transactional
    public void approvePartner(String partnerId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new RuntimeException("Đối tác không tồn tại");
        }

        Partner partner = partnerOpt.get();

        if (partner.getStatus() == PartnerStatus.ACTIVE) {
            throw new RuntimeException("Đối tác đã được Phê Duyệt từ trước");
        }
        if (partner.getStatus() != PartnerStatus.INACTIVE) {
            throw new RuntimeException("Đối tác chưa gửi yêu cầu phê duyệt hoặc đã bị tạm ngưng");
        }


        partner.setStatus(PartnerStatus.ACTIVE);
        partnerRepository.save(partner);
    }

    @Transactional
    public void suspendPartner(String partnerId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new RuntimeException("Đối tác không Tồn Tại");
        }

        Partner partner = partnerOpt.get();

        if (partner.getStatus() == PartnerStatus.SUSPENDED) {
            throw new RuntimeException("Đối tác đã bị Tạm Ngưng");
        }

        partner.setStatus(PartnerStatus.SUSPENDED);
        partnerRepository.save(partner);
    }

    @Transactional
    public void restorePartner(String partnerId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) {
            throw new RuntimeException("Đối tác không tồn tại");
        }

        Partner partner = partnerOpt.get();

        if (partner.getStatus() != PartnerStatus.SUSPENDED) {
            throw new RuntimeException("Đối tác không ở trạng thái tạm ngưng");
        }

        partner.setStatus(PartnerStatus.ACTIVE);
        partnerRepository.save(partner);
    }

    //từ chối đối tác hoặc xoá đối tác ra khỏi hệ thống
    @Transactional
    public void deletePartner(String partnerId) {
        Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);

        if (partnerOpt.isEmpty()) {
            throw new RuntimeException("Đối tác không tồn tại");
        }

        Partner partner = partnerOpt.get();

        if (partner.getStatus() == PartnerStatus.ACTIVE) {
            throw new RuntimeException("Đối tác đang hoạt động, không thể làm thao tác này");
        }

        try {
            partnerRepository.delete(partner);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa đối tác: " + e.getMessage());
        }
    }

}
