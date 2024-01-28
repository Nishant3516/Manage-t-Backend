package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolLedgerHeadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolLedgerHeadDTO.class);
        SchoolLedgerHeadDTO schoolLedgerHeadDTO1 = new SchoolLedgerHeadDTO();
        schoolLedgerHeadDTO1.setId(1L);
        SchoolLedgerHeadDTO schoolLedgerHeadDTO2 = new SchoolLedgerHeadDTO();
        assertThat(schoolLedgerHeadDTO1).isNotEqualTo(schoolLedgerHeadDTO2);
        schoolLedgerHeadDTO2.setId(schoolLedgerHeadDTO1.getId());
        assertThat(schoolLedgerHeadDTO1).isEqualTo(schoolLedgerHeadDTO2);
        schoolLedgerHeadDTO2.setId(2L);
        assertThat(schoolLedgerHeadDTO1).isNotEqualTo(schoolLedgerHeadDTO2);
        schoolLedgerHeadDTO1.setId(null);
        assertThat(schoolLedgerHeadDTO1).isNotEqualTo(schoolLedgerHeadDTO2);
    }
}
