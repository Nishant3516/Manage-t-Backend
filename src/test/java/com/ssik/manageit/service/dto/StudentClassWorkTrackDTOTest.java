package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentClassWorkTrackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClassWorkTrackDTO.class);
        StudentClassWorkTrackDTO studentClassWorkTrackDTO1 = new StudentClassWorkTrackDTO();
        studentClassWorkTrackDTO1.setId(1L);
        StudentClassWorkTrackDTO studentClassWorkTrackDTO2 = new StudentClassWorkTrackDTO();
        assertThat(studentClassWorkTrackDTO1).isNotEqualTo(studentClassWorkTrackDTO2);
        studentClassWorkTrackDTO2.setId(studentClassWorkTrackDTO1.getId());
        assertThat(studentClassWorkTrackDTO1).isEqualTo(studentClassWorkTrackDTO2);
        studentClassWorkTrackDTO2.setId(2L);
        assertThat(studentClassWorkTrackDTO1).isNotEqualTo(studentClassWorkTrackDTO2);
        studentClassWorkTrackDTO1.setId(null);
        assertThat(studentClassWorkTrackDTO1).isNotEqualTo(studentClassWorkTrackDTO2);
    }
}
