package com.ssik.manageit.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.repository.StudentHomeWorkTrackRepository;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentHomeWorkTrackMapper;

/**
 * Service Implementation for managing {@link StudentHomeWorkTrack}.
 */
@Service
@Transactional
public class StudentHomeWorkTrackService {

	private final Logger log = LoggerFactory.getLogger(StudentHomeWorkTrackService.class);

	private final StudentHomeWorkTrackRepository studentHomeWorkTrackRepository;

	private final StudentHomeWorkTrackMapper studentHomeWorkTrackMapper;

	public StudentHomeWorkTrackService(StudentHomeWorkTrackRepository studentHomeWorkTrackRepository,
			StudentHomeWorkTrackMapper studentHomeWorkTrackMapper) {
		this.studentHomeWorkTrackRepository = studentHomeWorkTrackRepository;
		this.studentHomeWorkTrackMapper = studentHomeWorkTrackMapper;
	}

	/**
	 * Save a studentHomeWorkTrack.
	 *
	 * @param studentHomeWorkTrackDTO the entity to save.
	 * @return the persisted entity.
	 */
	public StudentHomeWorkTrackDTO save(StudentHomeWorkTrackDTO studentHomeWorkTrackDTO) {
		log.debug("Request to save StudentHomeWorkTrack : {}", studentHomeWorkTrackDTO);
		StudentHomeWorkTrack studentHomeWorkTrack = studentHomeWorkTrackMapper.toEntity(studentHomeWorkTrackDTO);
		studentHomeWorkTrack = studentHomeWorkTrackRepository.save(studentHomeWorkTrack);
		return studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);
	}

	public List<StudentHomeWorkTrack> saveAll(List<StudentHomeWorkTrack> studentHomeWorkTracks) {
		log.debug("Request to save StudentHomeWorkTrack : {}", studentHomeWorkTracks);
		return studentHomeWorkTrackRepository.saveAll(studentHomeWorkTracks);

	}

	/**
	 * Partially update a studentHomeWorkTrack.
	 *
	 * @param studentHomeWorkTrackDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<StudentHomeWorkTrackDTO> partialUpdate(StudentHomeWorkTrackDTO studentHomeWorkTrackDTO) {
		log.debug("Request to partially update StudentHomeWorkTrack : {}", studentHomeWorkTrackDTO);

		return studentHomeWorkTrackRepository.findById(studentHomeWorkTrackDTO.getId())
				.map(existingStudentHomeWorkTrack -> {
					studentHomeWorkTrackMapper.partialUpdate(existingStudentHomeWorkTrack, studentHomeWorkTrackDTO);
					return existingStudentHomeWorkTrack;
				}).map(studentHomeWorkTrackRepository::save).map(studentHomeWorkTrackMapper::toDto);
	}

	/**
	 * Get all the studentHomeWorkTracks.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<StudentHomeWorkTrackDTO> findAll(Pageable pageable) {
		log.debug("Request to get all StudentHomeWorkTracks");
		return studentHomeWorkTrackRepository.findAll(pageable).map(studentHomeWorkTrackMapper::toDto);
	}

	/**
	 * Get one studentHomeWorkTrack by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<StudentHomeWorkTrack> findOne(Long id) {
		log.debug("Request to get StudentHomeWorkTrack : {}", id);
		return studentHomeWorkTrackRepository.findById(id);
	}

	/**
	 * Delete the studentHomeWorkTrack by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete StudentHomeWorkTrack : {}", id);
		studentHomeWorkTrackRepository.deleteById(id);
	}
}
