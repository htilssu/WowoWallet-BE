package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ErrorVm {
    private String message;
    private int errorCode;
    private String error;
}
