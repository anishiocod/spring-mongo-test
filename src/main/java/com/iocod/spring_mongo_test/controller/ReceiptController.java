package com.iocod.spring_mongo_test.controller;

import com.iocod.spring_mongo_test.model.Receipt;
import com.iocod.spring_mongo_test.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Receipt> createReceipt(@RequestBody Receipt receipt) {
        // Capture the start time
        long startTime = System.currentTimeMillis();

        try {
            Receipt savedReceipt = receiptService.saveReceipt(receipt);

            // Capture the end time
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Log the duration
            logger.info("Time taken to process the request: {} ms", duration);

            return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error and return a 500 response
            e.printStackTrace();

            // Capture the end time even if an error occurs
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.error("Request processing failed. Time taken: {} ms", duration);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
