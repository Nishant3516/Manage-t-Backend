package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.service.criteria.SchoolLedgerHeadCriteria;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.mapper.SchoolLedgerHeadMapper;
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
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link SchoolLedgerHead} entities
 * in the database. The main input is a {@link SchoolLedgerHeadCriteria} which
 * gets converted to {@link Specification}, in a way that all the filters must
 * apply. It returns a {@link List} of {@link SchoolLedgerHeadDTO} or a
 * {@link Page} of {@link SchoolLedgerHeadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolLedgerHeadQueryService extends QueryService<SchoolLedgerHead> {

	private final Logger log = LoggerFactory.getLogger(SchoolLedgerHeadQueryService.class);

	private final SchoolLedgerHeadRepository schoolLedgerHeadRepository;

	private final SchoolLedgerHeadMapper schoolLedgerHeadMapper;

	public SchoolLedgerHeadQueryService(SchoolLedgerHeadRepository schoolLedgerHeadRepository,
			SchoolLedgerHeadMapper schoolLedgerHeadMapper) {
		this.schoolLedgerHeadRepository = schoolLedgerHeadRepository;
		this.schoolLedgerHeadMapper = schoolLedgerHeadMapper;
	}

	/**
	 * Return a {@link List} of {@link SchoolLedgerHeadDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<SchoolLedgerHead> findByCriteria(SchoolLedgerHeadCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		LocalDateFilter localDateFilter = new LocalDateFilter();
		localDateFilter.setSpecified(false);
		criteria.setCancelDate(localDateFilter);

		final Specification<SchoolLedgerHead> specification = createSpecification(criteria);
		return schoolLedgerHeadRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link SchoolLedgerHeadDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<SchoolLedgerHeadDTO> findByCriteria(SchoolLedgerHeadCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<SchoolLedgerHead> specification = createSpecification(criteria);
		return schoolLedgerHeadRepository.findAll(specification, page).map(schoolLedgerHeadMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(SchoolLedgerHeadCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<SchoolLedgerHead> specification = createSpecification(criteria);
		return schoolLedgerHeadRepository.count(specification);
	}

	/**
	 * Function to convert {@link SchoolLedgerHeadCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<SchoolLedgerHead> createSpecification(SchoolLedgerHeadCriteria criteria) {
		Specification<SchoolLedgerHead> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolLedgerHead_.id));
			}
			if (criteria.getStudentLedgerHeadType() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentLedgerHeadType(),
						SchoolLedgerHead_.studentLedgerHeadType));
			}
			if (criteria.getLedgerHeadName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getLedgerHeadName(), SchoolLedgerHead_.ledgerHeadName));
			}
			if (criteria.getLedgerHeadLongName() != null) {
				specification = specification.and(buildStringSpecification(criteria.getLedgerHeadLongName(),
						SchoolLedgerHead_.ledgerHeadLongName));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), SchoolLedgerHead_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), SchoolLedgerHead_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), SchoolLedgerHead_.cancelDate));
			}
			if (criteria.getClassFeeId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassFeeId(),
						root -> root.join(SchoolLedgerHead_.classFees, JoinType.LEFT).get(ClassFee_.id)));
			}
			if (criteria.getStudentDiscountId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentDiscountId(),
						root -> root.join(SchoolLedgerHead_.studentDiscounts, JoinType.LEFT).get(StudentDiscount_.id)));
			}
			if (criteria.getStudentAdditionalChargesId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentAdditionalChargesId(),
						root -> root.join(SchoolLedgerHead_.studentAdditionalCharges, JoinType.LEFT)
								.get(StudentAdditionalCharges_.id)));
			}
			if (criteria.getStudentChargesSummaryId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentChargesSummaryId(),
						root -> root.join(SchoolLedgerHead_.studentChargesSummaries, JoinType.LEFT)
								.get(StudentChargesSummary_.id)));
			}
			if (criteria.getSchoolId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolId(),
						root -> root.join(SchoolLedgerHead_.school, JoinType.LEFT).get(School_.id)));
			}
//			if (criteria.getTenantId() != null) {
//				specification = specification.and(buildSpecification(criteria.getTenantId(),
//						root -> root.join(SchoolLedgerHead_.tenant, JoinType.LEFT).get(Tenant_.id)));
//			}
		}
		return specification;
	}
}
