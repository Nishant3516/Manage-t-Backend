package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAdditionalChargesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAdditionalCharges.class);
        StudentAdditionalCharges studentAdditionalCharges1 = new StudentAdditionalCharges();
        studentAdditionalCharges1.setId(1L);
        StudentAdditionalCharges studentAdditionalCharges2 = new StudentAdditionalCharges();
        studentAdditionalCharges2.setId(studentAdditionalCharges1.getId());
        assertThat(studentAdditionalCharges1).isEqualTo(studentAdditionalCharges2);
        studentAdditionalCharges2.setId(2L);
        assertThat(studentAdditionalCharges1).isNotEqualTo(studentAdditionalCharges2);
        studentAdditionalCharges1.setId(null);
        assertThat(studentAdditionalCharges1).isNotEqualTo(studentAdditionalCharges2);
    }
}
