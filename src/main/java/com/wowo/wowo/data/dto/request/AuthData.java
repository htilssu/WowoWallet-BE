package com.wowo.wowo.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthData {
    String username;
    String password;
}
