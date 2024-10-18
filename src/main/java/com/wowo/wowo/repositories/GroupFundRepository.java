package com.wowo.wowo.repositories;

import com.wowo.wowo.models.GroupFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupFundRepository extends JpaRepository<GroupFund, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh nếu cần
    List<GroupFund> findAllById(Iterable<Long> ids);
}