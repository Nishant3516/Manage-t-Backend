package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolUser entity.
 */
@Repository
public interface SchoolUserRepository extends JpaRepository<SchoolUser, Long>, JpaSpecificationExecutor<SchoolUser> {
    @Query(
        value = "select distinct schoolUser from SchoolUser schoolUser left join fetch schoolUser.schoolClasses left join fetch schoolUser.classSubjects",
        countQuery = "select count(distinct schoolUser) from SchoolUser schoolUser"
    )
    Page<SchoolUser> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct schoolUser from SchoolUser schoolUser left join fetch schoolUser.schoolClasses left join fetch schoolUser.classSubjects"
    )
    List<SchoolUser> findAllWithEagerRelationships();

    @Query(
        "select schoolUser from SchoolUser schoolUser left join fetch schoolUser.schoolClasses left join fetch schoolUser.classSubjects where schoolUser.id =:id"
    )
    Optional<SchoolUser> findOneWithEagerRelationships(@Param("id") Long id);
}
