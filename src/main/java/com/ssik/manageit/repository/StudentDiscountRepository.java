package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentDiscount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentDiscountRepository extends JpaRepository<StudentDiscount, Long>, JpaSpecificationExecutor<StudentDiscount> {}
