package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class STRouteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(STRoute.class);
        STRoute sTRoute1 = new STRoute();
        sTRoute1.setId(1L);
        STRoute sTRoute2 = new STRoute();
        sTRoute2.setId(sTRoute1.getId());
        assertThat(sTRoute1).isEqualTo(sTRoute2);
        sTRoute2.setId(2L);
        assertThat(sTRoute1).isNotEqualTo(sTRoute2);
        sTRoute1.setId(null);
        assertThat(sTRoute1).isNotEqualTo(sTRoute2);
    }
}
