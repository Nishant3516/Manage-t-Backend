package com.ssik.manageit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.SubjectChapter;

/**
 * Spring Data SQL repository for the SubjectChapter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectChapterRepository
		extends JpaRepository<SubjectChapter, Long>, JpaSpecificationExecutor<SubjectChapter> {

	@Query()
	List<SubjectChapter> findByChapterName(String chapterName);

}
