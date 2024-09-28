package com.wowo.wowo.repositories;

import com.wowo.wowo.models.GroupFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupFundRepository extends JpaRepository<GroupFund, Integer> {
    // Bạn có thể thêm các phương thức tùy chỉnh nếu cần
}