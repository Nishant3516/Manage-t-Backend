package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolEventMapperTest {

    private SchoolEventMapper schoolEventMapper;

    @BeforeEach
    public void setUp() {
        schoolEventMapper = new SchoolEventMapperImpl();
    }
}
