package com.wowo.wowo.repositories;

import com.wowo.wowo.models.GroupFund;
import com.wowo.wowo.models.GroupFundInvitation;
import com.wowo.wowo.models.InvitationStatus;
import com.wowo.wowo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<GroupFundInvitation, Long> {
    // Tìm tất cả lời mời đã gửi bởi senderId
    List<GroupFundInvitation> findBySender(User sender);

    // Tìm tất cả lời mời được nhận bởi recipientId
    List<GroupFundInvitation> findByRecipient(User receiver);

    // Kiểm tra lời mời đã tồn tại với trạng thái PENDING
    boolean existsByGroupFundAndRecipientAndStatus(GroupFund groupFund, User recipient, InvitationStatus status);
}
