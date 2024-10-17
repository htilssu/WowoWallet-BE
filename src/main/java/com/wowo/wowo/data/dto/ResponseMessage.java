package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseMessage {

    @JsonIgnore
    public static final ResponseMessage NOT_FOUND = new ResponseMessage("Not Found");
    String message;
}
