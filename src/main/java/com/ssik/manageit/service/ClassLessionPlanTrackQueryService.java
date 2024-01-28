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
import com.ssik.manageit.domain.ClassLessionPlanTrack;
import com.ssik.manageit.domain.ClassLessionPlanTrack_;
import com.ssik.manageit.domain.ClassLessionPlan_;
import com.ssik.manageit.repository.ClassLessionPlanTrackRepository;
import com.ssik.manageit.service.criteria.ClassLessionPlanTrackCriteria;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanTrackMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ClassLessionPlanTrack}
 * entities in the database. The main input is a
 * {@link ClassLessionPlanTrackCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link ClassLessionPlanTrackDTO} or a {@link Page} of
 * {@link ClassLessionPlanTrackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassLessionPlanTrackQueryService extends QueryService<ClassLessionPlanTrack> {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanTrackQueryService.class);

	private final ClassLessionPlanTrackRepository classLessionPlanTrackRepository;

	private final ClassLessionPlanTrackMapper classLessionPlanTrackMapper;

	public ClassLessionPlanTrackQueryService(ClassLessionPlanTrackRepository classLessionPlanTrackRepository,
			ClassLessionPlanTrackMapper classLessionPlanTrackMapper) {
		this.classLessionPlanTrackRepository = classLessionPlanTrackRepository;
		this.classLessionPlanTrackMapper = classLessionPlanTrackMapper;
	}

	/**
	 * Return a {@link List} of {@link ClassLessionPlanTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassLessionPlanTrack> findByCriteria(ClassLessionPlanTrackCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<ClassLessionPlanTrack> specification = createSpecification(criteria);
		return classLessionPlanTrackRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link ClassLessionPlanTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassLessionPlanTrack> findByCriteria(ClassLessionPlanTrackCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<ClassLessionPlanTrack> specification = createSpecification(criteria);
		Page<ClassLessionPlanTrack> lps = classLessionPlanTrackRepository.findAll(specification, page);
		return lps;
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ClassLessionPlanTrackCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ClassLessionPlanTrack> specification = createSpecification(criteria);
		return classLessionPlanTrackRepository.count(specification);
	}

	/**
	 * Function to convert {@link ClassLessionPlanTrackCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ClassLessionPlanTrack> createSpecification(ClassLessionPlanTrackCriteria criteria) {
		Specification<ClassLessionPlanTrack> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ClassLessionPlanTrack_.id));
			}
			if (criteria.getWorkStatus() != null) {
				specification = specification
						.and(buildSpecification(criteria.getWorkStatus(), ClassLessionPlanTrack_.workStatus));
			}
			if (criteria.getRemarks() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRemarks(), ClassLessionPlanTrack_.remarks));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ClassLessionPlanTrack_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ClassLessionPlanTrack_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ClassLessionPlanTrack_.cancelDate));
			}
			if (criteria.getClassLessionPlanId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassLessionPlanId(), root -> root
						.join(ClassLessionPlanTrack_.classLessionPlan, JoinType.LEFT).get(ClassLessionPlan_.id)));
			}
		}
		return specification;
	}
}
