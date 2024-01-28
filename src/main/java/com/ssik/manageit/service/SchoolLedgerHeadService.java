package com.ssik.manageit.service;

import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.mapper.SchoolLedgerHeadMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SchoolLedgerHead}.
 */
@Service
@Transactional
public class SchoolLedgerHeadService {

    private final Logger log = LoggerFactory.getLogger(SchoolLedgerHeadService.class);

    private final SchoolLedgerHeadRepository schoolLedgerHeadRepository;

    private final SchoolLedgerHeadMapper schoolLedgerHeadMapper;

    public SchoolLedgerHeadService(SchoolLedgerHeadRepository schoolLedgerHeadRepository, SchoolLedgerHeadMapper schoolLedgerHeadMapper) {
        this.schoolLedgerHeadRepository = schoolLedgerHeadRepository;
        this.schoolLedgerHeadMapper = schoolLedgerHeadMapper;
    }

    /**
     * Save a schoolLedgerHead.
     *
     * @param schoolLedgerHeadDTO the entity to save.
     * @return the persisted entity.
     */
    public SchoolLedgerHeadDTO save(SchoolLedgerHeadDTO schoolLedgerHeadDTO) {
        log.debug("Request to save SchoolLedgerHead : {}", schoolLedgerHeadDTO);
        SchoolLedgerHead schoolLedgerHead = schoolLedgerHeadMapper.toEntity(schoolLedgerHeadDTO);
        schoolLedgerHead = schoolLedgerHeadRepository.save(schoolLedgerHead);
        return schoolLedgerHeadMapper.toDto(schoolLedgerHead);
    }

    /**
     * Partially update a schoolLedgerHead.
     *
     * @param schoolLedgerHeadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolLedgerHeadDTO> partialUpdate(SchoolLedgerHeadDTO schoolLedgerHeadDTO) {
        log.debug("Request to partially update SchoolLedgerHead : {}", schoolLedgerHeadDTO);

        return schoolLedgerHeadRepository
            .findById(schoolLedgerHeadDTO.getId())
            .map(
                existingSchoolLedgerHead -> {
                    schoolLedgerHeadMapper.partialUpdate(existingSchoolLedgerHead, schoolLedgerHeadDTO);
                    return existingSchoolLedgerHead;
                }
            )
            .map(schoolLedgerHeadRepository::save)
            .map(schoolLedgerHeadMapper::toDto);
    }

    /**
     * Get all the schoolLedgerHeads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolLedgerHeadDTO> findAll() {
        log.debug("Request to get all SchoolLedgerHeads");
        return schoolLedgerHeadRepository
            .findAll()
            .stream()
            .map(schoolLedgerHeadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one schoolLedgerHead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolLedgerHeadDTO> findOne(Long id) {
        log.debug("Request to get SchoolLedgerHead : {}", id);
        return schoolLedgerHeadRepository.findById(id).map(schoolLedgerHeadMapper::toDto);
    }

    /**
     * Delete the schoolLedgerHead by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolLedgerHead : {}", id);
        schoolLedgerHeadRepository.deleteById(id);
    }

    public SchoolLedgerHeadDTO getOneIfExists(Long id) {
        Optional<SchoolLedgerHeadDTO> schoolLedgerHeadDtoOpt = this.findOne(id);
        if (schoolLedgerHeadDtoOpt.isPresent()) {
            return schoolLedgerHeadDtoOpt.get();
        }
        return new SchoolLedgerHeadDTO();
    }
}
