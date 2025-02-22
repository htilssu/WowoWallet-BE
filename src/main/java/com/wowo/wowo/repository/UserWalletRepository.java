package com.wowo.wowo.repository;

import com.wowo.wowo.model.User;
import com.wowo.wowo.model.UserWallet;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserWalletRepository extends CrudRepository<UserWallet, Long> {

    Optional<UserWallet> findByUser(User user);
    UserWallet findUserWalletByUser(User user);
    UserWallet findUserWalletByUser_Id(@Size(max = 32) String userId);
    @Size(max = 32) String user(User user);
}
