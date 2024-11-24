package com.wowo.wowo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wowo.wowo.model.Verify;

public interface VerifyRepository extends JpaRepository<Verify, Long>{
    List<Verify> findByCustomer_Id(String customerId);
}
