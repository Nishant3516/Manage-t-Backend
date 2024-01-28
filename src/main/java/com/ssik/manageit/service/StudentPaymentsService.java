package com.ssik.manageit.service;

import com.ssik.manageit.domain.StudentPayments;
import com.ssik.manageit.repository.StudentPaymentsRepository;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.service.mapper.StudentPaymentsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentPayments}.
 */
@Service
@Transactional
public class StudentPaymentsService {

    private final Logger log = LoggerFactory.getLogger(StudentPaymentsService.class);

    private final StudentPaymentsRepository studentPaymentsRepository;

    private final StudentPaymentsMapper studentPaymentsMapper;

    public StudentPaymentsService(StudentPaymentsRepository studentPaymentsRepository, StudentPaymentsMapper studentPaymentsMapper) {
        this.studentPaymentsRepository = studentPaymentsRepository;
        this.studentPaymentsMapper = studentPaymentsMapper;
    }

    /**
     * Save a studentPayments.
     *
     * @param studentPaymentsDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentPaymentsDTO save(StudentPaymentsDTO studentPaymentsDTO) {
        log.debug("Request to save StudentPayments : {}", studentPaymentsDTO);
        StudentPayments studentPayments = studentPaymentsMapper.toEntity(studentPaymentsDTO);
        studentPayments = studentPaymentsRepository.save(studentPayments);
        return studentPaymentsMapper.toDto(studentPayments);
    }

    /**
     * Partially update a studentPayments.
     *
     * @param studentPaymentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentPaymentsDTO> partialUpdate(StudentPaymentsDTO studentPaymentsDTO) {
        log.debug("Request to partially update StudentPayments : {}", studentPaymentsDTO);

        return studentPaymentsRepository
            .findById(studentPaymentsDTO.getId())
            .map(
                existingStudentPayments -> {
                    studentPaymentsMapper.partialUpdate(existingStudentPayments, studentPaymentsDTO);
                    return existingStudentPayments;
                }
            )
            .map(studentPaymentsRepository::save)
            .map(studentPaymentsMapper::toDto);
    }

    /**
     * Get all the studentPayments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentPaymentsDTO> findAll() {
        log.debug("Request to get all StudentPayments");
        return studentPaymentsRepository
            .findAll()
            .stream()
            .map(studentPaymentsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one studentPayments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentPaymentsDTO> findOne(Long id) {
        log.debug("Request to get StudentPayments : {}", id);
        return studentPaymentsRepository.findById(id).map(studentPaymentsMapper::toDto);
    }

    /**
     * Delete the studentPayments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentPayments : {}", id);
        studentPaymentsRepository.deleteById(id);
    }
}
