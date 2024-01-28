package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentDiscountMapperTest {

    private StudentDiscountMapper studentDiscountMapper;

    @BeforeEach
    public void setUp() {
        studentDiscountMapper = new StudentDiscountMapperImpl();
    }
}
