package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassHomeWork;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassHomeWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassHomeWorkRepository extends JpaRepository<ClassHomeWork, Long>, JpaSpecificationExecutor<ClassHomeWork> {}
