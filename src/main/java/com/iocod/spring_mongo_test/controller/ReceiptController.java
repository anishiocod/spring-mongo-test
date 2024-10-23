package com.iocod.spring_mongo_test.controller;

import com.iocod.spring_mongo_test.model.Receipt;
import com.iocod.spring_mongo_test.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Receipt> createReceipt(@RequestBody Receipt receipt) {
        long startTime = System.currentTimeMillis();
        logger.info("Received request to create receipt: {}", receipt);
        logger.info("Receipt Name",receipt.getName());

        try {
            Receipt savedReceipt = receiptService.saveReceipt(receipt);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            logger.info("Successfully created receipt. Time taken: {} ms", duration);
            return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Log the error and return a 400 Bad Request response
            logger.error("Invalid request body: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the error and return a 500 Internal Server Error response
            logger.error("Failed to create receipt. Reason: {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        logger.error("Unexpected error occurred during request processing", e);
        return new ResponseEntity<>("Internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}