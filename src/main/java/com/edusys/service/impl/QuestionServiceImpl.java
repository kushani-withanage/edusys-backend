package com.edusys.service.impl;

import com.edusys.entity.QuestionEntity;
import com.edusys.entity.QuestionOptionEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.QuestionDTO;
import com.edusys.model.dto.QuestionOptionDTO;
import com.edusys.repository.QuestionRepository;
import com.edusys.repository.QuestionOptionRepository;
import com.edusys.repository.ExamQuestionRepository;
import com.edusys.service.QuestionService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public QuestionDTO create(QuestionDTO dto) {
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            dto.setId(idGenerator.generateId(EntityPrefix.QUESTION, questionRepository.count()));
        }
        dto.setStatus("ACTIVE");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        if (dto.getCreatedBy() == null || dto.getCreatedBy().trim().isEmpty()) {
            org.springframework.security.core.Authentication auth =
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() != null) {
                dto.setCreatedBy(auth.getPrincipal().toString());
            } else {
                dto.setCreatedBy("usr0001");
            }
        }

        QuestionEntity entity = mapper.map(dto, QuestionEntity.class);
        
        // Map options
        if (dto.getOptions() != null) {
            List<QuestionOptionEntity> optionEntities = new ArrayList<>();
            for (QuestionOptionDTO optDto : dto.getOptions()) {
                if (optDto.getId() == null || optDto.getId().trim().isEmpty()) {
                    optDto.setId(idGenerator.generateId(EntityPrefix.QUESTION_OPTION, questionOptionRepository.count() + optionEntities.size()));
                }
                QuestionOptionEntity optEntity = mapper.map(optDto, QuestionOptionEntity.class);
                optEntity.setQuestion(entity);
                optionEntities.add(optEntity);
            }
            entity.setOptions(optionEntities);
        }

        QuestionEntity saved = questionRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public QuestionDTO getById(String id) {
        return questionRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<QuestionDTO> getAll() {
        List<QuestionDTO> list = new ArrayList<>();
        questionRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    @Transactional
    public QuestionDTO update(String id, QuestionDTO dto) {
        QuestionEntity existing = questionRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        // Critical constraint: once a question is attached to any exam with status PUBLISHED or CLOSED,
        // it becomes LOCKED. Editing a locked question must create a new question row, not mutate the original.
        boolean isLocked = "LOCKED".equalsIgnoreCase(existing.getStatus()) 
            || examQuestionRepository.existsByQuestionIdAndExamStatusIn(id, Arrays.asList("PUBLISHED", "CLOSED"));

        if (isLocked) {
            // Create a new question row instead of modifying
            dto.setId(null); 
            dto.setStatus("ACTIVE");
            return create(dto);
        }

        existing.setQuestionText(dto.getQuestionText());
        existing.setQuestionType(dto.getQuestionType());
        existing.setDifficulty(dto.getDifficulty());
        existing.setDefaultMarks(dto.getDefaultMarks());
        existing.setUpdatedAt(LocalDateTime.now());

        // Replace options
        if (existing.getOptions() != null) {
            existing.getOptions().clear();
        } else {
            existing.setOptions(new ArrayList<>());
        }

        if (dto.getOptions() != null) {
            for (QuestionOptionDTO optDto : dto.getOptions()) {
                if (optDto.getId() == null || optDto.getId().trim().isEmpty()) {
                    optDto.setId(idGenerator.generateId(EntityPrefix.QUESTION_OPTION, questionOptionRepository.count() + existing.getOptions().size()));
                }
                QuestionOptionEntity optEntity = mapper.map(optDto, QuestionOptionEntity.class);
                optEntity.setQuestion(existing);
                existing.getOptions().add(optEntity);
            }
        }

        QuestionEntity updated = questionRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        QuestionEntity existing = questionRepository.findById(id).orElse(null);
        if (existing != null) {
            // Check if attached to published/closed exams
            boolean isLocked = "LOCKED".equalsIgnoreCase(existing.getStatus()) 
                || examQuestionRepository.existsByQuestionIdAndExamStatusIn(id, Arrays.asList("PUBLISHED", "CLOSED"));
            if (isLocked) {
                throw new IllegalStateException("Cannot delete a locked question attached to a published or closed exam.");
            }
            questionRepository.delete(existing);
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionDTO> getQuestions(String courseId, String difficulty, String status) {
        List<QuestionEntity> entities;
        boolean hasDifficulty = difficulty != null && !difficulty.trim().isEmpty() && !"ALL".equalsIgnoreCase(difficulty);
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status);

        if (hasDifficulty && hasStatus) {
            entities = questionRepository.findByCourseIdAndDifficultyAndStatus(courseId, difficulty, status);
        } else if (hasDifficulty) {
            entities = questionRepository.findByCourseIdAndDifficulty(courseId, difficulty);
        } else if (hasStatus) {
            entities = questionRepository.findByCourseIdAndStatus(courseId, status);
        } else {
            entities = questionRepository.findByCourseId(courseId);
        }

        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<QuestionDTO> importQuestionsFromCsv(String courseId, String createdBy, String csvContent) {
        List<QuestionDTO> imported = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new StringReader(csvContent))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip CSV headers
                }

                List<String> cols = parseCsvLine(line);
                if (cols.size() < 5) {
                    // Skip invalid rows
                    continue;
                }

                String text = cols.get(0);
                String type = cols.get(1); // SINGLE_CHOICE, MULTI_CHOICE
                String diff = cols.get(2); // EASY, MEDIUM, HARD
                int marks = Integer.parseInt(cols.get(3));
                String optionsStr = cols.get(4); // pipe-separated
                String correctIndicesStr = cols.size() > 5 ? cols.get(5) : "0"; // comma/pipe separated correct indices

                List<String> optionsList = Arrays.asList(optionsStr.split("\\|"));
                Set<Integer> correctIndices = new HashSet<>();
                if (correctIndicesStr != null && !correctIndicesStr.trim().isEmpty()) {
                    String[] parts = correctIndicesStr.split("[,|]");
                    for (String part : parts) {
                        try {
                            correctIndices.add(Integer.parseInt(part.trim()));
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }
                }

                List<QuestionOptionDTO> optionDTOs = new ArrayList<>();
                for (int i = 0; i < optionsList.size(); i++) {
                    optionDTOs.add(QuestionOptionDTO.builder()
                            .optionText(optionsList.get(i).trim())
                            .isCorrect(correctIndices.contains(i))
                            .orderIndex(i)
                            .build());
                }

                QuestionDTO qDto = QuestionDTO.builder()
                        .courseId(courseId)
                        .questionText(text)
                        .questionType(type.toUpperCase())
                        .difficulty(diff.toUpperCase())
                        .defaultMarks(marks)
                        .createdBy(createdBy)
                        .options(optionDTOs)
                        .build();

                imported.add(create(qDto));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV question import file.", e);
        }
        return imported;
    }

    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        return result;
    }

    private QuestionDTO convertToDTO(QuestionEntity entity) {
        QuestionDTO dto = mapper.map(entity, QuestionDTO.class);
        if (entity.getOptions() != null) {
            List<QuestionOptionDTO> options = entity.getOptions().stream()
                    .map(o -> mapper.map(o, QuestionOptionDTO.class))
                    .sorted(Comparator.comparing(QuestionOptionDTO::getOrderIndex))
                    .collect(Collectors.toList());
            dto.setOptions(options);
        }
        return dto;
    }
}
