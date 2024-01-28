package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolEventDTO.class);
        SchoolEventDTO schoolEventDTO1 = new SchoolEventDTO();
        schoolEventDTO1.setId(1L);
        SchoolEventDTO schoolEventDTO2 = new SchoolEventDTO();
        assertThat(schoolEventDTO1).isNotEqualTo(schoolEventDTO2);
        schoolEventDTO2.setId(schoolEventDTO1.getId());
        assertThat(schoolEventDTO1).isEqualTo(schoolEventDTO2);
        schoolEventDTO2.setId(2L);
        assertThat(schoolEventDTO1).isNotEqualTo(schoolEventDTO2);
        schoolEventDTO1.setId(null);
        assertThat(schoolEventDTO1).isNotEqualTo(schoolEventDTO2);
    }
}
