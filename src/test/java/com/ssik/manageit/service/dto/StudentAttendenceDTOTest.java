package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAttendenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAttendenceDTO.class);
        StudentAttendenceDTO studentAttendenceDTO1 = new StudentAttendenceDTO();
        studentAttendenceDTO1.setId(1L);
        StudentAttendenceDTO studentAttendenceDTO2 = new StudentAttendenceDTO();
        assertThat(studentAttendenceDTO1).isNotEqualTo(studentAttendenceDTO2);
        studentAttendenceDTO2.setId(studentAttendenceDTO1.getId());
        assertThat(studentAttendenceDTO1).isEqualTo(studentAttendenceDTO2);
        studentAttendenceDTO2.setId(2L);
        assertThat(studentAttendenceDTO1).isNotEqualTo(studentAttendenceDTO2);
        studentAttendenceDTO1.setId(null);
        assertThat(studentAttendenceDTO1).isNotEqualTo(studentAttendenceDTO2);
    }
}
