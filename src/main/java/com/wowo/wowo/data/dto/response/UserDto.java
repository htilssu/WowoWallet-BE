package com.wowo.wowo.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {

    @Size(max = 10)
    private String id;
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
    private String userName;
    @Size(max = 255)
    private String avatar;
    @NotNull
    private String dob;
    @NotNull
    private Boolean isActive = false;
    @NotNull
    private Boolean isVerified = false;
    private Boolean gender;
    @NotNull
    private String created;
    @Size(max = 255)
    private String address;
    @Size(max = 10)
    private String phoneNumber;
    @Size(max = 255)
    private String job;
}