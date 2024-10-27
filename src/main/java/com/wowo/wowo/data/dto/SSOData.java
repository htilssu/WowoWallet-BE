package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SSOData {

    String email;
    String id;
    String username;
    String firstName;
    String lastName;
}
