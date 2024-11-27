package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wowo.wowo.model.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class RoleDTO implements Serializable {

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
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}