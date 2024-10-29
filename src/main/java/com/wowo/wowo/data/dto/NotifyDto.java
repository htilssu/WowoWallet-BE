package com.wowo.wowo.data.dto;

import lombok.Data;

@Data
public class NotifyDto {

    private String receiverId;
    private String message;
    private MessageType type;
}
