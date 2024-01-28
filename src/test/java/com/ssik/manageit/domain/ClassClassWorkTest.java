package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassClassWorkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassClassWork.class);
        ClassClassWork classClassWork1 = new ClassClassWork();
        classClassWork1.setId(1L);
        ClassClassWork classClassWork2 = new ClassClassWork();
        classClassWork2.setId(classClassWork1.getId());
        assertThat(classClassWork1).isEqualTo(classClassWork2);
        classClassWork2.setId(2L);
        assertThat(classClassWork1).isNotEqualTo(classClassWork2);
        classClassWork1.setId(null);
        assertThat(classClassWork1).isNotEqualTo(classClassWork2);
    }
}
