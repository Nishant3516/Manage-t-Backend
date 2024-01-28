package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentDiscountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentDiscountDTO.class);
        StudentDiscountDTO studentDiscountDTO1 = new StudentDiscountDTO();
        studentDiscountDTO1.setId(1L);
        StudentDiscountDTO studentDiscountDTO2 = new StudentDiscountDTO();
        assertThat(studentDiscountDTO1).isNotEqualTo(studentDiscountDTO2);
        studentDiscountDTO2.setId(studentDiscountDTO1.getId());
        assertThat(studentDiscountDTO1).isEqualTo(studentDiscountDTO2);
        studentDiscountDTO2.setId(2L);
        assertThat(studentDiscountDTO1).isNotEqualTo(studentDiscountDTO2);
        studentDiscountDTO1.setId(null);
        assertThat(studentDiscountDTO1).isNotEqualTo(studentDiscountDTO2);
    }
}
