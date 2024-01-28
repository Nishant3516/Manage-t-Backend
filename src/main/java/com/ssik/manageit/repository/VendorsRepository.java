package com.ssik.manageit.repository;

import com.ssik.manageit.domain.Vendors;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vendors entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendorsRepository extends JpaRepository<Vendors, Long> {}
