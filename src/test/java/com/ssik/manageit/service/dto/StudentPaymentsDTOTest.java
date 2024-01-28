package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentPaymentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPaymentsDTO.class);
        StudentPaymentsDTO studentPaymentsDTO1 = new StudentPaymentsDTO();
        studentPaymentsDTO1.setId(1L);
        StudentPaymentsDTO studentPaymentsDTO2 = new StudentPaymentsDTO();
        assertThat(studentPaymentsDTO1).isNotEqualTo(studentPaymentsDTO2);
        studentPaymentsDTO2.setId(studentPaymentsDTO1.getId());
        assertThat(studentPaymentsDTO1).isEqualTo(studentPaymentsDTO2);
        studentPaymentsDTO2.setId(2L);
        assertThat(studentPaymentsDTO1).isNotEqualTo(studentPaymentsDTO2);
        studentPaymentsDTO1.setId(null);
        assertThat(studentPaymentsDTO1).isNotEqualTo(studentPaymentsDTO2);
    }
}
