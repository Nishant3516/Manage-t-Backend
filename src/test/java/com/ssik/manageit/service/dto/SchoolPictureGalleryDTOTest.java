package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolPictureGalleryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolPictureGalleryDTO.class);
        SchoolPictureGalleryDTO schoolPictureGalleryDTO1 = new SchoolPictureGalleryDTO();
        schoolPictureGalleryDTO1.setId(1L);
        SchoolPictureGalleryDTO schoolPictureGalleryDTO2 = new SchoolPictureGalleryDTO();
        assertThat(schoolPictureGalleryDTO1).isNotEqualTo(schoolPictureGalleryDTO2);
        schoolPictureGalleryDTO2.setId(schoolPictureGalleryDTO1.getId());
        assertThat(schoolPictureGalleryDTO1).isEqualTo(schoolPictureGalleryDTO2);
        schoolPictureGalleryDTO2.setId(2L);
        assertThat(schoolPictureGalleryDTO1).isNotEqualTo(schoolPictureGalleryDTO2);
        schoolPictureGalleryDTO1.setId(null);
        assertThat(schoolPictureGalleryDTO1).isNotEqualTo(schoolPictureGalleryDTO2);
    }
}
