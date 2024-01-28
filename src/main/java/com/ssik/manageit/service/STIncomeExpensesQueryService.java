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

import com.ssik.manageit.domain.ClassStudent_;
// for static metamodels
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.STIncomeExpenses_;
import com.ssik.manageit.domain.STRoute_;
import com.ssik.manageit.domain.Vendors_;
import com.ssik.manageit.repository.STIncomeExpensesRepository;
import com.ssik.manageit.service.criteria.STIncomeExpensesCriteria;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link STIncomeExpenses} entities
 * in the database. The main input is a {@link STIncomeExpensesCriteria} which
 * gets converted to {@link Specification}, in a way that all the filters must
 * apply. It returns a {@link List} of {@link STIncomeExpenses} or a
 * {@link Page} of {@link STIncomeExpenses} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class STIncomeExpensesQueryService extends QueryService<STIncomeExpenses> {

	private final Logger log = LoggerFactory.getLogger(STIncomeExpensesQueryService.class);

	private final STIncomeExpensesRepository sTIncomeExpensesRepository;

	public STIncomeExpensesQueryService(STIncomeExpensesRepository sTIncomeExpensesRepository) {
		this.sTIncomeExpensesRepository = sTIncomeExpensesRepository;
	}

	/**
	 * Return a {@link List} of {@link STIncomeExpenses} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<STIncomeExpenses> findByCriteria(STIncomeExpensesCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		final Specification<STIncomeExpenses> specification = createSpecification(criteria);
		return sTIncomeExpensesRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link STIncomeExpenses} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<STIncomeExpenses> findByCriteria(STIncomeExpensesCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<STIncomeExpenses> specification = createSpecification(criteria);
		return sTIncomeExpensesRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(STIncomeExpensesCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<STIncomeExpenses> specification = createSpecification(criteria);
		return sTIncomeExpensesRepository.count(specification);
	}

	/**
	 * Function to convert {@link STIncomeExpensesCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<STIncomeExpenses> createSpecification(STIncomeExpensesCriteria criteria) {
		Specification<STIncomeExpenses> specification = Specification.where(null);
		if (criteria != null) {
			// This has to be called first, because the distinct method returns null
//            if (criteria.getDistinct() != null) {
//                specification = specification.and(distinct(criteria.getDistinct()));
//            }
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), STIncomeExpenses_.id));
			}
			if (criteria.getAmountPaid() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getAmountPaid(), STIncomeExpenses_.amountPaid));
			}
			if (criteria.getModeOfPay() != null) {
				specification = specification
						.and(buildSpecification(criteria.getModeOfPay(), STIncomeExpenses_.modeOfPay));
			}
			if (criteria.getNoteNumbers() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getNoteNumbers(), STIncomeExpenses_.noteNumbers));
			}
			if (criteria.getUpiId() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getUpiId(), STIncomeExpenses_.upiId));
			}
			if (criteria.getRemarks() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRemarks(), STIncomeExpenses_.remarks));
			}
			if (criteria.getPaymentDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getPaymentDate(), STIncomeExpenses_.paymentDate));
			}
			if (criteria.getReceiptId() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getReceiptId(), STIncomeExpenses_.receiptId));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), STIncomeExpenses_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), STIncomeExpenses_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), STIncomeExpenses_.cancelDate));
			}
			if (criteria.getTransactionType() != null) {
				specification = specification
						.and(buildSpecification(criteria.getTransactionType(), STIncomeExpenses_.transactionType));
			}
			if (criteria.getClassStudentId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassStudentId(),
						root -> root.join(STIncomeExpenses_.classStudent, JoinType.LEFT).get(ClassStudent_.id)));
			}
			if (criteria.getStRouteId() != null) {
				specification = specification.and(buildSpecification(criteria.getStRouteId(),
						root -> root.join(STIncomeExpenses_.stRoute, JoinType.LEFT).get(STRoute_.id)));
			}
			if (criteria.getOperatedById() != null) {
				specification = specification.and(buildSpecification(criteria.getOperatedById(),
						root -> root.join(STIncomeExpenses_.operatedBy, JoinType.LEFT).get(Vendors_.id)));
			}
		}
		return specification;
	}
}
