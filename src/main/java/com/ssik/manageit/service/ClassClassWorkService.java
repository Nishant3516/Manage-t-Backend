package com.ssik.manageit.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.repository.ClassClassWorkRepository;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import com.ssik.manageit.service.mapper.ClassClassWorkMapper;

/**
 * Service Implementation for managing {@link ClassClassWork}.
 */
@Service
@Transactional
public class ClassClassWorkService {

	private final Logger log = LoggerFactory.getLogger(ClassClassWorkService.class);

	private final ClassClassWorkRepository classClassWorkRepository;

	private final ClassClassWorkMapper classClassWorkMapper;

	public ClassClassWorkService(ClassClassWorkRepository classClassWorkRepository,
			ClassClassWorkMapper classClassWorkMapper) {
		this.classClassWorkRepository = classClassWorkRepository;
		this.classClassWorkMapper = classClassWorkMapper;
	}

	/**
	 * Save a classClassWork.
	 *
	 * @param classClassWorkDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ClassClassWorkDTO save(ClassClassWorkDTO classClassWorkDTO) {
		log.debug("Request to save ClassClassWork : {}", classClassWorkDTO);
		ClassClassWork classClassWork = classClassWorkMapper.toEntity(classClassWorkDTO);
		classClassWork = classClassWorkRepository.save(classClassWork);
		return classClassWorkMapper.toDto(classClassWork);
	}

	/**
	 * Partially update a classClassWork.
	 *
	 * @param classClassWorkDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ClassClassWorkDTO> partialUpdate(ClassClassWorkDTO classClassWorkDTO) {
		log.debug("Request to partially update ClassClassWork : {}", classClassWorkDTO);

		return classClassWorkRepository.findById(classClassWorkDTO.getId()).map(existingClassClassWork -> {
			classClassWorkMapper.partialUpdate(existingClassClassWork, classClassWorkDTO);
			return existingClassClassWork;
		}).map(classClassWorkRepository::save).map(classClassWorkMapper::toDto);
	}

	/**
	 * Get all the classClassWorks.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassClassWorkDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ClassClassWorks");
		return classClassWorkRepository.findAll(pageable).map(classClassWorkMapper::toDto);
	}

	/**
	 * Get one classClassWork by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ClassClassWork> findOne(Long id) {
		log.debug("Request to get ClassClassWork : {}", id);
		return classClassWorkRepository.findById(id);
	}

	/**
	 * Delete the classClassWork by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ClassClassWork : {}", id);
		classClassWorkRepository.deleteById(id);
	}
}
