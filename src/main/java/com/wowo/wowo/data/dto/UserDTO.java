package com.wowo.wowo.data.dto;

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
public class UserDTO implements Serializable {

    @Size(max = 32)
    private String id;
    @NotNull
    private Boolean isActive = false;
    @NotNull
    private Boolean isVerified = false;
    private String job;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phoneNumber;
    private Long totalMoney;

    /**
     * Lấy họ tên đầy đủ của người dùng
     * 
     * @return họ tên đầy đủ
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}