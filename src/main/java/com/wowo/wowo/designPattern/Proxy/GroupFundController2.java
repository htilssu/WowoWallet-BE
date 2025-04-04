package com.wowo.wowo.designPattern.Proxy;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.model.FundMember;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/group-fund")
@AllArgsConstructor
public class GroupFundController2 {

    private final GroupFundServiceProxy groupFundService;

    @PostMapping
    public GroupFund createGroupFund(@RequestBody GroupFundDTO groupFundDTO, Authentication authentication) {
        return groupFundService.createGroupFund(groupFundDTO, authentication);
    }

    @PostMapping("/{groupId}/members")
    public FundMember addMemberToGroup(@PathVariable Long groupId, @RequestParam String memberId) {
        return groupFundService.addMemberToGroup(groupId, memberId);
    }

    @PostMapping("/{groupId}/leave")
    public Map<String, Object> leaveGroupFund(@PathVariable Long groupId, @RequestParam String memberId) {
        return groupFundService.leaveGroupFund(groupId, memberId);
    }

    @GetMapping("/{groupId}")
    public GroupFundDTO getGroupFund(@PathVariable Long groupId) {
        return groupFundService.getGroupFund(groupId);
    }

    @GetMapping("/{groupId}/members")
    public List<UserDTO> getGroupMembers(@PathVariable Long groupId) {
        return groupFundService.getGroupMembers(groupId);
    }

    @GetMapping("/user")
    public Map<String, List<GroupFundDTO>> getUserGroupFunds(Authentication authentication) {
        return groupFundService.getUserGroupFunds(authentication);
    }

    @PutMapping("/{groupId}")
    public GroupFundDTO updateGroupFund(@PathVariable Long groupId, @RequestBody GroupFundDTO groupFundDTO, Authentication authentication) {
        return groupFundService.updateGroupFund(groupId, groupFundDTO, authentication);
    }

    @PostMapping("/{groupId}/top-up")
    public Transaction topUp(@PathVariable Long groupId, @RequestParam String memberId, @RequestParam Long amount, @RequestParam String message) {
        return groupFundService.topUp(groupId, memberId, amount, message);
    }

    @GetMapping("/{groupId}/transactions")
    public List<TransactionDTO> getTransactionHistory(@PathVariable Long groupId, @RequestParam Integer offset, @RequestParam Integer page) {
        return groupFundService.getTransactionHistory(groupId, offset, page);
    }

    @PostMapping("/{groupId}/withdraw")
    public Transaction withdraw(@PathVariable Long groupId, @RequestParam Long amount, @RequestParam String message) {
        return groupFundService.withdraw(groupId, amount, message);
    }

    @PostMapping("/{groupId}/lock")
    public GroupFund lockGroupFund(@PathVariable Long groupId, Authentication authentication) {
        return groupFundService.lockGroupFund(groupId, authentication);
    }

    @PostMapping("/{groupId}/unlock")
    public GroupFund unlockGroupFund(@PathVariable Long groupId, Authentication authentication) {
        return groupFundService.unlockGroupFund(groupId, authentication);
    }
}
