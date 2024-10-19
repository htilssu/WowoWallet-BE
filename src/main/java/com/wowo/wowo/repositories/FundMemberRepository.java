package com.wowo.wowo.repositories;

import com.wowo.wowo.models.FundMember;
import com.wowo.wowo.models.FundMemberId;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundMemberRepository extends JpaRepository<FundMember, FundMemberId> {
    // Phương thức tìm danh sách thành viên theo groupId
    List<FundMember> findByGroupId(Long groupId);
    // Kiểm tra xem thành viên đã tham gia nhóm hay chưa
    boolean existsByGroupIdAndMemberId(Long group_id, @Size(max = 10) String member_id);

    List<FundMember> findByMemberId(String memberId);
}
