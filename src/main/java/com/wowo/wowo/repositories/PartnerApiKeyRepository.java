package com.wowo.wowo.repositories;

import com.wowo.wowo.models.PartnerApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface PartnerApiKeyRepository extends JpaRepository<PartnerApiKey, String>,
                                                 JpaSpecificationExecutor<PartnerApiKey> {

    Collection<PartnerApiKey> findAllByPartner_Id(String partnerId);
}