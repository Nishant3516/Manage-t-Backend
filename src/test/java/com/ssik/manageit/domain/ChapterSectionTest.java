package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChapterSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChapterSection.class);
        ChapterSection chapterSection1 = new ChapterSection();
        chapterSection1.setId(1L);
        ChapterSection chapterSection2 = new ChapterSection();
        chapterSection2.setId(chapterSection1.getId());
        assertThat(chapterSection1).isEqualTo(chapterSection2);
        chapterSection2.setId(2L);
        assertThat(chapterSection1).isNotEqualTo(chapterSection2);
        chapterSection1.setId(null);
        assertThat(chapterSection1).isNotEqualTo(chapterSection2);
    }
}
