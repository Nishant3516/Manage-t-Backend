package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.repository.TagRepository;
import com.ssik.manageit.service.criteria.TagCriteria;
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
 * Service for executing complex queries for {@link Tag} entities in the
 * database. The main input is a {@link TagCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link Tag} or a {@link Page} of {@link Tag} which fulfills
 * the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TagQueryService extends QueryService<Tag> {

	private final Logger log = LoggerFactory.getLogger(TagQueryService.class);

	private final TagRepository tagRepository;

	public TagQueryService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	/**
	 * Return a {@link List} of {@link Tag} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<Tag> findByCriteria(TagCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<Tag> specification = createSpecification(criteria);
		return tagRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link Tag} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<Tag> findByCriteria(TagCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<Tag> specification = createSpecification(criteria);
		return tagRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(TagCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<Tag> specification = createSpecification(criteria);
		return tagRepository.count(specification);
	}

	/**
	 * Function to convert {@link TagCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<Tag> createSpecification(TagCriteria criteria) {
		Specification<Tag> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), Tag_.id));
			}
			if (criteria.getTagText() != null) {
				specification = specification.and(buildStringSpecification(criteria.getTagText(), Tag_.tagText));
			}
			if (criteria.getTagLevel() != null) {
				specification = specification.and(buildSpecification(criteria.getTagLevel(), Tag_.tagLevel));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Tag_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), Tag_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), Tag_.cancelDate));
			}
			if (criteria.getTenantId() != null) {
				specification = specification.and(buildSpecification(criteria.getTenantId(),
						root -> root.join(Tag_.tenant, JoinType.LEFT).get(Tenant_.id)));
			}
			if (criteria.getQuestionPaperId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionPaperId(),
						root -> root.join(Tag_.questionPapers, JoinType.LEFT).get(QuestionPaper_.id)));
			}
			if (criteria.getQuestionId() != null) {
				specification = specification.and(buildSpecification(criteria.getQuestionId(),
						root -> root.join(Tag_.questions, JoinType.LEFT).get(Question_.id)));
			}
		}
		return specification;
	}
}
