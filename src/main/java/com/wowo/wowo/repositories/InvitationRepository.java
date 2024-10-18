package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Invitation;
import com.wowo.wowo.models.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    // Tìm tất cả lời mời đã gửi bởi senderId
    List<Invitation> findBySenderId(String senderId);

    // Tìm tất cả lời mời được nhận bởi recipientId
    List<Invitation> findByRecipientId(String recipientId);

    // Kiểm tra lời mời đã tồn tại với trạng thái PENDING
    boolean existsByGroupIdAndRecipientIdAndStatus(Long groupId, String recipientId, InvitationStatus status);
}
