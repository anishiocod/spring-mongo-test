package com.iocod.spring_mongo_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iocod.spring_mongo_test.model.Receipt;
import com.iocod.spring_mongo_test.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Added for JSON conversion

    @PostMapping
    public ResponseEntity<Void> createReceipt(@RequestBody byte[] binaryData, @RequestHeader Map<String, String> headers) {
        logger.info("Received request with binary data");
        // ... (logging similar to original code)

        try {
            String dataString = new String(binaryData, StandardCharsets.UTF_8);
            Map<String, String> parameters = parseWebhookData(dataString);
            logger.info("Parsed Parameters: {}", parameters);

            // Convert Map to JSON String
            String jsonData = objectMapper.writeValueAsString(parameters);

            // Send data to message queue
            amqpTemplate.convertAndSend("twillio-sms", jsonData);

            logger.info("Successfully sent data to message queue");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

    // Message listener - Single consumer using @RabbitListener(exclusive = true)
    @RabbitListener(queues = "twillio-sms", exclusive = true)
    public void processReceiptData(String jsonData) {
        try {
            logger.info("Received data from message queue: {}", jsonData);

            // Convert JSON String to Map
            Map<String, String> parameters = objectMapper.readValue(jsonData, Map.class);

            // Create a new Receipt object and populate it
            Receipt receipt = new Receipt();
            receipt.setApiVersion(parameters.get("ApiVersion"));
            receipt.setMessageStatus(parameters.get("MessageStatus"));
            receipt.setSmsSid(parameters.get("SmsSid"));
            receipt.setTo(parameters.get("To"));
            receipt.setFrom(parameters.get("From"));
            receipt.setAccountSid(parameters.get("AccountSid"));

            // Save the receipt using the service
            Receipt savedReceipt = receiptService.saveReceipt(receipt);

            if (savedReceipt != null) {
                logger.info("Successfully created receipt with data: {}", savedReceipt);
            } else {
                logger.error("Failed to save receipt; received null object from service");
            }
        } catch (Exception e) {
            logger.error("Failed to process receipt data: {}", e.getMessage());
        }
    }
}