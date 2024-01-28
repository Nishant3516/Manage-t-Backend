package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassStudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassStudent.class);
        ClassStudent classStudent1 = new ClassStudent();
        classStudent1.setId(1L);
        ClassStudent classStudent2 = new ClassStudent();
        classStudent2.setId(classStudent1.getId());
        assertThat(classStudent1).isEqualTo(classStudent2);
        classStudent2.setId(2L);
        assertThat(classStudent1).isNotEqualTo(classStudent2);
        classStudent1.setId(null);
        assertThat(classStudent1).isNotEqualTo(classStudent2);
    }
}
