package com.edusys.service;

import com.edusys.model.dto.ReceiptDTO;

import java.util.List;

public interface ReceiptService {
    ReceiptDTO create(ReceiptDTO receiptDTO);
    ReceiptDTO getById(String id);
    List<ReceiptDTO> getAll();
    ReceiptDTO update(String id, ReceiptDTO receiptDTO);
    boolean delete(String id);
}
