package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolDaysOffMapperTest {

    private SchoolDaysOffMapper schoolDaysOffMapper;

    @BeforeEach
    public void setUp() {
        schoolDaysOffMapper = new SchoolDaysOffMapperImpl();
    }
}
