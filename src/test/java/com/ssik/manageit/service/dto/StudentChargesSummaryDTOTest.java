package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentChargesSummaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentChargesSummaryDTO.class);
        StudentChargesSummaryDTO studentChargesSummaryDTO1 = new StudentChargesSummaryDTO();
        studentChargesSummaryDTO1.setId(1L);
        StudentChargesSummaryDTO studentChargesSummaryDTO2 = new StudentChargesSummaryDTO();
        assertThat(studentChargesSummaryDTO1).isNotEqualTo(studentChargesSummaryDTO2);
        studentChargesSummaryDTO2.setId(studentChargesSummaryDTO1.getId());
        assertThat(studentChargesSummaryDTO1).isEqualTo(studentChargesSummaryDTO2);
        studentChargesSummaryDTO2.setId(2L);
        assertThat(studentChargesSummaryDTO1).isNotEqualTo(studentChargesSummaryDTO2);
        studentChargesSummaryDTO1.setId(null);
        assertThat(studentChargesSummaryDTO1).isNotEqualTo(studentChargesSummaryDTO2);
    }
}
