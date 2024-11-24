package com.wowo.wowo.repository;

import com.wowo.wowo.model.GroupFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFundRepository extends JpaRepository<GroupFund, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh nếu cần
    List<GroupFund> findAllById(Iterable<Long> ids);
}