package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolUserDTO.class);
        SchoolUserDTO schoolUserDTO1 = new SchoolUserDTO();
        schoolUserDTO1.setId(1L);
        SchoolUserDTO schoolUserDTO2 = new SchoolUserDTO();
        assertThat(schoolUserDTO1).isNotEqualTo(schoolUserDTO2);
        schoolUserDTO2.setId(schoolUserDTO1.getId());
        assertThat(schoolUserDTO1).isEqualTo(schoolUserDTO2);
        schoolUserDTO2.setId(2L);
        assertThat(schoolUserDTO1).isNotEqualTo(schoolUserDTO2);
        schoolUserDTO1.setId(null);
        assertThat(schoolUserDTO1).isNotEqualTo(schoolUserDTO2);
    }
}
