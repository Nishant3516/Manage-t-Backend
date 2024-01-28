package com.ssik.manageit.repository;

import com.ssik.manageit.domain.STRoute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the STRoute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface STRouteRepository extends JpaRepository<STRoute, Long>, JpaSpecificationExecutor<STRoute> {}
