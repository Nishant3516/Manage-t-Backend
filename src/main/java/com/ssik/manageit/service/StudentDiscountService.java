package com.ssik.manageit.service;

import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.repository.StudentDiscountRepository;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.service.mapper.StudentDiscountMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentDiscount}.
 */
@Service
@Transactional
public class StudentDiscountService {

    private final Logger log = LoggerFactory.getLogger(StudentDiscountService.class);

    private final StudentDiscountRepository studentDiscountRepository;

    private final StudentDiscountMapper studentDiscountMapper;

    public StudentDiscountService(StudentDiscountRepository studentDiscountRepository, StudentDiscountMapper studentDiscountMapper) {
        this.studentDiscountRepository = studentDiscountRepository;
        this.studentDiscountMapper = studentDiscountMapper;
    }

    /**
     * Save a studentDiscount.
     *
     * @param studentDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentDiscountDTO save(StudentDiscountDTO studentDiscountDTO) {
        log.debug("Request to save StudentDiscount : {}", studentDiscountDTO);
        StudentDiscount studentDiscount = studentDiscountMapper.toEntity(studentDiscountDTO);
        studentDiscount = studentDiscountRepository.save(studentDiscount);
        return studentDiscountMapper.toDto(studentDiscount);
    }

    /**
     * Partially update a studentDiscount.
     *
     * @param studentDiscountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentDiscountDTO> partialUpdate(StudentDiscountDTO studentDiscountDTO) {
        log.debug("Request to partially update StudentDiscount : {}", studentDiscountDTO);

        return studentDiscountRepository
            .findById(studentDiscountDTO.getId())
            .map(
                existingStudentDiscount -> {
                    studentDiscountMapper.partialUpdate(existingStudentDiscount, studentDiscountDTO);
                    return existingStudentDiscount;
                }
            )
            .map(studentDiscountRepository::save)
            .map(studentDiscountMapper::toDto);
    }

    /**
     * Get all the studentDiscounts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentDiscountDTO> findAll() {
        log.debug("Request to get all StudentDiscounts");
        return studentDiscountRepository
            .findAll()
            .stream()
            .map(studentDiscountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one studentDiscount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentDiscountDTO> findOne(Long id) {
        log.debug("Request to get StudentDiscount : {}", id);
        return studentDiscountRepository.findById(id).map(studentDiscountMapper::toDto);
    }

    /**
     * Delete the studentDiscount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentDiscount : {}", id);
        studentDiscountRepository.deleteById(id);
    }
}
