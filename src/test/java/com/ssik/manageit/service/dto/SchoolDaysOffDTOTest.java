package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolDaysOffDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolDaysOffDTO.class);
        SchoolDaysOffDTO schoolDaysOffDTO1 = new SchoolDaysOffDTO();
        schoolDaysOffDTO1.setId(1L);
        SchoolDaysOffDTO schoolDaysOffDTO2 = new SchoolDaysOffDTO();
        assertThat(schoolDaysOffDTO1).isNotEqualTo(schoolDaysOffDTO2);
        schoolDaysOffDTO2.setId(schoolDaysOffDTO1.getId());
        assertThat(schoolDaysOffDTO1).isEqualTo(schoolDaysOffDTO2);
        schoolDaysOffDTO2.setId(2L);
        assertThat(schoolDaysOffDTO1).isNotEqualTo(schoolDaysOffDTO2);
        schoolDaysOffDTO1.setId(null);
        assertThat(schoolDaysOffDTO1).isNotEqualTo(schoolDaysOffDTO2);
    }
}
