package com.ssik.manageit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.ChapterSection;

/**
 * Spring Data SQL repository for the ChapterSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChapterSectionRepository
		extends JpaRepository<ChapterSection, Long>, JpaSpecificationExecutor<ChapterSection> {

	@Query()
	List<ChapterSection> findBySectionName(String sectionName);

}
