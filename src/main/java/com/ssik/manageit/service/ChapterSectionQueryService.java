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
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ChapterSection_;
import com.ssik.manageit.domain.ClassClassWork_;
import com.ssik.manageit.domain.ClassHomeWork_;
import com.ssik.manageit.domain.ClassLessionPlan_;
import com.ssik.manageit.domain.SubjectChapter_;
import com.ssik.manageit.repository.ChapterSectionRepository;
import com.ssik.manageit.service.criteria.ChapterSectionCriteria;
import com.ssik.manageit.service.dto.ChapterSectionDTO;
import com.ssik.manageit.service.mapper.ChapterSectionMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ChapterSection} entities in
 * the database. The main input is a {@link ChapterSectionCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChapterSectionDTO} or a {@link Page} of
 * {@link ChapterSectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChapterSectionQueryService extends QueryService<ChapterSection> {

	private final Logger log = LoggerFactory.getLogger(ChapterSectionQueryService.class);

	private final ChapterSectionRepository chapterSectionRepository;

	private final ChapterSectionMapper chapterSectionMapper;

	public ChapterSectionQueryService(ChapterSectionRepository chapterSectionRepository,
			ChapterSectionMapper chapterSectionMapper) {
		this.chapterSectionRepository = chapterSectionRepository;
		this.chapterSectionMapper = chapterSectionMapper;
	}

	/**
	 * Return a {@link List} of {@link ChapterSectionDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ChapterSectionDTO> findByCriteria(ChapterSectionCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<ChapterSection> specification = createSpecification(criteria);
		return chapterSectionMapper.toDto(chapterSectionRepository.findAll(specification));
	}

	/**
	 * Return a {@link Page} of {@link ChapterSectionDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ChapterSection> findByCriteria(ChapterSectionCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<ChapterSection> specification = createSpecification(criteria);
		return chapterSectionRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ChapterSectionCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ChapterSection> specification = createSpecification(criteria);
		return chapterSectionRepository.count(specification);
	}

	/**
	 * Function to convert {@link ChapterSectionCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ChapterSection> createSpecification(ChapterSectionCriteria criteria) {
		Specification<ChapterSection> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ChapterSection_.id));
			}
			if (criteria.getSectionName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getSectionName(), ChapterSection_.sectionName));
			}
			if (criteria.getSectionNumber() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getSectionNumber(), ChapterSection_.sectionNumber));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ChapterSection_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ChapterSection_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ChapterSection_.cancelDate));
			}
			if (criteria.getClassHomeWorkId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassHomeWorkId(),
						root -> root.join(ChapterSection_.classHomeWorks, JoinType.LEFT).get(ClassHomeWork_.id)));
			}
			if (criteria.getClassClassWorkId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassClassWorkId(),
						root -> root.join(ChapterSection_.classClassWorks, JoinType.LEFT).get(ClassClassWork_.id)));
			}
			if (criteria.getClassLessionPlanId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassLessionPlanId(),
						root -> root.join(ChapterSection_.classLessionPlans, JoinType.LEFT).get(ClassLessionPlan_.id)));
			}
			if (criteria.getSubjectChapterId() != null) {
				specification = specification.and(buildSpecification(criteria.getSubjectChapterId(),
						root -> root.join(ChapterSection_.subjectChapter, JoinType.LEFT).get(SubjectChapter_.id)));
			}
		}
		return specification;
	}
}
