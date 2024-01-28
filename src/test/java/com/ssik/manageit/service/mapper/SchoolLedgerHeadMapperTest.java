package com.ssik.manageit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolLedgerHeadMapperTest {

    private SchoolLedgerHeadMapper schoolLedgerHeadMapper;

    @BeforeEach
    public void setUp() {
        schoolLedgerHeadMapper = new SchoolLedgerHeadMapperImpl();
    }
}
