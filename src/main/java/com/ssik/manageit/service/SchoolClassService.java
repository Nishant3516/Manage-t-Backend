package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.repository.SchoolClassRepository;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.mapper.SchoolClassMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolClass}.
 */
@Service
@Transactional
public class SchoolClassService {

    private final Logger log = LoggerFactory.getLogger(SchoolClassService.class);

    private final SchoolClassRepository schoolClassRepository;

    private final SchoolClassMapper schoolClassMapper;

    public SchoolClassService(SchoolClassRepository schoolClassRepository, SchoolClassMapper schoolClassMapper) {
        this.schoolClassRepository = schoolClassRepository;
        this.schoolClassMapper = schoolClassMapper;
    }

    /**
     * Save a schoolClass.
     *
     * @param schoolClassDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolClassDTO save(SchoolClassDTO schoolClassDTO) {
        log.debug("Request to save SchoolClass : {}", schoolClassDTO);
        SchoolClass schoolClass = schoolClassMapper.toEntity(schoolClassDTO);
        schoolClass = schoolClassRepository.save(schoolClass);
        return schoolClassMapper.toDto(schoolClass);
    }

    /**
     * Partially update a schoolClass.
     *
     * @param schoolClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolClassDTO> partialUpdate(SchoolClassDTO schoolClassDTO) {
        log.debug("Request to partially update SchoolClass : {}", schoolClassDTO);

        return schoolClassRepository
            .findById(schoolClassDTO.getId())
            .map(
                existingSchoolClass -> {
                    schoolClassMapper.partialUpdate(existingSchoolClass, schoolClassDTO);
                    return existingSchoolClass;
                }
            )
            .map(schoolClassRepository::save)
            .map(schoolClassMapper::toDto);
    }

    /**
     * Get all the schoolClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolClassDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolClasses");
        return schoolClassRepository.findAll(pageable).map(schoolClassMapper::toDto);
    }

    /**
     * Get one schoolClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolClassDTO> findOne(Long id) {
        log.debug("Request to get SchoolClass : {}", id);
        return schoolClassRepository.findById(id).map(schoolClassMapper::toDto);
    }

    @Transactional(readOnly = true)
    public SchoolClassDTO findDto(Long id) {
        log.debug("Request to get SchoolClass : {}", id);
        Optional<SchoolClass> schpoolClassOpt = schoolClassRepository.findById(id);
        if (schpoolClassOpt.isPresent()) {
            return schoolClassMapper.toDto(schpoolClassOpt.get());
        } else {
            return null;
        }
    }

    /**
     * Delete the schoolClass by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolClass : {}", id);
        schoolClassRepository.deleteById(id);
    }
}
