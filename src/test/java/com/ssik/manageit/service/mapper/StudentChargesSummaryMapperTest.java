package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentChargesSummaryMapperTest {

    private StudentChargesSummaryMapper studentChargesSummaryMapper;

    @BeforeEach
    public void setUp() {
        studentChargesSummaryMapper = new StudentChargesSummaryMapperImpl();
    }
}
