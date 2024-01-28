package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolDaysOff;
import com.ssik.manageit.repository.SchoolDaysOffRepository;
import com.ssik.manageit.service.dto.SchoolDaysOffDTO;
import com.ssik.manageit.service.mapper.SchoolDaysOffMapper;
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
 * Service Implementation for managing {@link SchoolDaysOff}.
 */
@Service
@Transactional
public class SchoolDaysOffService {

    private final Logger log = LoggerFactory.getLogger(SchoolDaysOffService.class);

    private final SchoolDaysOffRepository schoolDaysOffRepository;

    private final SchoolDaysOffMapper schoolDaysOffMapper;

    public SchoolDaysOffService(SchoolDaysOffRepository schoolDaysOffRepository, SchoolDaysOffMapper schoolDaysOffMapper) {
        this.schoolDaysOffRepository = schoolDaysOffRepository;
        this.schoolDaysOffMapper = schoolDaysOffMapper;
    }

    /**
     * Save a schoolDaysOff.
     *
     * @param schoolDaysOffDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolDaysOffDTO save(SchoolDaysOffDTO schoolDaysOffDTO) {
        log.debug("Request to save SchoolDaysOff : {}", schoolDaysOffDTO);
        SchoolDaysOff schoolDaysOff = schoolDaysOffMapper.toEntity(schoolDaysOffDTO);
        schoolDaysOff = schoolDaysOffRepository.save(schoolDaysOff);
        return schoolDaysOffMapper.toDto(schoolDaysOff);
    }

    /**
     * Partially update a schoolDaysOff.
     *
     * @param schoolDaysOffDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolDaysOffDTO> partialUpdate(SchoolDaysOffDTO schoolDaysOffDTO) {
        log.debug("Request to partially update SchoolDaysOff : {}", schoolDaysOffDTO);

        return schoolDaysOffRepository
            .findById(schoolDaysOffDTO.getId())
            .map(
                existingSchoolDaysOff -> {
                    schoolDaysOffMapper.partialUpdate(existingSchoolDaysOff, schoolDaysOffDTO);
                    return existingSchoolDaysOff;
                }
            )
            .map(schoolDaysOffRepository::save)
            .map(schoolDaysOffMapper::toDto);
    }

    /**
     * Get all the schoolDaysOffs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolDaysOffDTO> findAll() {
        log.debug("Request to get all SchoolDaysOffs");
        return schoolDaysOffRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(schoolDaysOffMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the schoolDaysOffs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolDaysOffDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolDaysOffRepository.findAllWithEagerRelationships(pageable).map(schoolDaysOffMapper::toDto);
    }

    /**
     * Get one schoolDaysOff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolDaysOffDTO> findOne(Long id) {
        log.debug("Request to get SchoolDaysOff : {}", id);
        return schoolDaysOffRepository.findOneWithEagerRelationships(id).map(schoolDaysOffMapper::toDto);
    }

    /**
     * Delete the schoolDaysOff by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolDaysOff : {}", id);
        schoolDaysOffRepository.deleteById(id);
    }
}
