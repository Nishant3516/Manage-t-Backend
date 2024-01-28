package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolReport entity.
 */
@Repository
public interface SchoolReportRepository extends JpaRepository<SchoolReport, Long>, JpaSpecificationExecutor<SchoolReport> {
    @Query(
        value = "select distinct schoolReport from SchoolReport schoolReport left join fetch schoolReport.schoolClasses",
        countQuery = "select count(distinct schoolReport) from SchoolReport schoolReport"
    )
    Page<SchoolReport> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct schoolReport from SchoolReport schoolReport left join fetch schoolReport.schoolClasses")
    List<SchoolReport> findAllWithEagerRelationships();

    @Query("select schoolReport from SchoolReport schoolReport left join fetch schoolReport.schoolClasses where schoolReport.id =:id")
    Optional<SchoolReport> findOneWithEagerRelationships(@Param("id") Long id);
}
