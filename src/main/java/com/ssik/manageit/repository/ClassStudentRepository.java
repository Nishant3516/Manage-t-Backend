package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassStudent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassStudent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long>, JpaSpecificationExecutor<ClassStudent> {
    ClassStudent findByStudentId(String studentId);
}
