package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentAttendenceMapperTest {

    private StudentAttendenceMapper studentAttendenceMapper;

    @BeforeEach
    public void setUp() {
        studentAttendenceMapper = new StudentAttendenceMapperImpl();
    }
}