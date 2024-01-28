package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassHomeWorkMapperTest {

    private ClassHomeWorkMapper classHomeWorkMapper;

    @BeforeEach
    public void setUp() {
        classHomeWorkMapper = new ClassHomeWorkMapperImpl();
    }
}
