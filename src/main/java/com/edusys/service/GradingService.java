package com.edusys.service;

import com.edusys.entity.ExamAttemptEntity;

public interface GradingService {
    void gradeAttempt(ExamAttemptEntity attempt);
}
