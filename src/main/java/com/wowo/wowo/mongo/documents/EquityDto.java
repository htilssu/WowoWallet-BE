package com.wowo.wowo.mongo.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Equity}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquityDto implements Serializable {

    String user;
    Integer month;
    Integer year;
    List<EquityItemDto> equityItemList;


    /**
     * DTO for {@link Equity.EquityItem}
     */
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EquityItemDto implements Serializable {

        String date;
        double in;
        double out;
    }
}