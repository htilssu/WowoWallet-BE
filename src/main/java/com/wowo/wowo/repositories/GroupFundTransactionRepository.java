package com.wowo.wowo.repositories;

import com.wowo.wowo.models.GroupFundTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFundTransactionRepository extends JpaRepository<GroupFundTransaction, String> {
    // Phương thức tùy chỉnh để tìm danh sách giao dịch theo groupId
    List<GroupFundTransaction> findByGroupId(int groupId);
}
