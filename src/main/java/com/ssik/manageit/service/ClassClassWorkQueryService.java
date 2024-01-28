package com.ssik.manageit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import com.ssik.manageit.domain.ChapterSection_;
import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.domain.ClassClassWork_;
import com.ssik.manageit.domain.StudentClassWorkTrack_;
import com.ssik.manageit.repository.ClassClassWorkRepository;
import com.ssik.manageit.service.criteria.ClassClassWorkCriteria;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import com.ssik.manageit.service.mapper.ClassClassWorkMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ClassClassWork} entities in
 * the database. The main input is a {@link ClassClassWorkCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassClassWorkDTO} or a {@link Page} of
 * {@link ClassClassWorkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassClassWorkQueryService extends QueryService<ClassClassWork> {

	private final Logger log = LoggerFactory.getLogger(ClassClassWorkQueryService.class);

	private final ClassClassWorkRepository classClassWorkRepository;

	private final ClassClassWorkMapper classClassWorkMapper;

	public ClassClassWorkQueryService(ClassClassWorkRepository classClassWorkRepository,
			ClassClassWorkMapper classClassWorkMapper) {
		this.classClassWorkRepository = classClassWorkRepository;
		this.classClassWorkMapper = classClassWorkMapper;
	}

	/**
	 * Return a {@link List} of {@link ClassClassWorkDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassClassWork> findByCriteria(ClassClassWorkCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<ClassClassWork> specification = createSpecification(criteria);
		return classClassWorkRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link ClassClassWorkDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassClassWorkDTO> findByCriteria(ClassClassWorkCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<ClassClassWork> specification = createSpecification(criteria);
		return classClassWorkRepository.findAll(specification, page).map(classClassWorkMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ClassClassWorkCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ClassClassWork> specification = createSpecification(criteria);
		return classClassWorkRepository.count(specification);
	}

	/**
	 * Function to convert {@link ClassClassWorkCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ClassClassWork> createSpecification(ClassClassWorkCriteria criteria) {
		Specification<ClassClassWork> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ClassClassWork_.id));
			}
			if (criteria.getSchoolDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getSchoolDate(), ClassClassWork_.schoolDate));
			}
			if (criteria.getStudentAssignmentType() != null) {
				specification = specification.and(
						buildSpecification(criteria.getStudentAssignmentType(), ClassClassWork_.studentAssignmentType));
			}
			if (criteria.getClassWorkText() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getClassWorkText(), ClassClassWork_.classWorkText));
			}
			if (criteria.getClassWorkFileLink() != null) {
				specification = specification.and(
						buildStringSpecification(criteria.getClassWorkFileLink(), ClassClassWork_.classWorkFileLink));
			}
			if (criteria.getAssign() != null) {
				specification = specification.and(buildSpecification(criteria.getAssign(), ClassClassWork_.assign));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ClassClassWork_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ClassClassWork_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ClassClassWork_.cancelDate));
			}
			if (criteria.getStudentClassWorkTrackId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentClassWorkTrackId(), root -> root
						.join(ClassClassWork_.studentClassWorkTracks, JoinType.LEFT).get(StudentClassWorkTrack_.id)));
			}
			if (criteria.getChapterSectionId() != null) {
				specification = specification.and(buildSpecification(criteria.getChapterSectionId(),
						root -> root.join(ClassClassWork_.chapterSection, JoinType.LEFT).get(ChapterSection_.id)));
			}
		}
		return specification;
	}
}
