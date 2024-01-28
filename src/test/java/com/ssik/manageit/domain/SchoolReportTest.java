package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolReport.class);
        SchoolReport schoolReport1 = new SchoolReport();
        schoolReport1.setId(1L);
        SchoolReport schoolReport2 = new SchoolReport();
        schoolReport2.setId(schoolReport1.getId());
        assertThat(schoolReport1).isEqualTo(schoolReport2);
        schoolReport2.setId(2L);
        assertThat(schoolReport1).isNotEqualTo(schoolReport2);
        schoolReport1.setId(null);
        assertThat(schoolReport1).isNotEqualTo(schoolReport2);
    }
}
