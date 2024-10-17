package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserDto implements Serializable {

    @NotNull
    @Size(max = 50)
    private String firstName;
    @NotNull
    @Size(max = 50)
    private String lastName;
    @NotNull
    @Size(max = 255)
    private String email;
    @Size(max = 50)
    private String username;
    @NotNull
    @Size(max = 255)
    private String password;
    @NotNull
    private String dob;
    private Boolean gender;
    @Size(max = 255)
    private String address;
    @Size(max = 10)
    private String phoneNumber;
    @Size(max = 255)
    private String job;
}