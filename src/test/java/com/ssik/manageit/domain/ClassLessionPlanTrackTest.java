package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassLessionPlanTrackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassLessionPlanTrack.class);
        ClassLessionPlanTrack classLessionPlanTrack1 = new ClassLessionPlanTrack();
        classLessionPlanTrack1.setId(1L);
        ClassLessionPlanTrack classLessionPlanTrack2 = new ClassLessionPlanTrack();
        classLessionPlanTrack2.setId(classLessionPlanTrack1.getId());
        assertThat(classLessionPlanTrack1).isEqualTo(classLessionPlanTrack2);
        classLessionPlanTrack2.setId(2L);
        assertThat(classLessionPlanTrack1).isNotEqualTo(classLessionPlanTrack2);
        classLessionPlanTrack1.setId(null);
        assertThat(classLessionPlanTrack1).isNotEqualTo(classLessionPlanTrack2);
    }
}
