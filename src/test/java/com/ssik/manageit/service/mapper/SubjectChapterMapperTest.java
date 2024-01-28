package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubjectChapterMapperTest {

    private SubjectChapterMapper subjectChapterMapper;

    @BeforeEach
    public void setUp() {
        subjectChapterMapper = new SubjectChapterMapperImpl();
    }
}
