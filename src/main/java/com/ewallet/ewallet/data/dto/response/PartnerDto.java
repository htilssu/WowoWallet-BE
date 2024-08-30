package com.ewallet.ewallet.data.dto.response;

import com.ewallet.ewallet.models.Partner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Partner}
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerDto implements Serializable {

    @Size(max = 10)
    String id;
    @NotNull
    @Size(max = 255)
    String name;
    String description;
    @NotNull
    @Size(max = 255)
    String email;
    @NotEmpty(message = "Không được để trống")
    @NotBlank(message = "Không được để trống")
    String partnerType;
    @Size(max = 255)
    String avatar;
    @NotNull
    @Size(max = 255)
    String apiBaseUrl;
    @NotNull
    @Size(max = 255)
    String apiKey;
    double balance;
    @Setter
    @NotNull
    String created;

}