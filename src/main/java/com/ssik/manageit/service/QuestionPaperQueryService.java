package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.repository.QuestionPaperRepository;
import com.ssik.manageit.service.criteria.QuestionPaperCriteria;
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
 * Service for executing complex queries for {@link QuestionPaper} entities in
 * the database. The main input is a {@link QuestionPaperCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionPaper} or a {@link Page} of
 * {@link QuestionPaper} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionPaperQueryService extends QueryService<QuestionPaper> {

	private final Logger log = LoggerFactory.getLogger(QuestionPaperQueryService.class);

	private final QuestionPaperRepository questionPaperRepository;

	public QuestionPaperQueryService(QuestionPaperRepository questionPaperRepository) {
		this.questionPaperRepository = questionPaperRepository;
	}

	/**
	 * Return a {@link List} of {@link QuestionPaper} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<QuestionPaper> findByCriteria(QuestionPaperCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<QuestionPaper> specification = createSpecification(criteria);
		return questionPaperRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link QuestionPaper} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<QuestionPaper> findByCriteria(QuestionPaperCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<QuestionPaper> specification = createSpecification(criteria);
		return questionPaperRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(QuestionPaperCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<QuestionPaper> specification = createSpecification(criteria);
		return questionPaperRepository.count(specification);
	}

	/**
	 * Function to convert {@link QuestionPaperCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<QuestionPaper> createSpecification(QuestionPaperCriteria criteria) {
		Specification<QuestionPaper> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), QuestionPaper_.id));
			}
			if (criteria.getQuestionPaperName() != null) {
				specification = specification.and(
						buildStringSpecification(criteria.getQuestionPaperName(), QuestionPaper_.questionPaperName));
			}
			if (criteria.getMainTitle() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getMainTitle(), QuestionPaper_.mainTitle));
			}
			if (criteria.getSubTitle() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getSubTitle(), QuestionPaper_.subTitle));
			}
			if (criteria.getLeftSubHeading1() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getLeftSubHeading1(), QuestionPaper_.leftSubHeading1));
			}
			if (criteria.getLeftSubHeading2() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getLeftSubHeading2(), QuestionPaper_.leftSubHeading2));
			}
			if (criteria.getRightSubHeading1() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRightSubHeading1(), QuestionPaper_.rightSubHeading1));
			}
			if (criteria.getRightSubHeading2() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRightSubHeading2(), QuestionPaper_.rightSubHeading2));
			}
			if (criteria.getInstructions() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getInstructions(), QuestionPaper_.instructions));
			}
			if (criteria.getFooterText() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getFooterText(), QuestionPaper_.footerText));
			}
			if (criteria.getTotalMarks() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getTotalMarks(), QuestionPaper_.totalMarks));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), QuestionPaper_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), QuestionPaper_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), QuestionPaper_.cancelDate));
			}
			if (criteria.getQuestionId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionId(),
						root -> root.join(QuestionPaper_.questions, JoinType.LEFT).get(Question_.id)));
			}
			if (criteria.getTagId() != null) {
				specification = specification.and(buildSpecification(criteria.getTagId(),
						root -> root.join(QuestionPaper_.tags, JoinType.LEFT).get(Tag_.id)));
			}
			if (criteria.getTenantId() != null) {
				specification = specification.and(buildSpecification(criteria.getTenantId(),
						root -> root.join(QuestionPaper_.tenant, JoinType.LEFT).get(Tenant_.id)));
			}
			if (criteria.getSchoolClassId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolClassId(),
						root -> root.join(QuestionPaper_.schoolClass, JoinType.LEFT).get(SchoolClass_.id)));
			}
			if (criteria.getClassSubjectId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassSubjectId(),
						root -> root.join(QuestionPaper_.classSubject, JoinType.LEFT).get(ClassSubject_.id)));
			}
		}
		return specification;
	}
}
