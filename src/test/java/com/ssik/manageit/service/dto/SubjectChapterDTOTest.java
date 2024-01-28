package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubjectChapterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectChapterDTO.class);
        SubjectChapterDTO subjectChapterDTO1 = new SubjectChapterDTO();
        subjectChapterDTO1.setId(1L);
        SubjectChapterDTO subjectChapterDTO2 = new SubjectChapterDTO();
        assertThat(subjectChapterDTO1).isNotEqualTo(subjectChapterDTO2);
        subjectChapterDTO2.setId(subjectChapterDTO1.getId());
        assertThat(subjectChapterDTO1).isEqualTo(subjectChapterDTO2);
        subjectChapterDTO2.setId(2L);
        assertThat(subjectChapterDTO1).isNotEqualTo(subjectChapterDTO2);
        subjectChapterDTO1.setId(null);
        assertThat(subjectChapterDTO1).isNotEqualTo(subjectChapterDTO2);
    }
}
