package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolVideoGalleryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolVideoGalleryDTO.class);
        SchoolVideoGalleryDTO schoolVideoGalleryDTO1 = new SchoolVideoGalleryDTO();
        schoolVideoGalleryDTO1.setId(1L);
        SchoolVideoGalleryDTO schoolVideoGalleryDTO2 = new SchoolVideoGalleryDTO();
        assertThat(schoolVideoGalleryDTO1).isNotEqualTo(schoolVideoGalleryDTO2);
        schoolVideoGalleryDTO2.setId(schoolVideoGalleryDTO1.getId());
        assertThat(schoolVideoGalleryDTO1).isEqualTo(schoolVideoGalleryDTO2);
        schoolVideoGalleryDTO2.setId(2L);
        assertThat(schoolVideoGalleryDTO1).isNotEqualTo(schoolVideoGalleryDTO2);
        schoolVideoGalleryDTO1.setId(null);
        assertThat(schoolVideoGalleryDTO1).isNotEqualTo(schoolVideoGalleryDTO2);
    }
}
