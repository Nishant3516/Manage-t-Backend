package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassStudentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassStudentDTO.class);
        ClassStudentDTO classStudentDTO1 = new ClassStudentDTO();
        classStudentDTO1.setId(1L);
        ClassStudentDTO classStudentDTO2 = new ClassStudentDTO();
        assertThat(classStudentDTO1).isNotEqualTo(classStudentDTO2);
        classStudentDTO2.setId(classStudentDTO1.getId());
        assertThat(classStudentDTO1).isEqualTo(classStudentDTO2);
        classStudentDTO2.setId(2L);
        assertThat(classStudentDTO1).isNotEqualTo(classStudentDTO2);
        classStudentDTO1.setId(null);
        assertThat(classStudentDTO1).isNotEqualTo(classStudentDTO2);
    }
}
