package com.iocod.spring_mongo_test.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "receipts")
public class Receipt {
    @Id
    private String id;
    private String name;
    private String email;
}
