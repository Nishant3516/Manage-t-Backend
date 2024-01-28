package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentHomeWorkTrackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentHomeWorkTrackDTO.class);
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO1 = new StudentHomeWorkTrackDTO();
        studentHomeWorkTrackDTO1.setId(1L);
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO2 = new StudentHomeWorkTrackDTO();
        assertThat(studentHomeWorkTrackDTO1).isNotEqualTo(studentHomeWorkTrackDTO2);
        studentHomeWorkTrackDTO2.setId(studentHomeWorkTrackDTO1.getId());
        assertThat(studentHomeWorkTrackDTO1).isEqualTo(studentHomeWorkTrackDTO2);
        studentHomeWorkTrackDTO2.setId(2L);
        assertThat(studentHomeWorkTrackDTO1).isNotEqualTo(studentHomeWorkTrackDTO2);
        studentHomeWorkTrackDTO1.setId(null);
        assertThat(studentHomeWorkTrackDTO1).isNotEqualTo(studentHomeWorkTrackDTO2);
    }
}
