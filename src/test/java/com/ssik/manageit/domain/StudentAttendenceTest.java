package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAttendenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAttendence.class);
        StudentAttendence studentAttendence1 = new StudentAttendence();
        studentAttendence1.setId(1L);
        StudentAttendence studentAttendence2 = new StudentAttendence();
        studentAttendence2.setId(studentAttendence1.getId());
        assertThat(studentAttendence1).isEqualTo(studentAttendence2);
        studentAttendence2.setId(2L);
        assertThat(studentAttendence1).isNotEqualTo(studentAttendence2);
        studentAttendence1.setId(null);
        assertThat(studentAttendence1).isNotEqualTo(studentAttendence2);
    }
}
