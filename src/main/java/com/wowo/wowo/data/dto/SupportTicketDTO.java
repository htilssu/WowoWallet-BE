package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
public class SupportTicketDTO {
    private Long id;
    private Customer customer; 
    private String title;
    private String content;
    private String status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    public static class Customer {
        private String id;  
    }
}