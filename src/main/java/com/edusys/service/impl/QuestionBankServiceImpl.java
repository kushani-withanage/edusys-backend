package com.edusys.service.impl;

import com.edusys.entity.QuestionBankEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.QuestionBankDTO;
import com.edusys.repository.QuestionBankRepository;
import com.edusys.service.QuestionBankService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public QuestionBankDTO create(QuestionBankDTO questionBankDTO) {
        if (questionBankDTO.getQuestionId() == null || questionBankDTO.getQuestionId().trim().isEmpty()) {
            questionBankDTO.setQuestionId(idGenerator.generateId(EntityPrefix.QUESTION_BANK, questionBankRepository.count()));
        }
        QuestionBankEntity entity = mapper.map(questionBankDTO, QuestionBankEntity.class);
        QuestionBankEntity saved = questionBankRepository.save(entity);
        return mapper.map(saved, QuestionBankDTO.class);
    }

    @Override
    public QuestionBankDTO getById(String id) {
        return questionBankRepository.findById(id)
                .map(entity -> mapper.map(entity, QuestionBankDTO.class))
                .orElse(null);
    }

    @Override
    public List<QuestionBankDTO> getAll() {
        List<QuestionBankDTO> list = new ArrayList<>();
        questionBankRepository.findAll().forEach(entity -> list.add(mapper.map(entity, QuestionBankDTO.class)));
        return list;
    }

    @Override
    public QuestionBankDTO update(String id, QuestionBankDTO questionBankDTO) {
        if (!questionBankRepository.existsById(id)) {
            return null;
        }
        questionBankDTO.setQuestionId(id);
        QuestionBankEntity entity = mapper.map(questionBankDTO, QuestionBankEntity.class);
        QuestionBankEntity updated = questionBankRepository.save(entity);
        return mapper.map(updated, QuestionBankDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (questionBankRepository.existsById(id)) {
            questionBankRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
