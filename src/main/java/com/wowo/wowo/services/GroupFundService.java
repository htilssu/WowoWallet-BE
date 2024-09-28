package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.response.FundMemberDto;
import com.wowo.wowo.data.dto.response.GroupFundDto;
import com.wowo.wowo.data.dto.response.GroupFundTransactionDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.FundMember;
import com.wowo.wowo.models.GroupFund;
import com.wowo.wowo.models.GroupFundTransaction;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.FundMemberRepository;
import com.wowo.wowo.repositories.GroupFundRepository;
import com.wowo.wowo.repositories.GroupFundTransactionRepository;
import com.wowo.wowo.repositories.UserRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupFundService {

    private final GroupFundRepository groupFundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final GroupFundTransactionRepository groupFundTransactionRepository;
    private final UserRepository userRepository;

    public GroupFundService(GroupFundRepository groupFundRepository,
                            FundMemberRepository fundMemberRepository,
                            GroupFundTransactionRepository groupFundTransactionRepository, UserRepository userRepository) {
        this.groupFundRepository = groupFundRepository;
        this.fundMemberRepository = fundMemberRepository;
        this.groupFundTransactionRepository = groupFundTransactionRepository;
        this.userRepository = userRepository;
    }

    // Tạo quỹ nhóm
    public GroupFund createGroupFund(GroupFundDto groupFundDto, Authentication authentication) {
        // Lấy thông tin người gửi request từ Authentication
        String ownerId = authentication.getName();  // Lấy ownerId từ username trong Authentication
        Optional<User> userOptional = userRepository.findById(ownerId);

        // Kiểm tra người dùng có tồn tại không
        User owner = userOptional.orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));

        // Tạo quỹ nhóm mới
        GroupFund groupFund = new GroupFund();
        groupFund.setOwner(owner);  // Đặt owner là người dùng tạo quỹ
        groupFund.setName(groupFundDto.getName());
        groupFund.setDescription(groupFundDto.getDescription());
        groupFund.setBalance(groupFundDto.getBalance());
        groupFund.setTarget(groupFundDto.getTarget());

        // Lưu vào database và trả về quỹ vừa tạo
        return groupFundRepository.save(groupFund);
    }


    //them thanh vien vao quy
    public FundMember addMemberToGroup(int groupId, FundMemberDto memberDto) {
        if (!groupFundRepository.existsById(groupId)) {
            throw new NotFoundException("Quỹ không tồn tại với ID: " + groupId);
        }

        FundMember member = new FundMember();
//        member.setGroupId(groupId);
//        member.setMemberId(memberDto.getMemberId());
        member.setMoney(memberDto.getMoney());
        return fundMemberRepository.save(member);
    }

    //ghi nhan giao dich
    public GroupFundTransaction createTransaction(int groupId, GroupFundTransactionDto transactionDto) {
        if (!groupFundRepository.existsById(groupId)) {
            throw new NotFoundException("Quỹ không tồn tại với ID: " + groupId);
        }

        GroupFundTransaction transaction = new GroupFundTransaction();
//        transaction.setGroupId(groupId);
//        transaction.setMemberId(transactionDto.getMemberId());
        transaction.setMoney(transactionDto.getMoney());
        return groupFundTransactionRepository.save(transaction);
    }

    //lay thong tin quy
    public GroupFund getGroupFund(int id) {
        return groupFundRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quỹ không tìm thấy với ID: " + id));
    }

    //lay danh sach thanh vien
    public List<FundMember> getGroupMembers(int groupId) {
        if (!groupFundRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Quỹ không tồn tại với ID: " + groupId);
        }
        return fundMemberRepository.findByGroupId(groupId);
    }

    //lay lich su giao dich quy
    public List<GroupFundTransaction> getTransactions(int groupId) {
        if (!groupFundRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Quỹ không tồn tại với ID: " + groupId);
        }
        return groupFundTransactionRepository.findByGroupId(groupId);
    }

}

