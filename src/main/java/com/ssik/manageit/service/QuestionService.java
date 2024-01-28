package com.ssik.manageit.service;

import com.ssik.manageit.domain.Question;
import com.ssik.manageit.repository.QuestionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Save a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    public Question save(Question question) {
        log.debug("Request to save Question : {}", question);
        return questionRepository.save(question);
    }

    /**
     * Partially update a question.
     *
     * @param question the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
            .findById(question.getId())
            .map(existingQuestion -> {
                if (question.getQuestionImportFile() != null) {
                    existingQuestion.setQuestionImportFile(question.getQuestionImportFile());
                }
                if (question.getQuestionImportFileContentType() != null) {
                    existingQuestion.setQuestionImportFileContentType(question.getQuestionImportFileContentType());
                }
                if (question.getQuestionText() != null) {
                    existingQuestion.setQuestionText(question.getQuestionText());
                }
                if (question.getQuestionImage() != null) {
                    existingQuestion.setQuestionImage(question.getQuestionImage());
                }
                if (question.getQuestionImageContentType() != null) {
                    existingQuestion.setQuestionImageContentType(question.getQuestionImageContentType());
                }
                if (question.getAnswerImage() != null) {
                    existingQuestion.setAnswerImage(question.getAnswerImage());
                }
                if (question.getAnswerImageContentType() != null) {
                    existingQuestion.setAnswerImageContentType(question.getAnswerImageContentType());
                }
                if (question.getImageSideBySide() != null) {
                    existingQuestion.setImageSideBySide(question.getImageSideBySide());
                }
                if (question.getOption1() != null) {
                    existingQuestion.setOption1(question.getOption1());
                }
                if (question.getOption2() != null) {
                    existingQuestion.setOption2(question.getOption2());
                }
                if (question.getOption3() != null) {
                    existingQuestion.setOption3(question.getOption3());
                }
                if (question.getOption4() != null) {
                    existingQuestion.setOption4(question.getOption4());
                }
                if (question.getOption5() != null) {
                    existingQuestion.setOption5(question.getOption5());
                }
                if (question.getStatus() != null) {
                    existingQuestion.setStatus(question.getStatus());
                }
                if (question.getWeightage() != null) {
                    existingQuestion.setWeightage(question.getWeightage());
                }
                if (question.getDifficultyLevel() != null) {
                    existingQuestion.setDifficultyLevel(question.getDifficultyLevel());
                }
                if (question.getCreateDate() != null) {
                    existingQuestion.setCreateDate(question.getCreateDate());
                }
                if (question.getLastModified() != null) {
                    existingQuestion.setLastModified(question.getLastModified());
                }
                if (question.getCancelDate() != null) {
                    existingQuestion.setCancelDate(question.getCancelDate());
                }

                return existingQuestion;
            })
            .map(questionRepository::save);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable);
    }

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Question> findAllWithEagerRelationships(Pageable pageable) {
        return questionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Question> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }
}
