package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionPaperTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionPaper.class);
        QuestionPaper questionPaper1 = new QuestionPaper();
        questionPaper1.setId(1L);
        QuestionPaper questionPaper2 = new QuestionPaper();
        questionPaper2.setId(questionPaper1.getId());
        assertThat(questionPaper1).isEqualTo(questionPaper2);
        questionPaper2.setId(2L);
        assertThat(questionPaper1).isNotEqualTo(questionPaper2);
        questionPaper1.setId(null);
        assertThat(questionPaper1).isNotEqualTo(questionPaper2);
    }
}
