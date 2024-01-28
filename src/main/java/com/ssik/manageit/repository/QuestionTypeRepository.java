package com.ssik.manageit.repository;

import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.domain.QuestionType;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionTypeRepository
		extends JpaRepository<QuestionType, Long>, JpaSpecificationExecutor<QuestionType> {

	QuestionType findByQuestionType(String quetionType);

}
