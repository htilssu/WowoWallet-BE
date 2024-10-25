package com.wowo.wowo.mongo.documents;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notify")
@Data
public class Notify {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    private String userId;
    private String type;
}
