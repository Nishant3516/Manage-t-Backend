package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolLedgerHeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolLedgerHead.class);
        SchoolLedgerHead schoolLedgerHead1 = new SchoolLedgerHead();
        schoolLedgerHead1.setId(1L);
        SchoolLedgerHead schoolLedgerHead2 = new SchoolLedgerHead();
        schoolLedgerHead2.setId(schoolLedgerHead1.getId());
        assertThat(schoolLedgerHead1).isEqualTo(schoolLedgerHead2);
        schoolLedgerHead2.setId(2L);
        assertThat(schoolLedgerHead1).isNotEqualTo(schoolLedgerHead2);
        schoolLedgerHead1.setId(null);
        assertThat(schoolLedgerHead1).isNotEqualTo(schoolLedgerHead2);
    }
}
