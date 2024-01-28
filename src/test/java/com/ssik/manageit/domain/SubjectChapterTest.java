package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubjectChapterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectChapter.class);
        SubjectChapter subjectChapter1 = new SubjectChapter();
        subjectChapter1.setId(1L);
        SubjectChapter subjectChapter2 = new SubjectChapter();
        subjectChapter2.setId(subjectChapter1.getId());
        assertThat(subjectChapter1).isEqualTo(subjectChapter2);
        subjectChapter2.setId(2L);
        assertThat(subjectChapter1).isNotEqualTo(subjectChapter2);
        subjectChapter1.setId(null);
        assertThat(subjectChapter1).isNotEqualTo(subjectChapter2);
    }
}
