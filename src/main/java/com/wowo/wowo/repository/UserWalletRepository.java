package com.wowo.wowo.repository;

import com.wowo.wowo.model.User;
import com.wowo.wowo.model.UserWallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserWalletRepository extends CrudRepository<UserWallet, Long> {

    Optional<UserWallet> findByUser(User user);
}
