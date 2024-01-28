package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolNotificationsMapperTest {

    private SchoolNotificationsMapper schoolNotificationsMapper;

    @BeforeEach
    public void setUp() {
        schoolNotificationsMapper = new SchoolNotificationsMapperImpl();
    }
}
