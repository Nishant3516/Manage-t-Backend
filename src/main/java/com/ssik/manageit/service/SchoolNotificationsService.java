package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolNotifications;
import com.ssik.manageit.repository.SchoolNotificationsRepository;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;
import com.ssik.manageit.service.mapper.SchoolNotificationsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolNotifications}.
 */
@Service
@Transactional
public class SchoolNotificationsService {

    private final Logger log = LoggerFactory.getLogger(SchoolNotificationsService.class);

    private final SchoolNotificationsRepository schoolNotificationsRepository;

    private final SchoolNotificationsMapper schoolNotificationsMapper;

    public SchoolNotificationsService(
        SchoolNotificationsRepository schoolNotificationsRepository,
        SchoolNotificationsMapper schoolNotificationsMapper
    ) {
        this.schoolNotificationsRepository = schoolNotificationsRepository;
        this.schoolNotificationsMapper = schoolNotificationsMapper;
    }

    /**
     * Save a schoolNotifications.
     *
     * @param schoolNotificationsDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolNotificationsDTO save(SchoolNotificationsDTO schoolNotificationsDTO) {
        log.debug("Request to save SchoolNotifications : {}", schoolNotificationsDTO);
        SchoolNotifications schoolNotifications = schoolNotificationsMapper.toEntity(schoolNotificationsDTO);
        schoolNotifications = schoolNotificationsRepository.save(schoolNotifications);
        return schoolNotificationsMapper.toDto(schoolNotifications);
    }

    /**
     * Partially update a schoolNotifications.
     *
     * @param schoolNotificationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolNotificationsDTO> partialUpdate(SchoolNotificationsDTO schoolNotificationsDTO) {
        log.debug("Request to partially update SchoolNotifications : {}", schoolNotificationsDTO);

        return schoolNotificationsRepository
            .findById(schoolNotificationsDTO.getId())
            .map(
                existingSchoolNotifications -> {
                    schoolNotificationsMapper.partialUpdate(existingSchoolNotifications, schoolNotificationsDTO);
                    return existingSchoolNotifications;
                }
            )
            .map(schoolNotificationsRepository::save)
            .map(schoolNotificationsMapper::toDto);
    }

    /**
     * Get all the schoolNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolNotificationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolNotifications");
        return schoolNotificationsRepository.findAll(pageable).map(schoolNotificationsMapper::toDto);
    }

    /**
     * Get all the schoolNotifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolNotificationsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolNotificationsRepository.findAllWithEagerRelationships(pageable).map(schoolNotificationsMapper::toDto);
    }

    /**
     * Get one schoolNotifications by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolNotificationsDTO> findOne(Long id) {
        log.debug("Request to get SchoolNotifications : {}", id);
        return schoolNotificationsRepository.findOneWithEagerRelationships(id).map(schoolNotificationsMapper::toDto);
    }

    /**
     * Delete the schoolNotifications by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolNotifications : {}", id);
        schoolNotificationsRepository.deleteById(id);
    }
}
