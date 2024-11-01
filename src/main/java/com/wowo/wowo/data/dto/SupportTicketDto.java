package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SupportTicketDto {
    private Long id;
    private String customerId; 
    private String title;
    private String content;
    private String status;
}
