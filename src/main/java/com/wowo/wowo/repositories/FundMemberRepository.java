package com.wowo.wowo.repositories;

import com.wowo.wowo.models.FundMember;
import com.wowo.wowo.models.FundMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundMemberRepository extends JpaRepository<FundMember, FundMemberId> {
    // Phương thức tùy chỉnh để tìm danh sách thành viên theo groupId
    List<FundMember> findByGroupId(int groupId);
}
