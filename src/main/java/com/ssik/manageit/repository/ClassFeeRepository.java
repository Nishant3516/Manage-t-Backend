package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassFee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassFee entity.
 */
@Repository
public interface ClassFeeRepository extends JpaRepository<ClassFee, Long>, JpaSpecificationExecutor<ClassFee> {
    @Query(
        value = "select distinct classFee from ClassFee classFee left join fetch classFee.schoolClasses",
        countQuery = "select count(distinct classFee) from ClassFee classFee"
    )
    Page<ClassFee> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct classFee from ClassFee classFee left join fetch classFee.schoolClasses")
    List<ClassFee> findAllWithEagerRelationships();

    @Query("select classFee from ClassFee classFee left join fetch classFee.schoolClasses where classFee.id =:id")
    Optional<ClassFee> findOneWithEagerRelationships(@Param("id") Long id);
}
