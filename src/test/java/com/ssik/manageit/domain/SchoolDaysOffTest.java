package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolDaysOffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolDaysOff.class);
        SchoolDaysOff schoolDaysOff1 = new SchoolDaysOff();
        schoolDaysOff1.setId(1L);
        SchoolDaysOff schoolDaysOff2 = new SchoolDaysOff();
        schoolDaysOff2.setId(schoolDaysOff1.getId());
        assertThat(schoolDaysOff1).isEqualTo(schoolDaysOff2);
        schoolDaysOff2.setId(2L);
        assertThat(schoolDaysOff1).isNotEqualTo(schoolDaysOff2);
        schoolDaysOff1.setId(null);
        assertThat(schoolDaysOff1).isNotEqualTo(schoolDaysOff2);
    }
}
