package com.ssik.manageit.service;

import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.repository.QuestionPaperRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionPaper}.
 */
@Service
@Transactional
public class QuestionPaperService {

    private final Logger log = LoggerFactory.getLogger(QuestionPaperService.class);

    private final QuestionPaperRepository questionPaperRepository;

    public QuestionPaperService(QuestionPaperRepository questionPaperRepository) {
        this.questionPaperRepository = questionPaperRepository;
    }

    /**
     * Save a questionPaper.
     *
     * @param questionPaper the entity to save.
     * @return the persisted entity.
     */
    public QuestionPaper save(QuestionPaper questionPaper) {
        log.debug("Request to save QuestionPaper : {}", questionPaper);
        return questionPaperRepository.save(questionPaper);
    }

    /**
     * Partially update a questionPaper.
     *
     * @param questionPaper the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionPaper> partialUpdate(QuestionPaper questionPaper) {
        log.debug("Request to partially update QuestionPaper : {}", questionPaper);

        return questionPaperRepository
            .findById(questionPaper.getId())
            .map(existingQuestionPaper -> {
                if (questionPaper.getTenatLogo() != null) {
                    existingQuestionPaper.setTenatLogo(questionPaper.getTenatLogo());
                }
                if (questionPaper.getTenatLogoContentType() != null) {
                    existingQuestionPaper.setTenatLogoContentType(questionPaper.getTenatLogoContentType());
                }
                if (questionPaper.getQuestionPaperFile() != null) {
                    existingQuestionPaper.setQuestionPaperFile(questionPaper.getQuestionPaperFile());
                }
                if (questionPaper.getQuestionPaperFileContentType() != null) {
                    existingQuestionPaper.setQuestionPaperFileContentType(questionPaper.getQuestionPaperFileContentType());
                }
                if (questionPaper.getQuestionPaperName() != null) {
                    existingQuestionPaper.setQuestionPaperName(questionPaper.getQuestionPaperName());
                }
                if (questionPaper.getMainTitle() != null) {
                    existingQuestionPaper.setMainTitle(questionPaper.getMainTitle());
                }
                if (questionPaper.getSubTitle() != null) {
                    existingQuestionPaper.setSubTitle(questionPaper.getSubTitle());
                }
                if (questionPaper.getLeftSubHeading1() != null) {
                    existingQuestionPaper.setLeftSubHeading1(questionPaper.getLeftSubHeading1());
                }
                if (questionPaper.getLeftSubHeading2() != null) {
                    existingQuestionPaper.setLeftSubHeading2(questionPaper.getLeftSubHeading2());
                }
                if (questionPaper.getRightSubHeading1() != null) {
                    existingQuestionPaper.setRightSubHeading1(questionPaper.getRightSubHeading1());
                }
                if (questionPaper.getRightSubHeading2() != null) {
                    existingQuestionPaper.setRightSubHeading2(questionPaper.getRightSubHeading2());
                }
                if (questionPaper.getInstructions() != null) {
                    existingQuestionPaper.setInstructions(questionPaper.getInstructions());
                }
                if (questionPaper.getFooterText() != null) {
                    existingQuestionPaper.setFooterText(questionPaper.getFooterText());
                }
                if (questionPaper.getTotalMarks() != null) {
                    existingQuestionPaper.setTotalMarks(questionPaper.getTotalMarks());
                }
                if (questionPaper.getCreateDate() != null) {
                    existingQuestionPaper.setCreateDate(questionPaper.getCreateDate());
                }
                if (questionPaper.getLastModified() != null) {
                    existingQuestionPaper.setLastModified(questionPaper.getLastModified());
                }
                if (questionPaper.getCancelDate() != null) {
                    existingQuestionPaper.setCancelDate(questionPaper.getCancelDate());
                }

                return existingQuestionPaper;
            })
            .map(questionPaperRepository::save);
    }

    /**
     * Get all the questionPapers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionPaper> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionPapers");
        return questionPaperRepository.findAll(pageable);
    }

    /**
     * Get all the questionPapers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QuestionPaper> findAllWithEagerRelationships(Pageable pageable) {
        return questionPaperRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one questionPaper by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionPaper> findOne(Long id) {
        log.debug("Request to get QuestionPaper : {}", id);
        return questionPaperRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the questionPaper by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionPaper : {}", id);
        questionPaperRepository.deleteById(id);
    }
}
