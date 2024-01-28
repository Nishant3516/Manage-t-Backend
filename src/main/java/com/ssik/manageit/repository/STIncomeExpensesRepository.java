package com.ssik.manageit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;

/**
 * Spring Data SQL repository for the STIncomeExpenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface STIncomeExpensesRepository
		extends JpaRepository<STIncomeExpenses, Long>, JpaSpecificationExecutor<STIncomeExpenses> {

	@Query
	public List<STIncomeExpenses> findByPaymentDateAndModeOfPayAndAmountPaidAndClassStudentAndStRouteAndCancelDateIsNull(
			LocalDate paymentDate, ModeOfPayment payMode, Double amtPaid, ClassStudent vendor, STRoute stRoute);

	@Query
	List<STIncomeExpenses> findByPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(LocalDate startDate,
			LocalDate endDate);

}
