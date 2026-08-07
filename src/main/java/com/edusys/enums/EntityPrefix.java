package com.edusys.enums;

import lombok.Getter;

@Getter
public enum EntityPrefix {
    USER("usr"),
    STUDENT("stu"),
    TEACHER("tec"),
    ADMIN("adm"),
    REVIEWER("rev"),
    PARENT("par"),
    COURSE("crs"),
    BATCH("bat"),
    SEMESTER("sem"),
    ACADEMIC_CALENDAR("cal"),
    ENROLLMENT("enr"),
    FEE_RECORD("fee"),
    RECEIPT("rec"),
    QUESTION_BANK("qst"),
    QUESTION("qst"),
    QUESTION_OPTION("qop"),
    EXAM("exm"),
    EXAM_ATTEMPT("eat"),
    ASSIGNMENT("asn"),
    ASSIGNMENT_SUBMISSION("asb"),
    GRADE("grd"),
    PARENT_STUDENT_LINK("psl"),
    INQUIRY("inq"),
    CAREER_TASK("ctk"),
    CAREER_SUBMISSION("csb"),
    EVALUATION("evl"),
    CAREER_LEVEL("lvl"),
    CAREER_POINTS_LEDGER("cpl");

    private final String prefix;

    EntityPrefix(String prefix) {
        this.prefix = prefix;
    }
}
