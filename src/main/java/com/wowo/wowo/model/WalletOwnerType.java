package com.wowo.wowo.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

public enum WalletOwnerType {
    USER,
    GROUP_FUND,
    PARTNER;


    @Document(collection = "analysis")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Analysis {

        @Id
        private String id;
        private String target;
        @Enumerated(EnumType.STRING)
        private WalletOwnerType targetType = USER;
        private long totalTransactions;
        private double totalInMoney;
        private double totalOutMoney;
        private LocalDate date;
    }
}
