package com.ssik.manageit.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassLessionPlanTrack;
import com.ssik.manageit.repository.ClassLessionPlanTrackRepository;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanTrackMapper;

/**
 * Service Implementation for managing {@link ClassLessionPlanTrack}.
 */
@Service
@Transactional
public class ClassLessionPlanTrackService {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanTrackService.class);

	private final ClassLessionPlanTrackRepository classLessionPlanTrackRepository;

	private final ClassLessionPlanTrackMapper classLessionPlanTrackMapper;

	public ClassLessionPlanTrackService(ClassLessionPlanTrackRepository classLessionPlanTrackRepository,
			ClassLessionPlanTrackMapper classLessionPlanTrackMapper) {
		this.classLessionPlanTrackRepository = classLessionPlanTrackRepository;
		this.classLessionPlanTrackMapper = classLessionPlanTrackMapper;
	}

	/**
	 * Save a classLessionPlanTrack.
	 *
	 * @param classLessionPlanTrackDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ClassLessionPlanTrackDTO save(ClassLessionPlanTrackDTO classLessionPlanTrackDTO) {
		log.debug("Request to save ClassLessionPlanTrack : {}", classLessionPlanTrackDTO);
		ClassLessionPlanTrack classLessionPlanTrack = classLessionPlanTrackMapper.toEntity(classLessionPlanTrackDTO);
		classLessionPlanTrack = classLessionPlanTrackRepository.save(classLessionPlanTrack);
		return classLessionPlanTrackMapper.toDto(classLessionPlanTrack);
	}

	/**
	 * Partially update a classLessionPlanTrack.
	 *
	 * @param classLessionPlanTrackDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ClassLessionPlanTrackDTO> partialUpdate(ClassLessionPlanTrackDTO classLessionPlanTrackDTO) {
		log.debug("Request to partially update ClassLessionPlanTrack : {}", classLessionPlanTrackDTO);

		return classLessionPlanTrackRepository.findById(classLessionPlanTrackDTO.getId())
				.map(existingClassLessionPlanTrack -> {
					classLessionPlanTrackMapper.partialUpdate(existingClassLessionPlanTrack, classLessionPlanTrackDTO);
					return existingClassLessionPlanTrack;
				}).map(classLessionPlanTrackRepository::save).map(classLessionPlanTrackMapper::toDto);
	}

	/**
	 * Get all the classLessionPlanTracks.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassLessionPlanTrackDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ClassLessionPlanTracks");
		return classLessionPlanTrackRepository.findAll(pageable).map(classLessionPlanTrackMapper::toDto);
	}

	/**
	 * Get one classLessionPlanTrack by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ClassLessionPlanTrackDTO> findOne(Long id) {
		log.debug("Request to get ClassLessionPlanTrack : {}", id);
		return classLessionPlanTrackRepository.findById(id).map(classLessionPlanTrackMapper::toDto);
	}

	/**
	 * Delete the classLessionPlanTrack by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ClassLessionPlanTrack : {}", id);
		classLessionPlanTrackRepository.deleteById(id);
	}
}
