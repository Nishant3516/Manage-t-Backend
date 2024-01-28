package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassLessionPlanTrack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassLessionPlanTrack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassLessionPlanTrackRepository
    extends JpaRepository<ClassLessionPlanTrack, Long>, JpaSpecificationExecutor<ClassLessionPlanTrack> {}
