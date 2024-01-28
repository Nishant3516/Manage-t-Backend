package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassHomeWorkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassHomeWork.class);
        ClassHomeWork classHomeWork1 = new ClassHomeWork();
        classHomeWork1.setId(1L);
        ClassHomeWork classHomeWork2 = new ClassHomeWork();
        classHomeWork2.setId(classHomeWork1.getId());
        assertThat(classHomeWork1).isEqualTo(classHomeWork2);
        classHomeWork2.setId(2L);
        assertThat(classHomeWork1).isNotEqualTo(classHomeWork2);
        classHomeWork1.setId(null);
        assertThat(classHomeWork1).isNotEqualTo(classHomeWork2);
    }
}
