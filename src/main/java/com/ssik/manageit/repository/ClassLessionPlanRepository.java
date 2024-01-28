package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassLessionPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassLessionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassLessionPlanRepository extends JpaRepository<ClassLessionPlan, Long>, JpaSpecificationExecutor<ClassLessionPlan> {}
