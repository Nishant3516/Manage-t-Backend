package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassFeeMapperTest {

    private ClassFeeMapper classFeeMapper;

    @BeforeEach
    public void setUp() {
        classFeeMapper = new ClassFeeMapperImpl();
    }
}
