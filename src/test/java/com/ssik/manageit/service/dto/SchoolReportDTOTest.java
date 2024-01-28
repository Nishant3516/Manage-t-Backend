package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolReportDTO.class);
        SchoolReportDTO schoolReportDTO1 = new SchoolReportDTO();
        schoolReportDTO1.setId(1L);
        SchoolReportDTO schoolReportDTO2 = new SchoolReportDTO();
        assertThat(schoolReportDTO1).isNotEqualTo(schoolReportDTO2);
        schoolReportDTO2.setId(schoolReportDTO1.getId());
        assertThat(schoolReportDTO1).isEqualTo(schoolReportDTO2);
        schoolReportDTO2.setId(2L);
        assertThat(schoolReportDTO1).isNotEqualTo(schoolReportDTO2);
        schoolReportDTO1.setId(null);
        assertThat(schoolReportDTO1).isNotEqualTo(schoolReportDTO2);
    }
}
