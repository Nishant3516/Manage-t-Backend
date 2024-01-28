package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolUserMapperTest {

    private SchoolUserMapper schoolUserMapper;

    @BeforeEach
    public void setUp() {
        schoolUserMapper = new SchoolUserMapperImpl();
    }
}
