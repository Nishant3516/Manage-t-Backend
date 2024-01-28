package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolReportMapperTest {

    private SchoolReportMapper schoolReportMapper;

    @BeforeEach
    public void setUp() {
        schoolReportMapper = new SchoolReportMapperImpl();
    }
}
