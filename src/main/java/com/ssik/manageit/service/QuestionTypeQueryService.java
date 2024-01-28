package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.repository.QuestionTypeRepository;
import com.ssik.manageit.service.criteria.QuestionTypeCriteria;
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
 * Service for executing complex queries for {@link QuestionType} entities in
 * the database. The main input is a {@link QuestionTypeCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionType} or a {@link Page} of
 * {@link QuestionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionTypeQueryService extends QueryService<QuestionType> {

	private final Logger log = LoggerFactory.getLogger(QuestionTypeQueryService.class);

	private final QuestionTypeRepository questionTypeRepository;

	public QuestionTypeQueryService(QuestionTypeRepository questionTypeRepository) {
		this.questionTypeRepository = questionTypeRepository;
	}

	/**
	 * Return a {@link List} of {@link QuestionType} which matches the criteria from
	 * the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<QuestionType> findByCriteria(QuestionTypeCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<QuestionType> specification = createSpecification(criteria);
		return questionTypeRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link QuestionType} which matches the criteria from
	 * the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<QuestionType> findByCriteria(QuestionTypeCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<QuestionType> specification = createSpecification(criteria);
		return questionTypeRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(QuestionTypeCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<QuestionType> specification = createSpecification(criteria);
		return questionTypeRepository.count(specification);
	}

	/**
	 * Function to convert {@link QuestionTypeCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<QuestionType> createSpecification(QuestionTypeCriteria criteria) {
		Specification<QuestionType> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), QuestionType_.id));
			}
			if (criteria.getQuestionType() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getQuestionType(), QuestionType_.questionType));
			}
			if (criteria.getMarks() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getMarks(), QuestionType_.marks));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), QuestionType_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), QuestionType_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), QuestionType_.cancelDate));
			}
			if (criteria.getQuestionId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionId(),
						root -> root.join(QuestionType_.questions, JoinType.LEFT).get(Question_.id)));
			}
			if (criteria.getTenantId() != null) {
				specification = specification.and(buildSpecification(criteria.getTenantId(),
						root -> root.join(QuestionType_.tenant, JoinType.LEFT).get(Tenant_.id)));
			}
		}
		return specification;
	}
}
