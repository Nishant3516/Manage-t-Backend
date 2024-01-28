package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassStudentMapperTest {

    private ClassStudentMapper classStudentMapper;

    @BeforeEach
    public void setUp() {
        classStudentMapper = new ClassStudentMapperImpl();
    }
}
