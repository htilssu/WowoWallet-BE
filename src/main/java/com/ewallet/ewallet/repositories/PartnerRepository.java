package com.ewallet.ewallet.repositories;

import com.ewallet.ewallet.models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    Optional<Partner> findPartnerByApiKey(String apiKey);
    boolean existsByEmail(String email);
    Partner findByEmail(String email);
}
