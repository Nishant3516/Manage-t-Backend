package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassSubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassSubject.class);
        ClassSubject classSubject1 = new ClassSubject();
        classSubject1.setId(1L);
        ClassSubject classSubject2 = new ClassSubject();
        classSubject2.setId(classSubject1.getId());
        assertThat(classSubject1).isEqualTo(classSubject2);
        classSubject2.setId(2L);
        assertThat(classSubject1).isNotEqualTo(classSubject2);
        classSubject1.setId(null);
        assertThat(classSubject1).isNotEqualTo(classSubject2);
    }
}
