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
 * DTO for {@link AtmCard}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtmCardDTO implements Serializable {

    private Integer id;
    @NotNull
    @Size(max = 16)
    private String cardNumber;
    private String atmId;
    @Size(max = 3)
    private String ccv;
    @NotNull
    @Size(max = 255)
    private String holderName;
    @NotNull
    private Integer month;
    @NotNull
    private Integer year;
    private String created;
    private Long bankId;
    private String bankShortName;
}