package com.ssik.manageit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolNotificationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolNotifications.class);
        SchoolNotifications schoolNotifications1 = new SchoolNotifications();
        schoolNotifications1.setId(1L);
        SchoolNotifications schoolNotifications2 = new SchoolNotifications();
        schoolNotifications2.setId(schoolNotifications1.getId());
        assertThat(schoolNotifications1).isEqualTo(schoolNotifications2);
        schoolNotifications2.setId(2L);
        assertThat(schoolNotifications1).isNotEqualTo(schoolNotifications2);
        schoolNotifications1.setId(null);
        assertThat(schoolNotifications1).isNotEqualTo(schoolNotifications2);
    }
}
