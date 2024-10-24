package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDto {
    private Long groupId;
    private String senderId;
    private String recipientId;
}

