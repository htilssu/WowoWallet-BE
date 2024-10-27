package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.GroupFundTransactionDto;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.data.dto.WithdrawRequestDto;
import com.wowo.wowo.data.mapper.GroupFundTransactionMapper;
import com.wowo.wowo.services.GroupFundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@IsUser
@RequestMapping(value = "v1/group-fund", produces = "application/json; charset=utf-8")
public class GroupFundTransactionController {

    private final GroupFundService groupFundService;
    private final GroupFundTransactionMapper groupFundTransactionMapper;

    public GroupFundTransactionController(GroupFundService groupFundService,
            GroupFundTransactionMapper groupFundTransactionMapper) {
        this.groupFundService = groupFundService;
        this.groupFundTransactionMapper = groupFundTransactionMapper;
    }

    @PostMapping("/top-up")
    public GroupFundTransactionDto topUp(@RequestBody TransferDto transferDto) {
        return groupFundTransactionMapper.toDto(
                groupFundService.topUp(Long.valueOf(transferDto.getReceiverId()),
                        transferDto.getSenderId(),
                        transferDto.getMoney()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResponseMessage> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {

        groupFundService.withdraw(withdrawRequestDto.getGroupId(),
                withdrawRequestDto.getAmount());

        return ResponseEntity.ok(new ResponseMessage("Rút tiền thành công"));
    }
}
