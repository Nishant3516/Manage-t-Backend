package com.ssik.manageit.repository;

import com.ssik.manageit.domain.SchoolPictureGallery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchoolPictureGallery entity.
 */
@Repository
public interface SchoolPictureGalleryRepository
    extends JpaRepository<SchoolPictureGallery, Long>, JpaSpecificationExecutor<SchoolPictureGallery> {
    @Query(
        value = "select distinct schoolPictureGallery from SchoolPictureGallery schoolPictureGallery left join fetch schoolPictureGallery.schoolClasses",
        countQuery = "select count(distinct schoolPictureGallery) from SchoolPictureGallery schoolPictureGallery"
    )
    Page<SchoolPictureGallery> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct schoolPictureGallery from SchoolPictureGallery schoolPictureGallery left join fetch schoolPictureGallery.schoolClasses"
    )
    List<SchoolPictureGallery> findAllWithEagerRelationships();

    @Query(
        "select schoolPictureGallery from SchoolPictureGallery schoolPictureGallery left join fetch schoolPictureGallery.schoolClasses where schoolPictureGallery.id =:id"
    )
    Optional<SchoolPictureGallery> findOneWithEagerRelationships(@Param("id") Long id);
}
