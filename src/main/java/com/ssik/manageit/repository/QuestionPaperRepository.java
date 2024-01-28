package com.ssik.manageit.repository;

import com.ssik.manageit.domain.QuestionPaper;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionPaper entity.
 */
@Repository
public interface QuestionPaperRepository extends JpaRepository<QuestionPaper, Long>, JpaSpecificationExecutor<QuestionPaper> {
    @Query(
        value = "select distinct questionPaper from QuestionPaper questionPaper left join fetch questionPaper.questions left join fetch questionPaper.tags",
        countQuery = "select count(distinct questionPaper) from QuestionPaper questionPaper"
    )
    Page<QuestionPaper> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct questionPaper from QuestionPaper questionPaper left join fetch questionPaper.questions left join fetch questionPaper.tags"
    )
    List<QuestionPaper> findAllWithEagerRelationships();

    @Query(
        "select questionPaper from QuestionPaper questionPaper left join fetch questionPaper.questions left join fetch questionPaper.tags where questionPaper.id =:id"
    )
    Optional<QuestionPaper> findOneWithEagerRelationships(@Param("id") Long id);
}
