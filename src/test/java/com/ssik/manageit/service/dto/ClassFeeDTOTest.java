package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassFeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassFeeDTO.class);
        ClassFeeDTO classFeeDTO1 = new ClassFeeDTO();
        classFeeDTO1.setId(1L);
        ClassFeeDTO classFeeDTO2 = new ClassFeeDTO();
        assertThat(classFeeDTO1).isNotEqualTo(classFeeDTO2);
        classFeeDTO2.setId(classFeeDTO1.getId());
        assertThat(classFeeDTO1).isEqualTo(classFeeDTO2);
        classFeeDTO2.setId(2L);
        assertThat(classFeeDTO1).isNotEqualTo(classFeeDTO2);
        classFeeDTO1.setId(null);
        assertThat(classFeeDTO1).isNotEqualTo(classFeeDTO2);
    }
}
