package com.wowo.wowo.repositories;

import com.wowo.wowo.models.WalletTransaction;
import org.springframework.data.repository.CrudRepository;

public interface WalletTransactionRepository
        extends CrudRepository<WalletTransaction, String> {


}
