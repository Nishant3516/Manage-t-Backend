package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassLessionPlanTrackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassLessionPlanTrackDTO.class);
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO1 = new ClassLessionPlanTrackDTO();
        classLessionPlanTrackDTO1.setId(1L);
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO2 = new ClassLessionPlanTrackDTO();
        assertThat(classLessionPlanTrackDTO1).isNotEqualTo(classLessionPlanTrackDTO2);
        classLessionPlanTrackDTO2.setId(classLessionPlanTrackDTO1.getId());
        assertThat(classLessionPlanTrackDTO1).isEqualTo(classLessionPlanTrackDTO2);
        classLessionPlanTrackDTO2.setId(2L);
        assertThat(classLessionPlanTrackDTO1).isNotEqualTo(classLessionPlanTrackDTO2);
        classLessionPlanTrackDTO1.setId(null);
        assertThat(classLessionPlanTrackDTO1).isNotEqualTo(classLessionPlanTrackDTO2);
    }
}
