package com.wowo.wowo.repository;

import com.wowo.wowo.model.PartnerApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface PartnerApiKeyRepository extends JpaRepository<PartnerApiKey, String>,
                                                 JpaSpecificationExecutor<PartnerApiKey> {

    Collection<PartnerApiKey> findAllByPartner_Id(String partnerId);
}