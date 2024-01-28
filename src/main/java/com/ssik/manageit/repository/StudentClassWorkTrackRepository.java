package com.ssik.manageit.repository;

import com.ssik.manageit.domain.StudentClassWorkTrack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentClassWorkTrack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentClassWorkTrackRepository
    extends JpaRepository<StudentClassWorkTrack, Long>, JpaSpecificationExecutor<StudentClassWorkTrack> {}
