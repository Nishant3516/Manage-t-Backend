package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentClassWorkTrackMapperTest {

    private StudentClassWorkTrackMapper studentClassWorkTrackMapper;

    @BeforeEach
    public void setUp() {
        studentClassWorkTrackMapper = new StudentClassWorkTrackMapperImpl();
    }
}
