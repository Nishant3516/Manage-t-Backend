package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolClassDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolClassDTO.class);
        SchoolClassDTO schoolClassDTO1 = new SchoolClassDTO();
        schoolClassDTO1.setId(1L);
        SchoolClassDTO schoolClassDTO2 = new SchoolClassDTO();
        assertThat(schoolClassDTO1).isNotEqualTo(schoolClassDTO2);
        schoolClassDTO2.setId(schoolClassDTO1.getId());
        assertThat(schoolClassDTO1).isEqualTo(schoolClassDTO2);
        schoolClassDTO2.setId(2L);
        assertThat(schoolClassDTO1).isNotEqualTo(schoolClassDTO2);
        schoolClassDTO1.setId(null);
        assertThat(schoolClassDTO1).isNotEqualTo(schoolClassDTO2);
    }
}
