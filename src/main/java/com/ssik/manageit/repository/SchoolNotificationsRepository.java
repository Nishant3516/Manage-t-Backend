package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolNotifications;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolNotifications entity.
 */
@Repository
public interface SchoolNotificationsRepository
    extends JpaRepository<SchoolNotifications, Long>, JpaSpecificationExecutor<SchoolNotifications> {
    @Query(
        value = "select distinct schoolNotifications from SchoolNotifications schoolNotifications left join fetch schoolNotifications.schoolClasses",
        countQuery = "select count(distinct schoolNotifications) from SchoolNotifications schoolNotifications"
    )
    Page<SchoolNotifications> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct schoolNotifications from SchoolNotifications schoolNotifications left join fetch schoolNotifications.schoolClasses"
    )
    List<SchoolNotifications> findAllWithEagerRelationships();

    @Query(
        "select schoolNotifications from SchoolNotifications schoolNotifications left join fetch schoolNotifications.schoolClasses where schoolNotifications.id =:id"
    )
    Optional<SchoolNotifications> findOneWithEagerRelationships(@Param("id") Long id);
}
