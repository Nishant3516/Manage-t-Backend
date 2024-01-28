package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentHomeWorkTrackMapperTest {

    private StudentHomeWorkTrackMapper studentHomeWorkTrackMapper;

    @BeforeEach
    public void setUp() {
        studentHomeWorkTrackMapper = new StudentHomeWorkTrackMapperImpl();
    }
}
