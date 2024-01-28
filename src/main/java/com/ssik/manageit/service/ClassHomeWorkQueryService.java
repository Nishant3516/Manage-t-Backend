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
import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.domain.ClassHomeWork_;
import com.ssik.manageit.domain.StudentHomeWorkTrack_;
import com.ssik.manageit.repository.ClassHomeWorkRepository;
import com.ssik.manageit.service.criteria.ClassHomeWorkCriteria;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import com.ssik.manageit.service.mapper.ClassHomeWorkMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ClassHomeWork} entities in
 * the database. The main input is a {@link ClassHomeWorkCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassHomeWorkDTO} or a {@link Page} of
 * {@link ClassHomeWorkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassHomeWorkQueryService extends QueryService<ClassHomeWork> {

	private final Logger log = LoggerFactory.getLogger(ClassHomeWorkQueryService.class);

	private final ClassHomeWorkRepository classHomeWorkRepository;

	private final ClassHomeWorkMapper classHomeWorkMapper;

	public ClassHomeWorkQueryService(ClassHomeWorkRepository classHomeWorkRepository,
			ClassHomeWorkMapper classHomeWorkMapper) {
		this.classHomeWorkRepository = classHomeWorkRepository;
		this.classHomeWorkMapper = classHomeWorkMapper;
	}

	/**
	 * Return a {@link List} of {@link ClassHomeWorkDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassHomeWork> findByCriteria(ClassHomeWorkCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<ClassHomeWork> specification = createSpecification(criteria);
		return classHomeWorkRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link ClassHomeWorkDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassHomeWorkDTO> findByCriteria(ClassHomeWorkCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<ClassHomeWork> specification = createSpecification(criteria);
		return classHomeWorkRepository.findAll(specification, page).map(classHomeWorkMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ClassHomeWorkCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ClassHomeWork> specification = createSpecification(criteria);
		return classHomeWorkRepository.count(specification);
	}

	/**
	 * Function to convert {@link ClassHomeWorkCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ClassHomeWork> createSpecification(ClassHomeWorkCriteria criteria) {
		Specification<ClassHomeWork> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ClassHomeWork_.id));
			}
			if (criteria.getSchoolDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getSchoolDate(), ClassHomeWork_.schoolDate));
			}
			if (criteria.getStudentAssignmentType() != null) {
				specification = specification.and(
						buildSpecification(criteria.getStudentAssignmentType(), ClassHomeWork_.studentAssignmentType));
			}
			if (criteria.getHomeWorkText() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getHomeWorkText(), ClassHomeWork_.homeWorkText));
			}
			if (criteria.getHomeWorkFileLink() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getHomeWorkFileLink(), ClassHomeWork_.homeWorkFileLink));
			}
			if (criteria.getAssign() != null) {
				specification = specification.and(buildSpecification(criteria.getAssign(), ClassHomeWork_.assign));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ClassHomeWork_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ClassHomeWork_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ClassHomeWork_.cancelDate));
			}
			if (criteria.getStudentHomeWorkTrackId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentHomeWorkTrackId(), root -> root
						.join(ClassHomeWork_.studentHomeWorkTracks, JoinType.LEFT).get(StudentHomeWorkTrack_.id)));
			}
			if (criteria.getChapterSectionId() != null) {
				specification = specification.and(buildSpecification(criteria.getChapterSectionId(),
						root -> root.join(ClassHomeWork_.chapterSection, JoinType.LEFT).get(ChapterSection_.id)));
			}
		}
		return specification;
	}
}
