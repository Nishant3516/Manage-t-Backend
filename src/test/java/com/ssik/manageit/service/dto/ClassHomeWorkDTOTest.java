package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassHomeWorkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassHomeWorkDTO.class);
        ClassHomeWorkDTO classHomeWorkDTO1 = new ClassHomeWorkDTO();
        classHomeWorkDTO1.setId(1L);
        ClassHomeWorkDTO classHomeWorkDTO2 = new ClassHomeWorkDTO();
        assertThat(classHomeWorkDTO1).isNotEqualTo(classHomeWorkDTO2);
        classHomeWorkDTO2.setId(classHomeWorkDTO1.getId());
        assertThat(classHomeWorkDTO1).isEqualTo(classHomeWorkDTO2);
        classHomeWorkDTO2.setId(2L);
        assertThat(classHomeWorkDTO1).isNotEqualTo(classHomeWorkDTO2);
        classHomeWorkDTO1.setId(null);
        assertThat(classHomeWorkDTO1).isNotEqualTo(classHomeWorkDTO2);
    }
}
