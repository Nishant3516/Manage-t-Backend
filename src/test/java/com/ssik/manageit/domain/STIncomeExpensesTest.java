package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class STIncomeExpensesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(STIncomeExpenses.class);
        STIncomeExpenses sTIncomeExpenses1 = new STIncomeExpenses();
        sTIncomeExpenses1.setId(1L);
        STIncomeExpenses sTIncomeExpenses2 = new STIncomeExpenses();
        sTIncomeExpenses2.setId(sTIncomeExpenses1.getId());
        assertThat(sTIncomeExpenses1).isEqualTo(sTIncomeExpenses2);
        sTIncomeExpenses2.setId(2L);
        assertThat(sTIncomeExpenses1).isNotEqualTo(sTIncomeExpenses2);
        sTIncomeExpenses1.setId(null);
        assertThat(sTIncomeExpenses1).isNotEqualTo(sTIncomeExpenses2);
    }
}
