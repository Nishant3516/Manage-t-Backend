package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentAttendence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentAttendence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentAttendenceRepository extends JpaRepository<StudentAttendence, Long>, JpaSpecificationExecutor<StudentAttendence> {}
