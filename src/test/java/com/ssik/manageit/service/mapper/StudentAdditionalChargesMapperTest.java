package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentAdditionalChargesMapperTest {

    private StudentAdditionalChargesMapper studentAdditionalChargesMapper;

    @BeforeEach
    public void setUp() {
        studentAdditionalChargesMapper = new StudentAdditionalChargesMapperImpl();
    }
}
