package com.ssik.manageit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.IncomeExpenses;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.Vendors;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;

/**
 * Spring Data SQL repository for the IncomeExpenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomeExpensesRepository extends JpaRepository<IncomeExpenses, Long> {

	@Query
	public List<IncomeExpenses> findBySchoolAndPaymentDateAndModeOfPayAndAmountPaidAndVendorAndCancelDateIsNull(
			School school, LocalDate paymentDate, ModeOfPayment payMode, Double amtPaid, Vendors vendor);

	@Query
	List<IncomeExpenses> findBySchoolAndPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(School school,
			LocalDate startDate, LocalDate endDate);

}
