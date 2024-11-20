package com.wowo.wowo.repository;

import com.wowo.wowo.model.WalletTransaction;
import org.springframework.data.repository.CrudRepository;

public interface WalletTransactionRepository
        extends CrudRepository<WalletTransaction, String> {


}
