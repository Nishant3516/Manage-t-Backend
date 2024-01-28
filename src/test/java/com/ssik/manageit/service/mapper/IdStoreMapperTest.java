package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdStoreMapperTest {

    private IdStoreMapper idStoreMapper;

    @BeforeEach
    public void setUp() {
        idStoreMapper = new IdStoreMapperImpl();
    }
}
