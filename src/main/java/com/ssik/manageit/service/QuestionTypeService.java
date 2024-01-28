package com.ssik.manageit.service;

import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.repository.QuestionTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionType}.
 */
@Service
@Transactional
public class QuestionTypeService {

    private final Logger log = LoggerFactory.getLogger(QuestionTypeService.class);

    private final QuestionTypeRepository questionTypeRepository;

    public QuestionTypeService(QuestionTypeRepository questionTypeRepository) {
        this.questionTypeRepository = questionTypeRepository;
    }

    /**
     * Save a questionType.
     *
     * @param questionType the entity to save.
     * @return the persisted entity.
     */
    public QuestionType save(QuestionType questionType) {
        log.debug("Request to save QuestionType : {}", questionType);
        return questionTypeRepository.save(questionType);
    }

    /**
     * Partially update a questionType.
     *
     * @param questionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionType> partialUpdate(QuestionType questionType) {
        log.debug("Request to partially update QuestionType : {}", questionType);

        return questionTypeRepository
            .findById(questionType.getId())
            .map(existingQuestionType -> {
                if (questionType.getQuestionType() != null) {
                    existingQuestionType.setQuestionType(questionType.getQuestionType());
                }
                if (questionType.getMarks() != null) {
                    existingQuestionType.setMarks(questionType.getMarks());
                }
                if (questionType.getCreateDate() != null) {
                    existingQuestionType.setCreateDate(questionType.getCreateDate());
                }
                if (questionType.getLastModified() != null) {
                    existingQuestionType.setLastModified(questionType.getLastModified());
                }
                if (questionType.getCancelDate() != null) {
                    existingQuestionType.setCancelDate(questionType.getCancelDate());
                }

                return existingQuestionType;
            })
            .map(questionTypeRepository::save);
    }

    /**
     * Get all the questionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionType> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionTypes");
        return questionTypeRepository.findAll(pageable);
    }

    /**
     * Get one questionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionType> findOne(Long id) {
        log.debug("Request to get QuestionType : {}", id);
        return questionTypeRepository.findById(id);
    }

    /**
     * Delete the questionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionType : {}", id);
        questionTypeRepository.deleteById(id);
    }
}
