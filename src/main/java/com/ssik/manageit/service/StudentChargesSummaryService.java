package com.ssik.manageit.service;

import com.ssik.manageit.domain.StudentChargesSummary;
import com.ssik.manageit.repository.StudentChargesSummaryRepository;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.service.mapper.StudentChargesSummaryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentChargesSummary}.
 */
@Service
@Transactional
public class StudentChargesSummaryService {

    private final Logger log = LoggerFactory.getLogger(StudentChargesSummaryService.class);

    private final StudentChargesSummaryRepository studentChargesSummaryRepository;

    private final StudentChargesSummaryMapper studentChargesSummaryMapper;

    public StudentChargesSummaryService(
        StudentChargesSummaryRepository studentChargesSummaryRepository,
        StudentChargesSummaryMapper studentChargesSummaryMapper
    ) {
        this.studentChargesSummaryRepository = studentChargesSummaryRepository;
        this.studentChargesSummaryMapper = studentChargesSummaryMapper;
    }

    /**
     * Save a studentChargesSummary.
     *
     * @param studentChargesSummaryDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentChargesSummaryDTO save(StudentChargesSummaryDTO studentChargesSummaryDTO) {
        log.debug("Request to save StudentChargesSummary : {}", studentChargesSummaryDTO);
        StudentChargesSummary studentChargesSummary = studentChargesSummaryMapper.toEntity(studentChargesSummaryDTO);
        studentChargesSummary = studentChargesSummaryRepository.save(studentChargesSummary);
        return studentChargesSummaryMapper.toDto(studentChargesSummary);
    }

    /**
     * Partially update a studentChargesSummary.
     *
     * @param studentChargesSummaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentChargesSummaryDTO> partialUpdate(StudentChargesSummaryDTO studentChargesSummaryDTO) {
        log.debug("Request to partially update StudentChargesSummary : {}", studentChargesSummaryDTO);

        return studentChargesSummaryRepository
            .findById(studentChargesSummaryDTO.getId())
            .map(
                existingStudentChargesSummary -> {
                    studentChargesSummaryMapper.partialUpdate(existingStudentChargesSummary, studentChargesSummaryDTO);
                    return existingStudentChargesSummary;
                }
            )
            .map(studentChargesSummaryRepository::save)
            .map(studentChargesSummaryMapper::toDto);
    }

    /**
     * Get all the studentChargesSummaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentChargesSummaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentChargesSummaries");
        return studentChargesSummaryRepository.findAll(pageable).map(studentChargesSummaryMapper::toDto);
    }

    /**
     * Get one studentChargesSummary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentChargesSummaryDTO> findOne(Long id) {
        log.debug("Request to get StudentChargesSummary : {}", id);
        return studentChargesSummaryRepository.findById(id).map(studentChargesSummaryMapper::toDto);
    }

    /**
     * Delete the studentChargesSummary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentChargesSummary : {}", id);
        studentChargesSummaryRepository.deleteById(id);
    }
}
