package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolEvent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolEvent entity.
 */
@Repository
public interface SchoolEventRepository extends JpaRepository<SchoolEvent, Long>, JpaSpecificationExecutor<SchoolEvent> {
    @Query(
        value = "select distinct schoolEvent from SchoolEvent schoolEvent left join fetch schoolEvent.schoolClasses",
        countQuery = "select count(distinct schoolEvent) from SchoolEvent schoolEvent"
    )
    Page<SchoolEvent> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct schoolEvent from SchoolEvent schoolEvent left join fetch schoolEvent.schoolClasses")
    List<SchoolEvent> findAllWithEagerRelationships();

    @Query("select schoolEvent from SchoolEvent schoolEvent left join fetch schoolEvent.schoolClasses where schoolEvent.id =:id")
    Optional<SchoolEvent> findOneWithEagerRelationships(@Param("id") Long id);
}
