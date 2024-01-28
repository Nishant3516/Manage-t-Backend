package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassClassWork;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassClassWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassClassWorkRepository extends JpaRepository<ClassClassWork, Long>, JpaSpecificationExecutor<ClassClassWork> {}
