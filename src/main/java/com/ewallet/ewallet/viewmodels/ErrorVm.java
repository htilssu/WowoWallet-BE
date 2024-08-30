package com.ewallet.ewallet.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
public class ErrorVm {
    private String message;
    private int errorCode;
}
