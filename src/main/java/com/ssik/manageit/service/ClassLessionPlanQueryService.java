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
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassLessionPlanTrack_;
import com.ssik.manageit.domain.ClassLessionPlan_;
import com.ssik.manageit.domain.ClassSubject_;
import com.ssik.manageit.domain.SchoolClass_;
import com.ssik.manageit.domain.SubjectChapter_;
import com.ssik.manageit.repository.ClassLessionPlanRepository;
import com.ssik.manageit.service.criteria.ClassLessionPlanCriteria;
import com.ssik.manageit.service.dto.ClassLessionPlanDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ClassLessionPlan} entities
 * in the database. The main input is a {@link ClassLessionPlanCriteria} which
 * gets converted to {@link Specification}, in a way that all the filters must
 * apply. It returns a {@link List} of {@link ClassLessionPlanDTO} or a
 * {@link Page} of {@link ClassLessionPlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassLessionPlanQueryService extends QueryService<ClassLessionPlan> {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanQueryService.class);

	private final ClassLessionPlanRepository classLessionPlanRepository;

	private final ClassLessionPlanMapper classLessionPlanMapper;

	public ClassLessionPlanQueryService(ClassLessionPlanRepository classLessionPlanRepository,
			ClassLessionPlanMapper classLessionPlanMapper) {
		this.classLessionPlanRepository = classLessionPlanRepository;
		this.classLessionPlanMapper = classLessionPlanMapper;
	}

	/**
	 * Return a {@link List} of {@link ClassLessionPlanDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassLessionPlan> findByCriteria(ClassLessionPlanCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<ClassLessionPlan> specification = createSpecification(criteria);
		return classLessionPlanRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link ClassLessionPlanDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassLessionPlanDTO> findByCriteria(ClassLessionPlanCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<ClassLessionPlan> specification = createSpecification(criteria);
		return classLessionPlanRepository.findAll(specification, page).map(classLessionPlanMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ClassLessionPlanCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ClassLessionPlan> specification = createSpecification(criteria);
		return classLessionPlanRepository.count(specification);
	}

	/**
	 * Function to convert {@link ClassLessionPlanCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ClassLessionPlan> createSpecification(ClassLessionPlanCriteria criteria) {
		Specification<ClassLessionPlan> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ClassLessionPlan_.id));
			}
			if (criteria.getSchoolDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getSchoolDate(), ClassLessionPlan_.schoolDate));
			}
			if (criteria.getClassWorkText() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getClassWorkText(), ClassLessionPlan_.classWorkText));
			}
			if (criteria.getHomeWorkText() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getHomeWorkText(), ClassLessionPlan_.homeWorkText));
			}
			if (criteria.getWorkStatus() != null) {
				specification = specification
						.and(buildSpecification(criteria.getWorkStatus(), ClassLessionPlan_.workStatus));
			}
			if (criteria.getLessionPlanFileLink() != null) {
				specification = specification.and(buildStringSpecification(criteria.getLessionPlanFileLink(),
						ClassLessionPlan_.lessionPlanFileLink));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ClassLessionPlan_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ClassLessionPlan_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ClassLessionPlan_.cancelDate));
			}
			if (criteria.getClassLessionPlanTrackId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassLessionPlanTrackId(), root -> root
						.join(ClassLessionPlan_.classLessionPlanTracks, JoinType.LEFT).get(ClassLessionPlanTrack_.id)));
			}
			if (criteria.getChapterSectionId() != null) {
				specification = specification.and(buildSpecification(criteria.getChapterSectionId(),
						root -> root.join(ClassLessionPlan_.chapterSection, JoinType.LEFT).get(ChapterSection_.id)));
			}
			if (criteria.getSchoolClassId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolClassId(),
						root -> root.join(ClassLessionPlan_.schoolClass, JoinType.LEFT).get(SchoolClass_.id)));
			}
			if (criteria.getClassSubjectId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassSubjectId(),
						root -> root.join(ClassLessionPlan_.classSubject, JoinType.LEFT).get(ClassSubject_.id)));
			}
			if (criteria.getSubjectChapterId() != null) {
				specification = specification.and(buildSpecification(criteria.getSubjectChapterId(),
						root -> root.join(ClassLessionPlan_.subjectChapter, JoinType.LEFT).get(SubjectChapter_.id)));
			}
		}
		return specification;
	}
}
