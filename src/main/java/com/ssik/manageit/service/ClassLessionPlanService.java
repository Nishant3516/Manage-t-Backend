package com.ssik.manageit.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.repository.ClassLessionPlanRepository;
import com.ssik.manageit.service.dto.ClassLessionPlanDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanMapper;

/**
 * Service Implementation for managing {@link ClassLessionPlan}.
 */
@Service
@Transactional
public class ClassLessionPlanService {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanService.class);

	private final ClassLessionPlanRepository classLessionPlanRepository;

	private final ClassLessionPlanMapper classLessionPlanMapper;

	public ClassLessionPlanService(ClassLessionPlanRepository classLessionPlanRepository,
			ClassLessionPlanMapper classLessionPlanMapper) {
		this.classLessionPlanRepository = classLessionPlanRepository;
		this.classLessionPlanMapper = classLessionPlanMapper;
	}

	/**
	 * Save a classLessionPlan.
	 *
	 * @param classLessionPlanDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ClassLessionPlanDTO save(ClassLessionPlanDTO classLessionPlanDTO) {
		log.debug("Request to save ClassLessionPlan : {}", classLessionPlanDTO);
		ClassLessionPlan classLessionPlan = classLessionPlanMapper.toEntity(classLessionPlanDTO);
		classLessionPlan = classLessionPlanRepository.save(classLessionPlan);
		return classLessionPlanMapper.toDto(classLessionPlan);
	}

	/**
	 * Partially update a classLessionPlan.
	 *
	 * @param classLessionPlanDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ClassLessionPlanDTO> partialUpdate(ClassLessionPlanDTO classLessionPlanDTO) {
		log.debug("Request to partially update ClassLessionPlan : {}", classLessionPlanDTO);

		return classLessionPlanRepository.findById(classLessionPlanDTO.getId()).map(existingClassLessionPlan -> {
			classLessionPlanMapper.partialUpdate(existingClassLessionPlan, classLessionPlanDTO);
			return existingClassLessionPlan;
		}).map(classLessionPlanRepository::save).map(classLessionPlanMapper::toDto);
	}

	/**
	 * Get all the classLessionPlans.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassLessionPlanDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ClassLessionPlans");
		return classLessionPlanRepository.findAll(pageable).map(classLessionPlanMapper::toDto);
	}

	/**
	 * Get one classLessionPlan by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ClassLessionPlan> findOne(Long id) {
		log.debug("Request to get ClassLessionPlan : {}", id);
		return classLessionPlanRepository.findById(id);
	}

	/**
	 * Delete the classLessionPlan by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ClassLessionPlan : {}", id);
		classLessionPlanRepository.deleteById(id);
	}
}
