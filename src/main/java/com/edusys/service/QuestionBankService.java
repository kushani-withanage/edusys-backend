package com.edusys.service;

import com.edusys.model.dto.QuestionBankDTO;

import java.util.List;

public interface QuestionBankService {
    QuestionBankDTO create(QuestionBankDTO questionBankDTO);
    QuestionBankDTO getById(String id);
    List<QuestionBankDTO> getAll();
    QuestionBankDTO update(String id, QuestionBankDTO questionBankDTO);
    boolean delete(String id);
}
