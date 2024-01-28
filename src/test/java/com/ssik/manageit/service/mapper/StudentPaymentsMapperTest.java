package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentPaymentsMapperTest {

    private StudentPaymentsMapper studentPaymentsMapper;

    @BeforeEach
    public void setUp() {
        studentPaymentsMapper = new StudentPaymentsMapperImpl();
    }
}
