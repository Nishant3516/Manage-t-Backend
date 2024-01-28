package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.repository.ClassFeeRepository;
import com.ssik.manageit.service.ClassFeeQueryService;
import com.ssik.manageit.service.ClassFeeService;
import com.ssik.manageit.service.criteria.ClassFeeCriteria;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ClassFee}.
 */
@RestController
@RequestMapping("/api")
public class ClassFeeResource {

	private final Logger log = LoggerFactory.getLogger(ClassFeeResource.class);

	private static final String ENTITY_NAME = "classFee";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassFeeService classFeeService;

	private final ClassFeeRepository classFeeRepository;

	private final ClassFeeQueryService classFeeQueryService;

	public ClassFeeResource(ClassFeeService classFeeService, ClassFeeRepository classFeeRepository,
			ClassFeeQueryService classFeeQueryService) {
		this.classFeeService = classFeeService;
		this.classFeeRepository = classFeeRepository;
		this.classFeeQueryService = classFeeQueryService;
	}

	/**
	 * {@code POST  /class-fees} : Create a new classFee.
	 *
	 * @param classFeeDTO the classFeeDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classFeeDTO, or with status {@code 400 (Bad Request)} if
	 *         the classFee has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-fees")
	public ResponseEntity<ClassFeeDTO> createClassFee(@Valid @RequestBody ClassFeeDTO classFeeDTO)
			throws URISyntaxException {
		log.debug("REST request to save ClassFee : {}", classFeeDTO);
		if (classFeeDTO.getId() != null) {
			throw new BadRequestAlertException("A new classFee cannot already have an ID", ENTITY_NAME, "idexists");
		}
		classFeeDTO.setCreateDate(LocalDate.now());
		ClassFeeDTO result = classFeeService.save(classFeeDTO);
		return ResponseEntity
				.created(new URI("/api/class-fees/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-fees/:id} : Updates an existing classFee.
	 *
	 * @param id          the id of the classFeeDTO to save.
	 * @param classFeeDTO the classFeeDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classFeeDTO, or with status {@code 400 (Bad Request)} if
	 *         the classFeeDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the classFeeDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-fees/{id}")
	public ResponseEntity<ClassFeeDTO> updateClassFee(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassFeeDTO classFeeDTO) throws URISyntaxException {
		log.debug("REST request to update ClassFee : {}, {}", id, classFeeDTO);
		if (classFeeDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classFeeDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classFeeRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classFeeDTO.setLastModified(LocalDate.now());
		ClassFeeDTO result = classFeeService.save(classFeeDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classFeeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /class-fees/:id} : Partial updates given fields of an existing
	 * classFee, field will ignore if it is null
	 *
	 * @param id          the id of the classFeeDTO to save.
	 * @param classFeeDTO the classFeeDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classFeeDTO, or with status {@code 400 (Bad Request)} if
	 *         the classFeeDTO is not valid, or with status {@code 404 (Not Found)}
	 *         if the classFeeDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the classFeeDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-fees/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassFeeDTO> partialUpdateClassFee(
			@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody ClassFeeDTO classFeeDTO)
			throws URISyntaxException {
		log.debug("REST request to partial update ClassFee partially : {}, {}", id, classFeeDTO);
		if (classFeeDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classFeeDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classFeeRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classFeeDTO.setLastModified(LocalDate.now());

		Optional<ClassFeeDTO> result = classFeeService.partialUpdate(classFeeDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classFeeDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-fees} : get all the classFees.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classFees in body.
	 */
	@GetMapping("/class-fees")
	public ResponseEntity<List<ClassFeeDTO>> getAllClassFees(ClassFeeCriteria criteria) {
		log.debug("REST request to get ClassFees by criteria: {}", criteria);
		List<ClassFeeDTO> entityList = classFeeQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /class-fees/count} : count all the classFees.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-fees/count")
	public ResponseEntity<Long> countClassFees(ClassFeeCriteria criteria) {
		log.debug("REST request to count ClassFees by criteria: {}", criteria);
		return ResponseEntity.ok().body(classFeeQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-fees/:id} : get the "id" classFee.
	 *
	 * @param id the id of the classFeeDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classFeeDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-fees/{id}")
	public ResponseEntity<ClassFeeDTO> getClassFee(@PathVariable Long id) {
		log.debug("REST request to get ClassFee : {}", id);
		Optional<ClassFeeDTO> classFeeDTO = classFeeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classFeeDTO);
	}

	/**
	 * {@code DELETE  /class-fees/:id} : delete the "id" classFee.
	 *
	 * @param id the id of the classFeeDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-fees/{id}")
	public ResponseEntity<Void> deleteClassFee(@PathVariable Long id) {
		log.debug("REST request to delete ClassFee : {}", id);
		// classFeeService.delete(id);
		Optional<ClassFeeDTO> classFeeOpt = classFeeService.findOne(id);
		if (classFeeOpt.isPresent()) {
			ClassFeeDTO classFee = classFeeOpt.get();
			classFee.setCancelDate(LocalDate.now());
			classFeeService.save(classFee);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
