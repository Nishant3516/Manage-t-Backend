package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolDaysOff;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolDaysOff entity.
 */
@Repository
public interface SchoolDaysOffRepository extends JpaRepository<SchoolDaysOff, Long>, JpaSpecificationExecutor<SchoolDaysOff> {
    @Query(
        value = "select distinct schoolDaysOff from SchoolDaysOff schoolDaysOff left join fetch schoolDaysOff.schoolClasses",
        countQuery = "select count(distinct schoolDaysOff) from SchoolDaysOff schoolDaysOff"
    )
    Page<SchoolDaysOff> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct schoolDaysOff from SchoolDaysOff schoolDaysOff left join fetch schoolDaysOff.schoolClasses")
    List<SchoolDaysOff> findAllWithEagerRelationships();

    @Query("select schoolDaysOff from SchoolDaysOff schoolDaysOff left join fetch schoolDaysOff.schoolClasses where schoolDaysOff.id =:id")
    Optional<SchoolDaysOff> findOneWithEagerRelationships(@Param("id") Long id);
}
