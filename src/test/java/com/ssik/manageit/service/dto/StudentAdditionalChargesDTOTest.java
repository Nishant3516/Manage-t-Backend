package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAdditionalChargesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAdditionalChargesDTO.class);
        StudentAdditionalChargesDTO studentAdditionalChargesDTO1 = new StudentAdditionalChargesDTO();
        studentAdditionalChargesDTO1.setId(1L);
        StudentAdditionalChargesDTO studentAdditionalChargesDTO2 = new StudentAdditionalChargesDTO();
        assertThat(studentAdditionalChargesDTO1).isNotEqualTo(studentAdditionalChargesDTO2);
        studentAdditionalChargesDTO2.setId(studentAdditionalChargesDTO1.getId());
        assertThat(studentAdditionalChargesDTO1).isEqualTo(studentAdditionalChargesDTO2);
        studentAdditionalChargesDTO2.setId(2L);
        assertThat(studentAdditionalChargesDTO1).isNotEqualTo(studentAdditionalChargesDTO2);
        studentAdditionalChargesDTO1.setId(null);
        assertThat(studentAdditionalChargesDTO1).isNotEqualTo(studentAdditionalChargesDTO2);
    }
}
