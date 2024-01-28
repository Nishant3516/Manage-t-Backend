package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolClassMapperTest {

    private SchoolClassMapper schoolClassMapper;

    @BeforeEach
    public void setUp() {
        schoolClassMapper = new SchoolClassMapperImpl();
    }
}
