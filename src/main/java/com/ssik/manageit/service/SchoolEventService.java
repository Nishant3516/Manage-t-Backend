package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolEvent;
import com.ssik.manageit.repository.SchoolEventRepository;
import com.ssik.manageit.service.dto.SchoolEventDTO;
import com.ssik.manageit.service.mapper.SchoolEventMapper;
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
 * Service Implementation for managing {@link SchoolEvent}.
 */
@Service
@Transactional
public class SchoolEventService {

    private final Logger log = LoggerFactory.getLogger(SchoolEventService.class);

    private final SchoolEventRepository schoolEventRepository;

    private final SchoolEventMapper schoolEventMapper;

    public SchoolEventService(SchoolEventRepository schoolEventRepository, SchoolEventMapper schoolEventMapper) {
        this.schoolEventRepository = schoolEventRepository;
        this.schoolEventMapper = schoolEventMapper;
    }

    /**
     * Save a schoolEvent.
     *
     * @param schoolEventDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolEventDTO save(SchoolEventDTO schoolEventDTO) {
        log.debug("Request to save SchoolEvent : {}", schoolEventDTO);
        SchoolEvent schoolEvent = schoolEventMapper.toEntity(schoolEventDTO);
        schoolEvent = schoolEventRepository.save(schoolEvent);
        return schoolEventMapper.toDto(schoolEvent);
    }

    /**
     * Partially update a schoolEvent.
     *
     * @param schoolEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolEventDTO> partialUpdate(SchoolEventDTO schoolEventDTO) {
        log.debug("Request to partially update SchoolEvent : {}", schoolEventDTO);

        return schoolEventRepository
            .findById(schoolEventDTO.getId())
            .map(
                existingSchoolEvent -> {
                    schoolEventMapper.partialUpdate(existingSchoolEvent, schoolEventDTO);
                    return existingSchoolEvent;
                }
            )
            .map(schoolEventRepository::save)
            .map(schoolEventMapper::toDto);
    }

    /**
     * Get all the schoolEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolEventDTO> findAll() {
        log.debug("Request to get all SchoolEvents");
        return schoolEventRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(schoolEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the schoolEvents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolEventDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolEventRepository.findAllWithEagerRelationships(pageable).map(schoolEventMapper::toDto);
    }

    /**
     * Get one schoolEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolEventDTO> findOne(Long id) {
        log.debug("Request to get SchoolEvent : {}", id);
        return schoolEventRepository.findOneWithEagerRelationships(id).map(schoolEventMapper::toDto);
    }

    /**
     * Delete the schoolEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolEvent : {}", id);
        schoolEventRepository.deleteById(id);
    }
}
