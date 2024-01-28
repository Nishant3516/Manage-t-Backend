package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassClassWorkMapperTest {

    private ClassClassWorkMapper classClassWorkMapper;

    @BeforeEach
    public void setUp() {
        classClassWorkMapper = new ClassClassWorkMapperImpl();
    }
}
