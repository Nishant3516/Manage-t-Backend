package com.ssik.manageit.service;

import com.ssik.manageit.domain.StudentAttendence;
import com.ssik.manageit.repository.StudentAttendenceRepository;
import com.ssik.manageit.service.dto.StudentAttendenceDTO;
import com.ssik.manageit.service.mapper.StudentAttendenceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentAttendence}.
 */
@Service
@Transactional
public class StudentAttendenceService {

    private final Logger log = LoggerFactory.getLogger(StudentAttendenceService.class);

    private final StudentAttendenceRepository studentAttendenceRepository;

    private final StudentAttendenceMapper studentAttendenceMapper;

    public StudentAttendenceService(
        StudentAttendenceRepository studentAttendenceRepository,
        StudentAttendenceMapper studentAttendenceMapper
    ) {
        this.studentAttendenceRepository = studentAttendenceRepository;
        this.studentAttendenceMapper = studentAttendenceMapper;
    }

    /**
     * Save a studentAttendence.
     *
     * @param studentAttendenceDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentAttendenceDTO save(StudentAttendenceDTO studentAttendenceDTO) {
        log.debug("Request to save StudentAttendence : {}", studentAttendenceDTO);
        StudentAttendence studentAttendence = studentAttendenceMapper.toEntity(studentAttendenceDTO);
        studentAttendence = studentAttendenceRepository.save(studentAttendence);
        return studentAttendenceMapper.toDto(studentAttendence);
    }

    /**
     * Partially update a studentAttendence.
     *
     * @param studentAttendenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentAttendenceDTO> partialUpdate(StudentAttendenceDTO studentAttendenceDTO) {
        log.debug("Request to partially update StudentAttendence : {}", studentAttendenceDTO);

        return studentAttendenceRepository
            .findById(studentAttendenceDTO.getId())
            .map(
                existingStudentAttendence -> {
                    studentAttendenceMapper.partialUpdate(existingStudentAttendence, studentAttendenceDTO);
                    return existingStudentAttendence;
                }
            )
            .map(studentAttendenceRepository::save)
            .map(studentAttendenceMapper::toDto);
    }

    /**
     * Get all the studentAttendences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentAttendenceDTO> findAll() {
        log.debug("Request to get all StudentAttendences");
        return studentAttendenceRepository
            .findAll()
            .stream()
            .map(studentAttendenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one studentAttendence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentAttendenceDTO> findOne(Long id) {
        log.debug("Request to get StudentAttendence : {}", id);
        return studentAttendenceRepository.findById(id).map(studentAttendenceMapper::toDto);
    }

    /**
     * Delete the studentAttendence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentAttendence : {}", id);
        studentAttendenceRepository.deleteById(id);
    }
}
