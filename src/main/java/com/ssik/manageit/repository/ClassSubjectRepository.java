package com.ssik.manageit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssik.manageit.domain.ClassSubject;

/**
 * Spring Data SQL repository for the ClassSubject entity.
 */
@Repository
public interface ClassSubjectRepository
		extends JpaRepository<ClassSubject, Long>, JpaSpecificationExecutor<ClassSubject> {
	@Query(value = "select distinct classSubject from ClassSubject classSubject left join fetch classSubject.schoolClasses", countQuery = "select count(distinct classSubject) from ClassSubject classSubject")
	Page<ClassSubject> findAllWithEagerRelationships(Pageable pageable);

	@Query("select distinct classSubject from ClassSubject classSubject left join fetch classSubject.schoolClasses")
	List<ClassSubject> findAllWithEagerRelationships();

	@Query("select classSubject from ClassSubject classSubject left join fetch classSubject.schoolClasses where classSubject.id =:id")
	Optional<ClassSubject> findOneWithEagerRelationships(@Param("id") Long id);

	@Query()
	List<ClassSubject> findBySubjectName(String subjectName);

}
