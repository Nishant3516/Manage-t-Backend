package com.ssik.manageit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.Tenant;

/**
 * Spring Data SQL repository for the SchoolLedgerHead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolLedgerHeadRepository
		extends JpaRepository<SchoolLedgerHead, Long>, JpaSpecificationExecutor<SchoolLedgerHead> {
	@Query()
	List<SchoolLedgerHead> findByLedgerHeadName(String ledgerHeadName);

}
