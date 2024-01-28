package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentHomeWorkTrackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentHomeWorkTrack.class);
        StudentHomeWorkTrack studentHomeWorkTrack1 = new StudentHomeWorkTrack();
        studentHomeWorkTrack1.setId(1L);
        StudentHomeWorkTrack studentHomeWorkTrack2 = new StudentHomeWorkTrack();
        studentHomeWorkTrack2.setId(studentHomeWorkTrack1.getId());
        assertThat(studentHomeWorkTrack1).isEqualTo(studentHomeWorkTrack2);
        studentHomeWorkTrack2.setId(2L);
        assertThat(studentHomeWorkTrack1).isNotEqualTo(studentHomeWorkTrack2);
        studentHomeWorkTrack1.setId(null);
        assertThat(studentHomeWorkTrack1).isNotEqualTo(studentHomeWorkTrack2);
    }
}
