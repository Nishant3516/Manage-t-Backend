package com.ssik.manageit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssik.manageit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolNotificationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolNotificationsDTO.class);
        SchoolNotificationsDTO schoolNotificationsDTO1 = new SchoolNotificationsDTO();
        schoolNotificationsDTO1.setId(1L);
        SchoolNotificationsDTO schoolNotificationsDTO2 = new SchoolNotificationsDTO();
        assertThat(schoolNotificationsDTO1).isNotEqualTo(schoolNotificationsDTO2);
        schoolNotificationsDTO2.setId(schoolNotificationsDTO1.getId());
        assertThat(schoolNotificationsDTO1).isEqualTo(schoolNotificationsDTO2);
        schoolNotificationsDTO2.setId(2L);
        assertThat(schoolNotificationsDTO1).isNotEqualTo(schoolNotificationsDTO2);
        schoolNotificationsDTO1.setId(null);
        assertThat(schoolNotificationsDTO1).isNotEqualTo(schoolNotificationsDTO2);
    }
}
