package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionType.class);
        QuestionType questionType1 = new QuestionType();
        questionType1.setId(1L);
        QuestionType questionType2 = new QuestionType();
        questionType2.setId(questionType1.getId());
        assertThat(questionType1).isEqualTo(questionType2);
        questionType2.setId(2L);
        assertThat(questionType1).isNotEqualTo(questionType2);
        questionType1.setId(null);
        assertThat(questionType1).isNotEqualTo(questionType2);
    }
}
