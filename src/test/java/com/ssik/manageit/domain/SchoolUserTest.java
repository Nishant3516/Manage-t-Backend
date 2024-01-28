package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolUser.class);
        SchoolUser schoolUser1 = new SchoolUser();
        schoolUser1.setId(1L);
        SchoolUser schoolUser2 = new SchoolUser();
        schoolUser2.setId(schoolUser1.getId());
        assertThat(schoolUser1).isEqualTo(schoolUser2);
        schoolUser2.setId(2L);
        assertThat(schoolUser1).isNotEqualTo(schoolUser2);
        schoolUser1.setId(null);
        assertThat(schoolUser1).isNotEqualTo(schoolUser2);
    }
}
