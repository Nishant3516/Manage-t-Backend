package com.ssik.manageit.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.repository.StudentClassWorkTrackRepository;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentClassWorkTrackMapper;

/**
 * Service Implementation for managing {@link StudentClassWorkTrack}.
 */
@Service
@Transactional
public class StudentClassWorkTrackService {

	private final Logger log = LoggerFactory.getLogger(StudentClassWorkTrackService.class);

	private final StudentClassWorkTrackRepository studentClassWorkTrackRepository;

	private final StudentClassWorkTrackMapper studentClassWorkTrackMapper;

	public StudentClassWorkTrackService(StudentClassWorkTrackRepository studentClassWorkTrackRepository,
			StudentClassWorkTrackMapper studentClassWorkTrackMapper) {
		this.studentClassWorkTrackRepository = studentClassWorkTrackRepository;
		this.studentClassWorkTrackMapper = studentClassWorkTrackMapper;
	}

	/**
	 * Save a studentClassWorkTrack.
	 *
	 * @param studentClassWorkTrackDTO the entity to save.
	 * @return the persisted entity.
	 */
	public StudentClassWorkTrackDTO save(StudentClassWorkTrackDTO studentClassWorkTrackDTO) {
		log.debug("Request to save StudentClassWorkTrack : {}", studentClassWorkTrackDTO);
		StudentClassWorkTrack studentClassWorkTrack = studentClassWorkTrackMapper.toEntity(studentClassWorkTrackDTO);
		studentClassWorkTrack = studentClassWorkTrackRepository.save(studentClassWorkTrack);
		return studentClassWorkTrackMapper.toDto(studentClassWorkTrack);
	}

	public List<StudentClassWorkTrack> saveAll(List<StudentClassWorkTrack> studentClassWorkTracks) {
		log.debug("Request to save StudentClassWorkTrack : {}", studentClassWorkTracks);
		return studentClassWorkTrackRepository.saveAll(studentClassWorkTracks);
	}

	/**
	 * Partially update a studentClassWorkTrack.
	 *
	 * @param studentClassWorkTrackDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<StudentClassWorkTrackDTO> partialUpdate(StudentClassWorkTrackDTO studentClassWorkTrackDTO) {
		log.debug("Request to partially update StudentClassWorkTrack : {}", studentClassWorkTrackDTO);

		return studentClassWorkTrackRepository.findById(studentClassWorkTrackDTO.getId())
				.map(existingStudentClassWorkTrack -> {
					studentClassWorkTrackMapper.partialUpdate(existingStudentClassWorkTrack, studentClassWorkTrackDTO);
					return existingStudentClassWorkTrack;
				}).map(studentClassWorkTrackRepository::save).map(studentClassWorkTrackMapper::toDto);
	}

	/**
	 * Get all the studentClassWorkTracks.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<StudentClassWorkTrackDTO> findAll(Pageable pageable) {
		log.debug("Request to get all StudentClassWorkTracks");
		return studentClassWorkTrackRepository.findAll(pageable).map(studentClassWorkTrackMapper::toDto);
	}

	/**
	 * Get one studentClassWorkTrack by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<StudentClassWorkTrack> findOne(Long id) {
		log.debug("Request to get StudentClassWorkTrack : {}", id);
		return studentClassWorkTrackRepository.findById(id);
	}

	/**
	 * Delete the studentClassWorkTrack by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete StudentClassWorkTrack : {}", id);
		studentClassWorkTrackRepository.deleteById(id);
	}
}
