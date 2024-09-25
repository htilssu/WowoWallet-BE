package com.ewallet.ewallet.repository;

import com.ewallet.ewallet.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepostitory extends JpaRepository<Bank, Long> {
}
