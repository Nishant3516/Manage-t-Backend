package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentChargesSummaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentChargesSummary.class);
        StudentChargesSummary studentChargesSummary1 = new StudentChargesSummary();
        studentChargesSummary1.setId(1L);
        StudentChargesSummary studentChargesSummary2 = new StudentChargesSummary();
        studentChargesSummary2.setId(studentChargesSummary1.getId());
        assertThat(studentChargesSummary1).isEqualTo(studentChargesSummary2);
        studentChargesSummary2.setId(2L);
        assertThat(studentChargesSummary1).isNotEqualTo(studentChargesSummary2);
        studentChargesSummary1.setId(null);
        assertThat(studentChargesSummary1).isNotEqualTo(studentChargesSummary2);
    }
}
