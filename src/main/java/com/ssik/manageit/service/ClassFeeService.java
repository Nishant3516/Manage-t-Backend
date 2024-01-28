package com.ssik.manageit.service;

import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.repository.ClassFeeRepository;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.service.mapper.ClassFeeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassFee}.
 */
@Service
@Transactional
public class ClassFeeService {

    private final Logger log = LoggerFactory.getLogger(ClassFeeService.class);

    private final ClassFeeRepository classFeeRepository;

    private final ClassFeeMapper classFeeMapper;

    public ClassFeeService(ClassFeeRepository classFeeRepository, ClassFeeMapper classFeeMapper) {
        this.classFeeRepository = classFeeRepository;
        this.classFeeMapper = classFeeMapper;
    }

    /**
     * Save a classFee.
     *
     * @param classFeeDTO the entity to save.
     * @return the persisted entity.
     */
    public ClassFeeDTO save(ClassFeeDTO classFeeDTO) {
        log.debug("Request to save ClassFee : {}", classFeeDTO);
        ClassFee classFee = classFeeMapper.toEntity(classFeeDTO);
        classFee = classFeeRepository.save(classFee);
        return classFeeMapper.toDto(classFee);
    }

    /**
     * Partially update a classFee.
     *
     * @param classFeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClassFeeDTO> partialUpdate(ClassFeeDTO classFeeDTO) {
        log.debug("Request to partially update ClassFee : {}", classFeeDTO);

        return classFeeRepository
            .findById(classFeeDTO.getId())
            .map(
                existingClassFee -> {
                    classFeeMapper.partialUpdate(existingClassFee, classFeeDTO);
                    return existingClassFee;
                }
            )
            .map(classFeeRepository::save)
            .map(classFeeMapper::toDto);
    }

    /**
     * Get all the classFees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClassFeeDTO> findAll() {
        log.debug("Request to get all ClassFees");
        return classFeeRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(classFeeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the classFees with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClassFeeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classFeeRepository.findAllWithEagerRelationships(pageable).map(classFeeMapper::toDto);
    }

    /**
     * Get one classFee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassFeeDTO> findOne(Long id) {
        log.debug("Request to get ClassFee : {}", id);
        return classFeeRepository.findOneWithEagerRelationships(id).map(classFeeMapper::toDto);
    }

    /**
     * Delete the classFee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassFee : {}", id);
        classFeeRepository.deleteById(id);
    }
}
