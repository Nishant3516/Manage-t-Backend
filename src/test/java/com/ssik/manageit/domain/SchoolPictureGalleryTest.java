package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolPictureGalleryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolPictureGallery.class);
        SchoolPictureGallery schoolPictureGallery1 = new SchoolPictureGallery();
        schoolPictureGallery1.setId(1L);
        SchoolPictureGallery schoolPictureGallery2 = new SchoolPictureGallery();
        schoolPictureGallery2.setId(schoolPictureGallery1.getId());
        assertThat(schoolPictureGallery1).isEqualTo(schoolPictureGallery2);
        schoolPictureGallery2.setId(2L);
        assertThat(schoolPictureGallery1).isNotEqualTo(schoolPictureGallery2);
        schoolPictureGallery1.setId(null);
        assertThat(schoolPictureGallery1).isNotEqualTo(schoolPictureGallery2);
    }
}
