package com.ssik.manageit.service;

import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.mapper.StudentAdditionalChargesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentAdditionalCharges}.
 */
@Service
@Transactional
public class StudentAdditionalChargesService {

    private final Logger log = LoggerFactory.getLogger(StudentAdditionalChargesService.class);

    private final StudentAdditionalChargesRepository studentAdditionalChargesRepository;

    private final StudentAdditionalChargesMapper studentAdditionalChargesMapper;

    public StudentAdditionalChargesService(
        StudentAdditionalChargesRepository studentAdditionalChargesRepository,
        StudentAdditionalChargesMapper studentAdditionalChargesMapper
    ) {
        this.studentAdditionalChargesRepository = studentAdditionalChargesRepository;
        this.studentAdditionalChargesMapper = studentAdditionalChargesMapper;
    }

    /**
     * Save a studentAdditionalCharges.
     *
     * @param studentAdditionalChargesDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentAdditionalChargesDTO save(StudentAdditionalChargesDTO studentAdditionalChargesDTO) {
        log.debug("Request to save StudentAdditionalCharges : {}", studentAdditionalChargesDTO);
        StudentAdditionalCharges studentAdditionalCharges = studentAdditionalChargesMapper.toEntity(studentAdditionalChargesDTO);
        studentAdditionalCharges = studentAdditionalChargesRepository.save(studentAdditionalCharges);
        return studentAdditionalChargesMapper.toDto(studentAdditionalCharges);
    }

    /**
     * Partially update a studentAdditionalCharges.
     *
     * @param studentAdditionalChargesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentAdditionalChargesDTO> partialUpdate(StudentAdditionalChargesDTO studentAdditionalChargesDTO) {
        log.debug("Request to partially update StudentAdditionalCharges : {}", studentAdditionalChargesDTO);

        return studentAdditionalChargesRepository
            .findById(studentAdditionalChargesDTO.getId())
            .map(
                existingStudentAdditionalCharges -> {
                    studentAdditionalChargesMapper.partialUpdate(existingStudentAdditionalCharges, studentAdditionalChargesDTO);
                    return existingStudentAdditionalCharges;
                }
            )
            .map(studentAdditionalChargesRepository::save)
            .map(studentAdditionalChargesMapper::toDto);
    }

    /**
     * Get all the studentAdditionalCharges.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentAdditionalChargesDTO> findAll() {
        log.debug("Request to get all StudentAdditionalCharges");
        return studentAdditionalChargesRepository
            .findAll()
            .stream()
            .map(studentAdditionalChargesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one studentAdditionalCharges by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentAdditionalChargesDTO> findOne(Long id) {
        log.debug("Request to get StudentAdditionalCharges : {}", id);
        return studentAdditionalChargesRepository.findById(id).map(studentAdditionalChargesMapper::toDto);
    }

    /**
     * Delete the studentAdditionalCharges by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentAdditionalCharges : {}", id);
        studentAdditionalChargesRepository.deleteById(id);
    }
}
