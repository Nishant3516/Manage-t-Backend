package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeExpensesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeExpenses.class);
        IncomeExpenses incomeExpenses1 = new IncomeExpenses();
        incomeExpenses1.setId(1L);
        IncomeExpenses incomeExpenses2 = new IncomeExpenses();
        incomeExpenses2.setId(incomeExpenses1.getId());
        assertThat(incomeExpenses1).isEqualTo(incomeExpenses2);
        incomeExpenses2.setId(2L);
        assertThat(incomeExpenses1).isNotEqualTo(incomeExpenses2);
        incomeExpenses1.setId(null);
        assertThat(incomeExpenses1).isNotEqualTo(incomeExpenses2);
    }
}
