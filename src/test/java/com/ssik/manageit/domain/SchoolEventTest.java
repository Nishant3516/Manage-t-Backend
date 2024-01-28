package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolEvent.class);
        SchoolEvent schoolEvent1 = new SchoolEvent();
        schoolEvent1.setId(1L);
        SchoolEvent schoolEvent2 = new SchoolEvent();
        schoolEvent2.setId(schoolEvent1.getId());
        assertThat(schoolEvent1).isEqualTo(schoolEvent2);
        schoolEvent2.setId(2L);
        assertThat(schoolEvent1).isNotEqualTo(schoolEvent2);
        schoolEvent1.setId(null);
        assertThat(schoolEvent1).isNotEqualTo(schoolEvent2);
    }
}
