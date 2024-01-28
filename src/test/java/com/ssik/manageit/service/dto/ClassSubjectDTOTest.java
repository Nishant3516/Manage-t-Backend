package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassSubjectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassSubjectDTO.class);
        ClassSubjectDTO classSubjectDTO1 = new ClassSubjectDTO();
        classSubjectDTO1.setId(1L);
        ClassSubjectDTO classSubjectDTO2 = new ClassSubjectDTO();
        assertThat(classSubjectDTO1).isNotEqualTo(classSubjectDTO2);
        classSubjectDTO2.setId(classSubjectDTO1.getId());
        assertThat(classSubjectDTO1).isEqualTo(classSubjectDTO2);
        classSubjectDTO2.setId(2L);
        assertThat(classSubjectDTO1).isNotEqualTo(classSubjectDTO2);
        classSubjectDTO1.setId(null);
        assertThat(classSubjectDTO1).isNotEqualTo(classSubjectDTO2);
    }
}
