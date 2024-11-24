package com.wowo.wowo.repository;

import com.wowo.wowo.model.GroupFundTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFundTransactionRepository extends JpaRepository<GroupFundTransaction, Long> {
    List<GroupFundTransaction> findByGroupId(Long groupId);
    List<GroupFundTransaction> findByGroup_IdOrderByTransaction_CreatedDesc(Long id,
            Pageable pageable);
}
