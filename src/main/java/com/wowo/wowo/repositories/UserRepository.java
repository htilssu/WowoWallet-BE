package com.wowo.wowo.repositories;

import com.wowo.wowo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findFirstByIdOrEmailOrUsername(String id, String id1, String id2);
}
