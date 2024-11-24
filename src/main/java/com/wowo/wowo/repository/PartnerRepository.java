package com.wowo.wowo.repository;

import com.wowo.wowo.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    Optional<Partner> findPartnerByApiKey(String apiKey);
}
