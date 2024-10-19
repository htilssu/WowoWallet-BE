package com.wowo.wowo.validator;

import org.springframework.stereotype.Service;

@Service
public class MoneyValidator {

    public boolean isValidMoney(Long money) {
        return money >= 0;
    }
}
