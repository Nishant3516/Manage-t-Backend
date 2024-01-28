package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassFeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassFee.class);
        ClassFee classFee1 = new ClassFee();
        classFee1.setId(1L);
        ClassFee classFee2 = new ClassFee();
        classFee2.setId(classFee1.getId());
        assertThat(classFee1).isEqualTo(classFee2);
        classFee2.setId(2L);
        assertThat(classFee1).isNotEqualTo(classFee2);
        classFee1.setId(null);
        assertThat(classFee1).isNotEqualTo(classFee2);
    }
}
