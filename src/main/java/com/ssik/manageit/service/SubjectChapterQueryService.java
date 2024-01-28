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
import com.ssik.manageit.domain.ClassLessionPlan_;
import com.ssik.manageit.domain.ClassSubject_;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.domain.SubjectChapter_;
import com.ssik.manageit.repository.SubjectChapterRepository;
import com.ssik.manageit.service.criteria.SubjectChapterCriteria;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import com.ssik.manageit.service.mapper.SubjectChapterMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SubjectChapter} entities in
 * the database. The main input is a {@link SubjectChapterCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubjectChapterDTO} or a {@link Page} of
 * {@link SubjectChapterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubjectChapterQueryService extends QueryService<SubjectChapter> {

	private final Logger log = LoggerFactory.getLogger(SubjectChapterQueryService.class);

	private final SubjectChapterRepository subjectChapterRepository;

	private final SubjectChapterMapper subjectChapterMapper;

	public SubjectChapterQueryService(SubjectChapterRepository subjectChapterRepository,
			SubjectChapterMapper subjectChapterMapper) {
		this.subjectChapterRepository = subjectChapterRepository;
		this.subjectChapterMapper = subjectChapterMapper;
	}

	/**
	 * Return a {@link List} of {@link SubjectChapterDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<SubjectChapterDTO> findByCriteria(SubjectChapterCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<SubjectChapter> specification = createSpecification(criteria);
		return subjectChapterMapper.toDto(subjectChapterRepository.findAll(specification));
	}

	/**
	 * Return a {@link Page} of {@link SubjectChapterDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<SubjectChapter> findByCriteria(SubjectChapterCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<SubjectChapter> specification = createSpecification(criteria);
		Page<SubjectChapter> subjectChaps = subjectChapterRepository.findAll(specification, page);
		// List<SubjectChapter> subs=subjectChaps.getContent();
		return subjectChaps;
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(SubjectChapterCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<SubjectChapter> specification = createSpecification(criteria);
		return subjectChapterRepository.count(specification);
	}

	/**
	 * Function to convert {@link SubjectChapterCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<SubjectChapter> createSpecification(SubjectChapterCriteria criteria) {
		Specification<SubjectChapter> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), SubjectChapter_.id));
			}
			if (criteria.getChapterName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getChapterName(), SubjectChapter_.chapterName));
			}
			if (criteria.getChapterNumber() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getChapterNumber(), SubjectChapter_.chapterNumber));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), SubjectChapter_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), SubjectChapter_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), SubjectChapter_.cancelDate));
			}
			if (criteria.getChapterSectionId() != null) {
				specification = specification.and(buildSpecification(criteria.getChapterSectionId(),
						root -> root.join(SubjectChapter_.chapterSections, JoinType.LEFT).get(ChapterSection_.id)));
			}
			if (criteria.getClassLessionPlanId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassLessionPlanId(),
						root -> root.join(SubjectChapter_.classLessionPlans, JoinType.LEFT).get(ClassLessionPlan_.id)));
			}
			if (criteria.getClassSubjectId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassSubjectId(),
						root -> root.join(SubjectChapter_.classSubject, JoinType.LEFT).get(ClassSubject_.id)));
			}
		}
		return specification;
	}
}
