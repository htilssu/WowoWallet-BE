package com.wowo.wowo.repository;


import com.wowo.wowo.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BankRepostitory extends JpaRepository<Bank, Long> {
    Bank findByShortName(String shortName);;

}
