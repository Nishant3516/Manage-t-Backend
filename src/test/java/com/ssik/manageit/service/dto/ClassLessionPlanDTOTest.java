package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassLessionPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassLessionPlanDTO.class);
        ClassLessionPlanDTO classLessionPlanDTO1 = new ClassLessionPlanDTO();
        classLessionPlanDTO1.setId(1L);
        ClassLessionPlanDTO classLessionPlanDTO2 = new ClassLessionPlanDTO();
        assertThat(classLessionPlanDTO1).isNotEqualTo(classLessionPlanDTO2);
        classLessionPlanDTO2.setId(classLessionPlanDTO1.getId());
        assertThat(classLessionPlanDTO1).isEqualTo(classLessionPlanDTO2);
        classLessionPlanDTO2.setId(2L);
        assertThat(classLessionPlanDTO1).isNotEqualTo(classLessionPlanDTO2);
        classLessionPlanDTO1.setId(null);
        assertThat(classLessionPlanDTO1).isNotEqualTo(classLessionPlanDTO2);
    }
}
