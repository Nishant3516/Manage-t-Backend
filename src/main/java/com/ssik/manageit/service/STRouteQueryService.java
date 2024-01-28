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

import com.ssik.manageit.domain.STIncomeExpenses_;
// for static metamodels
import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.domain.STRoute_;
import com.ssik.manageit.repository.STRouteRepository;
import com.ssik.manageit.service.criteria.STRouteCriteria;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link STRoute} entities in the
 * database. The main input is a {@link STRouteCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link STRoute} or a {@link Page} of {@link STRoute} which
 * fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class STRouteQueryService extends QueryService<STRoute> {

	private final Logger log = LoggerFactory.getLogger(STRouteQueryService.class);

	private final STRouteRepository sTRouteRepository;

	public STRouteQueryService(STRouteRepository sTRouteRepository) {
		this.sTRouteRepository = sTRouteRepository;
	}

	/**
	 * Return a {@link List} of {@link STRoute} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<STRoute> findByCriteria(STRouteCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<STRoute> specification = createSpecification(criteria);
		return sTRouteRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link STRoute} which matches the criteria from the
	 * database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<STRoute> findByCriteria(STRouteCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<STRoute> specification = createSpecification(criteria);
		return sTRouteRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(STRouteCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<STRoute> specification = createSpecification(criteria);
		return sTRouteRepository.count(specification);
	}

	/**
	 * Function to convert {@link STRouteCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<STRoute> createSpecification(STRouteCriteria criteria) {
		Specification<STRoute> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), STRoute_.id));
			}
			if (criteria.getTransportRouteName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getTransportRouteName(), STRoute_.transportRouteName));
			}
			if (criteria.getRouteCharge() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getRouteCharge(), STRoute_.routeCharge));
			}
			if (criteria.getTransportRouteAddress() != null) {
				specification = specification.and(
						buildStringSpecification(criteria.getTransportRouteAddress(), STRoute_.transportRouteAddress));
			}
			if (criteria.getContactNumber() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getContactNumber(), STRoute_.contactNumber));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), STRoute_.createDate));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), STRoute_.cancelDate));
			}
			if (criteria.getRemarks() != null) {
				specification = specification.and(buildStringSpecification(criteria.getRemarks(), STRoute_.remarks));
			}
			if (criteria.getSTIncomeExpensesId() != null) {
				specification = specification.and(buildSpecification(criteria.getSTIncomeExpensesId(),
						root -> root.join(STRoute_.sTIncomeExpenses, JoinType.LEFT).get(STIncomeExpenses_.id)));
			}
		}
		return specification;
	}
}
