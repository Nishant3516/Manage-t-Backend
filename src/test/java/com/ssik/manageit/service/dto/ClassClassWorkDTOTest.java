package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassClassWorkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassClassWorkDTO.class);
        ClassClassWorkDTO classClassWorkDTO1 = new ClassClassWorkDTO();
        classClassWorkDTO1.setId(1L);
        ClassClassWorkDTO classClassWorkDTO2 = new ClassClassWorkDTO();
        assertThat(classClassWorkDTO1).isNotEqualTo(classClassWorkDTO2);
        classClassWorkDTO2.setId(classClassWorkDTO1.getId());
        assertThat(classClassWorkDTO1).isEqualTo(classClassWorkDTO2);
        classClassWorkDTO2.setId(2L);
        assertThat(classClassWorkDTO1).isNotEqualTo(classClassWorkDTO2);
        classClassWorkDTO1.setId(null);
        assertThat(classClassWorkDTO1).isNotEqualTo(classClassWorkDTO2);
    }
}
