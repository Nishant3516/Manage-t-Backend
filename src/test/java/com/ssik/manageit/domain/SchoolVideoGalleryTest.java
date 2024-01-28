package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolVideoGalleryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolVideoGallery.class);
        SchoolVideoGallery schoolVideoGallery1 = new SchoolVideoGallery();
        schoolVideoGallery1.setId(1L);
        SchoolVideoGallery schoolVideoGallery2 = new SchoolVideoGallery();
        schoolVideoGallery2.setId(schoolVideoGallery1.getId());
        assertThat(schoolVideoGallery1).isEqualTo(schoolVideoGallery2);
        schoolVideoGallery2.setId(2L);
        assertThat(schoolVideoGallery1).isNotEqualTo(schoolVideoGallery2);
        schoolVideoGallery1.setId(null);
        assertThat(schoolVideoGallery1).isNotEqualTo(schoolVideoGallery2);
    }
}
