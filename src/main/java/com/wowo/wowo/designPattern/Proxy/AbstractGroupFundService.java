package com.wowo.wowo.designPattern.Proxy;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.model.FundMember;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.Transaction;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public abstract class AbstractGroupFundService {
    //các phương thức.
    public abstract GroupFund createGroupFund(GroupFundDTO groupFundDTO, Authentication authentication);
    public abstract FundMember addMemberToGroup(Long groupId, String memberId);
    public abstract FundMember addMemberToGroup(GroupFund groupFund, String memberId);
    public abstract Map<String, Object> leaveGroupFund(Long groupId, String memberId);
    public abstract GroupFundDTO getGroupFund(Long id);
    public abstract List<UserDTO> getGroupMembers(Long groupId);
    public abstract Map<String, List<GroupFundDTO>> getUserGroupFunds(Authentication authentication);
    public abstract GroupFundDTO updateGroupFund(Long groupId, GroupFundDTO groupFundDTO, Authentication authentication);
    public abstract Transaction topUp(Long groupId, String memberId, Long amount, String message);
    public abstract List<TransactionDTO> getTransactionHistory(Long groupId, Integer offset, Integer page);
    public abstract Transaction withdraw(Long groupFundId, Long amount, String message);
    public abstract GroupFund lockGroupFund(Long groupId, Authentication authentication);
    public abstract GroupFund unlockGroupFund(Long groupId, Authentication authentication);
}
