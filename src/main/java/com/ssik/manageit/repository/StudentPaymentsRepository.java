package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentPayments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentPayments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentPaymentsRepository extends JpaRepository<StudentPayments, Long>, JpaSpecificationExecutor<StudentPayments> {}
