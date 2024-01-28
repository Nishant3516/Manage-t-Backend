package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdStore.class);
        IdStore idStore1 = new IdStore();
        idStore1.setId(1L);
        IdStore idStore2 = new IdStore();
        idStore2.setId(idStore1.getId());
        assertThat(idStore1).isEqualTo(idStore2);
        idStore2.setId(2L);
        assertThat(idStore1).isNotEqualTo(idStore2);
        idStore1.setId(null);
        assertThat(idStore1).isNotEqualTo(idStore2);
    }
}
