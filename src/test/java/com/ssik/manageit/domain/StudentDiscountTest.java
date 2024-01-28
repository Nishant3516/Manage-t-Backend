package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentDiscountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentDiscount.class);
        StudentDiscount studentDiscount1 = new StudentDiscount();
        studentDiscount1.setId(1L);
        StudentDiscount studentDiscount2 = new StudentDiscount();
        studentDiscount2.setId(studentDiscount1.getId());
        assertThat(studentDiscount1).isEqualTo(studentDiscount2);
        studentDiscount2.setId(2L);
        assertThat(studentDiscount1).isNotEqualTo(studentDiscount2);
        studentDiscount1.setId(null);
        assertThat(studentDiscount1).isNotEqualTo(studentDiscount2);
    }
}
