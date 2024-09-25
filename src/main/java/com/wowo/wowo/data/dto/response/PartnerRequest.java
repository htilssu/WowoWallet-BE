package com.wowo.wowo.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerRequest {

    private
    String name;
    private String description;
    private String email;
    private String partnerType;
    private String password;
    private String apiBaseUrl;
}
