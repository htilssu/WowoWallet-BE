package com.wowo.wowo.data.dto;

import java.time.LocalDateTime;

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
public class VerifyDTO {
    private Long id;
    private Customer customer; 
    private String type;
    private Long numberCard;
    private LocalDateTime openDay;
    private LocalDateTime closeDay;
    private String fontImage;
    private String behindImage;
    private String userImage;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    public static class Customer {
        private String id;  
    }
}
