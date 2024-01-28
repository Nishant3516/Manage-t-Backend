package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolVideoGallery;
import com.ssik.manageit.repository.SchoolVideoGalleryRepository;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolVideoGalleryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolVideoGallery}.
 */
@Service
@Transactional
public class SchoolVideoGalleryService {

    private final Logger log = LoggerFactory.getLogger(SchoolVideoGalleryService.class);

    private final SchoolVideoGalleryRepository schoolVideoGalleryRepository;

    private final SchoolVideoGalleryMapper schoolVideoGalleryMapper;

    public SchoolVideoGalleryService(
        SchoolVideoGalleryRepository schoolVideoGalleryRepository,
        SchoolVideoGalleryMapper schoolVideoGalleryMapper
    ) {
        this.schoolVideoGalleryRepository = schoolVideoGalleryRepository;
        this.schoolVideoGalleryMapper = schoolVideoGalleryMapper;
    }

    /**
     * Save a schoolVideoGallery.
     *
     * @param schoolVideoGalleryDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolVideoGalleryDTO save(SchoolVideoGalleryDTO schoolVideoGalleryDTO) {
        log.debug("Request to save SchoolVideoGallery : {}", schoolVideoGalleryDTO);
        SchoolVideoGallery schoolVideoGallery = schoolVideoGalleryMapper.toEntity(schoolVideoGalleryDTO);
        schoolVideoGallery = schoolVideoGalleryRepository.save(schoolVideoGallery);
        return schoolVideoGalleryMapper.toDto(schoolVideoGallery);
    }

    /**
     * Partially update a schoolVideoGallery.
     *
     * @param schoolVideoGalleryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolVideoGalleryDTO> partialUpdate(SchoolVideoGalleryDTO schoolVideoGalleryDTO) {
        log.debug("Request to partially update SchoolVideoGallery : {}", schoolVideoGalleryDTO);

        return schoolVideoGalleryRepository
            .findById(schoolVideoGalleryDTO.getId())
            .map(
                existingSchoolVideoGallery -> {
                    schoolVideoGalleryMapper.partialUpdate(existingSchoolVideoGallery, schoolVideoGalleryDTO);
                    return existingSchoolVideoGallery;
                }
            )
            .map(schoolVideoGalleryRepository::save)
            .map(schoolVideoGalleryMapper::toDto);
    }

    /**
     * Get all the schoolVideoGalleries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolVideoGalleryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolVideoGalleries");
        return schoolVideoGalleryRepository.findAll(pageable).map(schoolVideoGalleryMapper::toDto);
    }

    /**
     * Get all the schoolVideoGalleries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolVideoGalleryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolVideoGalleryRepository.findAllWithEagerRelationships(pageable).map(schoolVideoGalleryMapper::toDto);
    }

    /**
     * Get one schoolVideoGallery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolVideoGalleryDTO> findOne(Long id) {
        log.debug("Request to get SchoolVideoGallery : {}", id);
        return schoolVideoGalleryRepository.findOneWithEagerRelationships(id).map(schoolVideoGalleryMapper::toDto);
    }

    /**
     * Delete the schoolVideoGallery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolVideoGallery : {}", id);
        schoolVideoGalleryRepository.deleteById(id);
    }
}
