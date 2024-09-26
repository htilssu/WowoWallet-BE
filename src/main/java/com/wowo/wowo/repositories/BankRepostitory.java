package com.wowo.wowo.repositories;


import com.wowo.wowo.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BankRepostitory extends JpaRepository<Bank, Long> {
    Bank findByShortName(String shortName);;

}
