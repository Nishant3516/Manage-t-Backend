package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassLessionPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassLessionPlan.class);
        ClassLessionPlan classLessionPlan1 = new ClassLessionPlan();
        classLessionPlan1.setId(1L);
        ClassLessionPlan classLessionPlan2 = new ClassLessionPlan();
        classLessionPlan2.setId(classLessionPlan1.getId());
        assertThat(classLessionPlan1).isEqualTo(classLessionPlan2);
        classLessionPlan2.setId(2L);
        assertThat(classLessionPlan1).isNotEqualTo(classLessionPlan2);
        classLessionPlan1.setId(null);
        assertThat(classLessionPlan1).isNotEqualTo(classLessionPlan2);
    }
}
