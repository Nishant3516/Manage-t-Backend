package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdStoreDTO.class);
        IdStoreDTO idStoreDTO1 = new IdStoreDTO();
        idStoreDTO1.setId(1L);
        IdStoreDTO idStoreDTO2 = new IdStoreDTO();
        assertThat(idStoreDTO1).isNotEqualTo(idStoreDTO2);
        idStoreDTO2.setId(idStoreDTO1.getId());
        assertThat(idStoreDTO1).isEqualTo(idStoreDTO2);
        idStoreDTO2.setId(2L);
        assertThat(idStoreDTO1).isNotEqualTo(idStoreDTO2);
        idStoreDTO1.setId(null);
        assertThat(idStoreDTO1).isNotEqualTo(idStoreDTO2);
    }
}
