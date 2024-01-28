package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentHomeWorkTrack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentHomeWorkTrack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentHomeWorkTrackRepository
    extends JpaRepository<StudentHomeWorkTrack, Long>, JpaSpecificationExecutor<StudentHomeWorkTrack> {}
