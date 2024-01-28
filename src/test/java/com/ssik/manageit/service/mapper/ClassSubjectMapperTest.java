package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassSubjectMapperTest {

    private ClassSubjectMapper classSubjectMapper;

    @BeforeEach
    public void setUp() {
        classSubjectMapper = new ClassSubjectMapperImpl();
    }
}
