package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChapterSectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChapterSectionDTO.class);
        ChapterSectionDTO chapterSectionDTO1 = new ChapterSectionDTO();
        chapterSectionDTO1.setId(1L);
        ChapterSectionDTO chapterSectionDTO2 = new ChapterSectionDTO();
        assertThat(chapterSectionDTO1).isNotEqualTo(chapterSectionDTO2);
        chapterSectionDTO2.setId(chapterSectionDTO1.getId());
        assertThat(chapterSectionDTO1).isEqualTo(chapterSectionDTO2);
        chapterSectionDTO2.setId(2L);
        assertThat(chapterSectionDTO1).isNotEqualTo(chapterSectionDTO2);
        chapterSectionDTO1.setId(null);
        assertThat(chapterSectionDTO1).isNotEqualTo(chapterSectionDTO2);
    }
}
