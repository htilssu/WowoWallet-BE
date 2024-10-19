package com.wowo.wowo.mongo.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "equity")
public class Equity {

    String user;
    String userType;
    Integer month;
    Integer year;
    List<EquityItem> equityItemList;



    @Data
    @AllArgsConstructor
    public static class EquityItem {

        LocalDate date;
        double in;
        double out;
    }
}
