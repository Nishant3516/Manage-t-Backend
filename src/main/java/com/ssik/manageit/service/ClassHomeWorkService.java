package com.ssik.manageit.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.repository.ClassHomeWorkRepository;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import com.ssik.manageit.service.mapper.ClassHomeWorkMapper;

/**
 * Service Implementation for managing {@link ClassHomeWork}.
 */
@Service
@Transactional
public class ClassHomeWorkService {

	private final Logger log = LoggerFactory.getLogger(ClassHomeWorkService.class);

	private final ClassHomeWorkRepository classHomeWorkRepository;

	private final ClassHomeWorkMapper classHomeWorkMapper;

	public ClassHomeWorkService(ClassHomeWorkRepository classHomeWorkRepository,
			ClassHomeWorkMapper classHomeWorkMapper) {
		this.classHomeWorkRepository = classHomeWorkRepository;
		this.classHomeWorkMapper = classHomeWorkMapper;
	}

	/**
	 * Save a classHomeWork.
	 *
	 * @param classHomeWorkDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ClassHomeWorkDTO save(ClassHomeWorkDTO classHomeWorkDTO) {
		log.debug("Request to save ClassHomeWork : {}", classHomeWorkDTO);
		ClassHomeWork classHomeWork = classHomeWorkMapper.toEntity(classHomeWorkDTO);
		classHomeWork = classHomeWorkRepository.save(classHomeWork);
		return classHomeWorkMapper.toDto(classHomeWork);
	}

	/**
	 * Partially update a classHomeWork.
	 *
	 * @param classHomeWorkDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ClassHomeWorkDTO> partialUpdate(ClassHomeWorkDTO classHomeWorkDTO) {
		log.debug("Request to partially update ClassHomeWork : {}", classHomeWorkDTO);

		return classHomeWorkRepository.findById(classHomeWorkDTO.getId()).map(existingClassHomeWork -> {
			classHomeWorkMapper.partialUpdate(existingClassHomeWork, classHomeWorkDTO);
			return existingClassHomeWork;
		}).map(classHomeWorkRepository::save).map(classHomeWorkMapper::toDto);
	}

	/**
	 * Get all the classHomeWorks.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassHomeWorkDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ClassHomeWorks");
		return classHomeWorkRepository.findAll(pageable).map(classHomeWorkMapper::toDto);
	}

	/**
	 * Get one classHomeWork by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ClassHomeWork> findOne(Long id) {
		log.debug("Request to get ClassHomeWork : {}", id);
		return classHomeWorkRepository.findById(id);
	}

	/**
	 * Delete the classHomeWork by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ClassHomeWork : {}", id);
		classHomeWorkRepository.deleteById(id);
	}
}
