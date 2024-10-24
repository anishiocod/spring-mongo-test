package com.iocod.spring_mongo_test.controller;

import com.iocod.spring_mongo_test.model.Receipt;
import com.iocod.spring_mongo_test.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Receipt> createReceipt(@RequestBody byte[] binaryData) {
        long startTime = System.currentTimeMillis();
        logger.info("Received request with binary data");

        try {
            // Parse binary data into key-value pairs
            String dataString = new String(binaryData, StandardCharsets.UTF_8);
            Map<String, String> parameters = parseWebhookData(dataString);

            // Create a new Receipt object and populate it from parsed data
            Receipt receipt = new Receipt();
            receipt.setApiVersion(parameters.get("ApiVersion"));
            receipt.setMessageStatus(parameters.get("MessageStatus"));
            receipt.setSmsSid(parameters.get("SmsSid"));
            receipt.setTo(parameters.get("To"));
            receipt.setFrom(parameters.get("From"));
            receipt.setAccountSid(parameters.get("AccountSid"));
            logger.info("Received Receipt SMSID: "+ receipt.getSmsSid());
            logger.info("Received Receipt AccountID: "+ receipt.getAccountSid());

            // Save the receipt using the service
            Receipt savedReceipt = receiptService.saveReceipt(receipt);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            logger.info("Saved Receipt SMSID: " + receipt.getSmsSid().toString());
            logger.info("Saved Receipt AccountID " + receipt.getAccountSid().toString());

            if (savedReceipt != null) {
                logger.info("Successfully created receipt with data: {}", savedReceipt);
                logger.info("Time taken: {} ms", duration);
                return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
            } else {
                logger.error("Failed to save receipt; received null object from service");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            logger.error("Failed to process receipt data: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> parseWebhookData(String dataString) {
        Map<String, String> parameters = new HashMap<>();
        String[] pairs = dataString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        return parameters;
    }
}
