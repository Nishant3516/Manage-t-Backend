package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentPaymentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPayments.class);
        StudentPayments studentPayments1 = new StudentPayments();
        studentPayments1.setId(1L);
        StudentPayments studentPayments2 = new StudentPayments();
        studentPayments2.setId(studentPayments1.getId());
        assertThat(studentPayments1).isEqualTo(studentPayments2);
        studentPayments2.setId(2L);
        assertThat(studentPayments1).isNotEqualTo(studentPayments2);
        studentPayments1.setId(null);
        assertThat(studentPayments1).isNotEqualTo(studentPayments2);
    }
}
