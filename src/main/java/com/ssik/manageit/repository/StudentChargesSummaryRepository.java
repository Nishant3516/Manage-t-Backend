package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentChargesSummary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentChargesSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentChargesSummaryRepository
    extends JpaRepository<StudentChargesSummary, Long>, JpaSpecificationExecutor<StudentChargesSummary> {}
