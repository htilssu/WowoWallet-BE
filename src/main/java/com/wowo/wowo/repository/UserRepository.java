package com.wowo.wowo.repository;

import com.wowo.wowo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findFirstByIdOrEmailOrUsername(String id, String id1, String id2);
    List<User> findByUsername(String username);
    List<User> findByEmailContainingIgnoreCase(String email);
    // Thêm phương thức tìm kiếm theo fullName
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(TRIM(u.firstName)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
           "LOWER(TRIM(u.lastName)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
           "LOWER(CONCAT(TRIM(u.firstName), ' ', TRIM(u.lastName))) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("keyword") String keyword);
}
