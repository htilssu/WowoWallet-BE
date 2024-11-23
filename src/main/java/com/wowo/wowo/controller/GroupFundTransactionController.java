package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.GroupFundTransactionDTO;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.data.dto.WithdrawRequestDTO;
import com.wowo.wowo.data.mapper.GroupFundTransactionMapper;
import com.wowo.wowo.model.GroupFundTransaction;
import com.wowo.wowo.service.GroupFundService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@IsUser
@RequestMapping(value = "v1/group-fund", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class GroupFundTransactionController {

    private final GroupFundService groupFundService;
    private final GroupFundTransactionMapper groupFundTransactionMapper;

    @PostMapping("/top-up")
    public GroupFundTransactionDTO topUp(@RequestBody @Validated TransferDTO transferDTO,
            Authentication authentication) {
        return groupFundTransactionMapper.toDto(
                groupFundService.topUp(Long.valueOf(transferDTO.getReceiverId()),
                        ((String) authentication.getPrincipal()),
                        transferDTO.getMoney(), transferDTO.getMessage()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<GroupFundTransactionDTO> withdraw(@RequestBody @Validated WithdrawRequestDTO withdrawRequestDTO) {

        final GroupFundTransaction groupFundTransaction = groupFundService.withdraw(
                withdrawRequestDTO.getGroupId(),
                withdrawRequestDTO.getAmount(), withdrawRequestDTO.getDescription());

        return ResponseEntity.ok(groupFundTransactionMapper.toDto(groupFundTransaction));
    }
}
