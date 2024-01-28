package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.Question;
import com.ssik.manageit.repository.QuestionRepository;
import com.ssik.manageit.service.criteria.QuestionCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Question} entities in the
 * database. The main input is a {@link QuestionCriteria} which gets converted
 * to {@link Specification}, in a way that all the filters must apply. It
 * returns a {@link List} of {@link Question} or a {@link Page} of
 * {@link Question} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionQueryService extends QueryService<Question> {

	private final Logger log = LoggerFactory.getLogger(QuestionQueryService.class);

	private final QuestionRepository questionRepository;

	public QuestionQueryService(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	/**
	 * Return a {@link List} of {@link Question} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<Question> findByCriteria(QuestionCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<Question> specification = createSpecification(criteria);
		return questionRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link Question} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<Question> findByCriteria(QuestionCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<Question> specification = createSpecification(criteria);
		return questionRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(QuestionCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<Question> specification = createSpecification(criteria);
		return questionRepository.count(specification);
	}

	/**
	 * Function to convert {@link QuestionCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<Question> createSpecification(QuestionCriteria criteria) {
		Specification<Question> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), Question_.id));
			}
			if (criteria.getImageSideBySide() != null) {
				specification = specification
						.and(buildSpecification(criteria.getImageSideBySide(), Question_.imageSideBySide));
			}
			if (criteria.getOption1() != null) {
				specification = specification.and(buildStringSpecification(criteria.getOption1(), Question_.option1));
			}
			if (criteria.getOption2() != null) {
				specification = specification.and(buildStringSpecification(criteria.getOption2(), Question_.option2));
			}
			if (criteria.getOption3() != null) {
				specification = specification.and(buildStringSpecification(criteria.getOption3(), Question_.option3));
			}
			if (criteria.getOption4() != null) {
				specification = specification.and(buildStringSpecification(criteria.getOption4(), Question_.option4));
			}
			if (criteria.getOption5() != null) {
				specification = specification.and(buildStringSpecification(criteria.getOption5(), Question_.option5));
			}
			if (criteria.getStatus() != null) {
				specification = specification.and(buildSpecification(criteria.getStatus(), Question_.status));
			}
			if (criteria.getWeightage() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getWeightage(), Question_.weightage));
			}
			if (criteria.getDifficultyLevel() != null) {
				specification = specification
						.and(buildSpecification(criteria.getDifficultyLevel(), Question_.difficultyLevel));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), Question_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), Question_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), Question_.cancelDate));
			}
			if (criteria.getTagId() != null) {
				specification = specification.and(buildSpecification(criteria.getTagId(),
						root -> root.join(Question_.tags, JoinType.LEFT).get(Tag_.id)));
			}
			if (criteria.getQuestionTypeId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionTypeId(),
						root -> root.join(Question_.questionType, JoinType.LEFT).get(QuestionType_.id)));
			}
			if (criteria.getTenantId() != null) {
				specification = specification.and(buildSpecification(criteria.getTenantId(),
						root -> root.join(Question_.tenant, JoinType.LEFT).get(Tenant_.id)));
			}
			if (criteria.getSchoolClassId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolClassId(),
						root -> root.join(Question_.schoolClass, JoinType.LEFT).get(SchoolClass_.id)));
			}
			if (criteria.getClassSubjectId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassSubjectId(),
						root -> root.join(Question_.classSubject, JoinType.LEFT).get(ClassSubject_.id)));
			}
			if (criteria.getSubjectChapterId() != null) {
				specification = specification.and(buildSpecification(criteria.getSubjectChapterId(),
						root -> root.join(Question_.subjectChapter, JoinType.LEFT).get(SubjectChapter_.id)));
			}
			if (criteria.getQuestionPaperId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionPaperId(),
						root -> root.join(Question_.questionPapers, JoinType.LEFT).get(QuestionPaper_.id)));
			}
		}
		return specification;
	}
}
