package com.ewallet.ewallet.repositories;

import com.ewallet.ewallet.models.WalletTransaction;
import org.springframework.data.repository.CrudRepository;

public interface WalletTransactionRepository
        extends CrudRepository<WalletTransaction, String> {


}
