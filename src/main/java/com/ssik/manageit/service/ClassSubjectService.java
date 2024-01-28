package com.ssik.manageit.service;

import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.repository.ClassSubjectRepository;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import com.ssik.manageit.service.mapper.ClassSubjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassSubject}.
 */
@Service
@Transactional
public class ClassSubjectService {

    private final Logger log = LoggerFactory.getLogger(ClassSubjectService.class);

    private final ClassSubjectRepository classSubjectRepository;

    private final ClassSubjectMapper classSubjectMapper;

    public ClassSubjectService(ClassSubjectRepository classSubjectRepository, ClassSubjectMapper classSubjectMapper) {
        this.classSubjectRepository = classSubjectRepository;
        this.classSubjectMapper = classSubjectMapper;
    }

    /**
     * Save a classSubject.
     *
     * @param classSubjectDTO the entity to save.
     * @return the persisted entity.
     */
    public ClassSubjectDTO save(ClassSubjectDTO classSubjectDTO) {
        log.debug("Request to save ClassSubject : {}", classSubjectDTO);
        ClassSubject classSubject = classSubjectMapper.toEntity(classSubjectDTO);
        classSubject = classSubjectRepository.save(classSubject);
        return classSubjectMapper.toDto(classSubject);
    }

    /**
     * Partially update a classSubject.
     *
     * @param classSubjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClassSubjectDTO> partialUpdate(ClassSubjectDTO classSubjectDTO) {
        log.debug("Request to partially update ClassSubject : {}", classSubjectDTO);

        return classSubjectRepository
            .findById(classSubjectDTO.getId())
            .map(
                existingClassSubject -> {
                    classSubjectMapper.partialUpdate(existingClassSubject, classSubjectDTO);
                    return existingClassSubject;
                }
            )
            .map(classSubjectRepository::save)
            .map(classSubjectMapper::toDto);
    }

    /**
     * Get all the classSubjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassSubjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassSubjects");
        return classSubjectRepository.findAll(pageable).map(classSubjectMapper::toDto);
    }

    /**
     * Get all the classSubjects with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClassSubjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classSubjectRepository.findAllWithEagerRelationships(pageable).map(classSubjectMapper::toDto);
    }

    /**
     * Get one classSubject by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassSubjectDTO> findOne(Long id) {
        log.debug("Request to get ClassSubject : {}", id);
        return classSubjectRepository.findOneWithEagerRelationships(id).map(classSubjectMapper::toDto);
    }

    /**
     * Delete the classSubject by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassSubject : {}", id);
        classSubjectRepository.deleteById(id);
    }
}
