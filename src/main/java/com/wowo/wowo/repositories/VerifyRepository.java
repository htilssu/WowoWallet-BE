package com.wowo.wowo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wowo.wowo.models.Verify;

public interface VerifyRepository extends JpaRepository<Verify, Long>{
    List<Verify> findByCustomer_Id(String customerId);
}
