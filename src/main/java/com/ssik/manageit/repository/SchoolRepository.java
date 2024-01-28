package com.ssik.manageit.repository;

import com.ssik.manageit.domain.School;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the School entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {
    @Query
    School findBySchoolName(String schoolName);
}
