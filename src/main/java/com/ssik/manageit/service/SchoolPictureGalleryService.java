package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolPictureGallery;
import com.ssik.manageit.repository.SchoolPictureGalleryRepository;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolPictureGalleryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolPictureGallery}.
 */
@Service
@Transactional
public class SchoolPictureGalleryService {

    private final Logger log = LoggerFactory.getLogger(SchoolPictureGalleryService.class);

    private final SchoolPictureGalleryRepository schoolPictureGalleryRepository;

    private final SchoolPictureGalleryMapper schoolPictureGalleryMapper;

    public SchoolPictureGalleryService(
        SchoolPictureGalleryRepository schoolPictureGalleryRepository,
        SchoolPictureGalleryMapper schoolPictureGalleryMapper
    ) {
        this.schoolPictureGalleryRepository = schoolPictureGalleryRepository;
        this.schoolPictureGalleryMapper = schoolPictureGalleryMapper;
    }

    /**
     * Save a schoolPictureGallery.
     *
     * @param schoolPictureGalleryDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolPictureGalleryDTO save(SchoolPictureGalleryDTO schoolPictureGalleryDTO) {
        log.debug("Request to save SchoolPictureGallery : {}", schoolPictureGalleryDTO);
        SchoolPictureGallery schoolPictureGallery = schoolPictureGalleryMapper.toEntity(schoolPictureGalleryDTO);
        schoolPictureGallery = schoolPictureGalleryRepository.save(schoolPictureGallery);
        return schoolPictureGalleryMapper.toDto(schoolPictureGallery);
    }

    /**
     * Partially update a schoolPictureGallery.
     *
     * @param schoolPictureGalleryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolPictureGalleryDTO> partialUpdate(SchoolPictureGalleryDTO schoolPictureGalleryDTO) {
        log.debug("Request to partially update SchoolPictureGallery : {}", schoolPictureGalleryDTO);

        return schoolPictureGalleryRepository
            .findById(schoolPictureGalleryDTO.getId())
            .map(
                existingSchoolPictureGallery -> {
                    schoolPictureGalleryMapper.partialUpdate(existingSchoolPictureGallery, schoolPictureGalleryDTO);
                    return existingSchoolPictureGallery;
                }
            )
            .map(schoolPictureGalleryRepository::save)
            .map(schoolPictureGalleryMapper::toDto);
    }

    /**
     * Get all the schoolPictureGalleries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolPictureGalleryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolPictureGalleries");
        return schoolPictureGalleryRepository.findAll(pageable).map(schoolPictureGalleryMapper::toDto);
    }

    /**
     * Get all the schoolPictureGalleries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolPictureGalleryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolPictureGalleryRepository.findAllWithEagerRelationships(pageable).map(schoolPictureGalleryMapper::toDto);
    }

    /**
     * Get one schoolPictureGallery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolPictureGalleryDTO> findOne(Long id) {
        log.debug("Request to get SchoolPictureGallery : {}", id);
        return schoolPictureGalleryRepository.findOneWithEagerRelationships(id).map(schoolPictureGalleryMapper::toDto);
    }

    /**
     * Delete the schoolPictureGallery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolPictureGallery : {}", id);
        schoolPictureGalleryRepository.deleteById(id);
    }
}
