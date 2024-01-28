package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.repository.SchoolUserRepository;
import com.ssik.manageit.service.dto.SchoolUserDTO;
import com.ssik.manageit.service.mapper.SchoolUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolUser}.
 */
@Service
@Transactional
public class SchoolUserService {

    private final Logger log = LoggerFactory.getLogger(SchoolUserService.class);

    private final SchoolUserRepository schoolUserRepository;

    private final SchoolUserMapper schoolUserMapper;

    public SchoolUserService(SchoolUserRepository schoolUserRepository, SchoolUserMapper schoolUserMapper) {
        this.schoolUserRepository = schoolUserRepository;
        this.schoolUserMapper = schoolUserMapper;
    }

    /**
     * Save a schoolUser.
     *
     * @param schoolUserDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolUserDTO save(SchoolUserDTO schoolUserDTO) {
        log.debug("Request to save SchoolUser : {}", schoolUserDTO);
        SchoolUser schoolUser = schoolUserMapper.toEntity(schoolUserDTO);
        schoolUser = schoolUserRepository.save(schoolUser);
        return schoolUserMapper.toDto(schoolUser);
    }

    /**
     * Partially update a schoolUser.
     *
     * @param schoolUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolUserDTO> partialUpdate(SchoolUserDTO schoolUserDTO) {
        log.debug("Request to partially update SchoolUser : {}", schoolUserDTO);

        return schoolUserRepository
            .findById(schoolUserDTO.getId())
            .map(
                existingSchoolUser -> {
                    schoolUserMapper.partialUpdate(existingSchoolUser, schoolUserDTO);
                    return existingSchoolUser;
                }
            )
            .map(schoolUserRepository::save)
            .map(schoolUserMapper::toDto);
    }

    /**
     * Get all the schoolUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolUsers");
        return schoolUserRepository.findAll(pageable).map(schoolUserMapper::toDto);
    }

    /**
     * Get all the schoolUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolUserRepository.findAllWithEagerRelationships(pageable).map(schoolUserMapper::toDto);
    }

    /**
     * Get one schoolUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolUserDTO> findOne(Long id) {
        log.debug("Request to get SchoolUser : {}", id);
        return schoolUserRepository.findOneWithEagerRelationships(id).map(schoolUserMapper::toDto);
    }

    /**
     * Delete the schoolUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolUser : {}", id);
        schoolUserRepository.deleteById(id);
    }
}
