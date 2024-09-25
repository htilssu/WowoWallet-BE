package com.ewallet.ewallet.user;

import com.ewallet.ewallet.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);
    User findByUserName(String userName);
    @Nullable
    User findByPhoneNumber(String phoneNumber);
    
}
