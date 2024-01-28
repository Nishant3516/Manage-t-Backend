package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentClassWorkTrackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClassWorkTrack.class);
        StudentClassWorkTrack studentClassWorkTrack1 = new StudentClassWorkTrack();
        studentClassWorkTrack1.setId(1L);
        StudentClassWorkTrack studentClassWorkTrack2 = new StudentClassWorkTrack();
        studentClassWorkTrack2.setId(studentClassWorkTrack1.getId());
        assertThat(studentClassWorkTrack1).isEqualTo(studentClassWorkTrack2);
        studentClassWorkTrack2.setId(2L);
        assertThat(studentClassWorkTrack1).isNotEqualTo(studentClassWorkTrack2);
        studentClassWorkTrack1.setId(null);
        assertThat(studentClassWorkTrack1).isNotEqualTo(studentClassWorkTrack2);
    }
}
