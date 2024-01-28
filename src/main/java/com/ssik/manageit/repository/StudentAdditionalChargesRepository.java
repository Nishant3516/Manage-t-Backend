package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentAdditionalCharges;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentAdditionalCharges entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentAdditionalChargesRepository
    extends JpaRepository<StudentAdditionalCharges, Long>, JpaSpecificationExecutor<StudentAdditionalCharges> {}
