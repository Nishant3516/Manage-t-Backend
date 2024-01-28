package com.ssik.manageit.repository;

import com.ssik.manageit.domain.IdStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IdStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdStoreRepository extends JpaRepository<IdStore, Long>, JpaSpecificationExecutor<IdStore> {}
