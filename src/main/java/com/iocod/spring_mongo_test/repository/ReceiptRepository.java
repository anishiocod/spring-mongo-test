package com.iocod.spring_mongo_test.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.iocod.spring_mongo_test.model.Receipt;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {
}
