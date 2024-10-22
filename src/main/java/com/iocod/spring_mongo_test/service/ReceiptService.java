package com.iocod.spring_mongo_test.service;

import com.iocod.spring_mongo_test.model.Receipt;
import com.iocod.spring_mongo_test.repository.ReceiptRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt saveReceipt(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public List<Receipt> saveReceipts(List<Receipt> receipts) {
        return receiptRepository.saveAll(receipts);
    }
}