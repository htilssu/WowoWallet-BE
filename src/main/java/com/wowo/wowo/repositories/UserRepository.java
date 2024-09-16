package com.wowo.wowo.repositories;

import com.wowo.wowo.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);
    User findByUserName(String userName);
    @Nullable
    User findByPhoneNumber(String phoneNumber);
}
