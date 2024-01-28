package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendorsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendors.class);
        Vendors vendors1 = new Vendors();
        vendors1.setId(1L);
        Vendors vendors2 = new Vendors();
        vendors2.setId(vendors1.getId());
        assertThat(vendors1).isEqualTo(vendors2);
        vendors2.setId(2L);
        assertThat(vendors1).isNotEqualTo(vendors2);
        vendors1.setId(null);
        assertThat(vendors1).isNotEqualTo(vendors2);
    }
}
