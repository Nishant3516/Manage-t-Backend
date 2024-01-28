package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolVideoGallery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolVideoGallery entity.
 */
@Repository
public interface SchoolVideoGalleryRepository
    extends JpaRepository<SchoolVideoGallery, Long>, JpaSpecificationExecutor<SchoolVideoGallery> {
    @Query(
        value = "select distinct schoolVideoGallery from SchoolVideoGallery schoolVideoGallery left join fetch schoolVideoGallery.schoolClasses",
        countQuery = "select count(distinct schoolVideoGallery) from SchoolVideoGallery schoolVideoGallery"
    )
    Page<SchoolVideoGallery> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct schoolVideoGallery from SchoolVideoGallery schoolVideoGallery left join fetch schoolVideoGallery.schoolClasses")
    List<SchoolVideoGallery> findAllWithEagerRelationships();

    @Query(
        "select schoolVideoGallery from SchoolVideoGallery schoolVideoGallery left join fetch schoolVideoGallery.schoolClasses where schoolVideoGallery.id =:id"
    )
    Optional<SchoolVideoGallery> findOneWithEagerRelationships(@Param("id") Long id);
}
