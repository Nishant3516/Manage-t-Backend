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

import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.service.SchoolQueryService;
import com.ssik.manageit.service.SchoolService;
import com.ssik.manageit.service.criteria.SchoolCriteria;
import com.ssik.manageit.service.dto.SchoolDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.School}.
 */
@RestController
@RequestMapping("/api")
public class SchoolResource {

	private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

	private static final String ENTITY_NAME = "school";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolService schoolService;

	private final SchoolRepository schoolRepository;

	private final SchoolQueryService schoolQueryService;

	public SchoolResource(SchoolService schoolService, SchoolRepository schoolRepository,
			SchoolQueryService schoolQueryService) {
		this.schoolService = schoolService;
		this.schoolRepository = schoolRepository;
		this.schoolQueryService = schoolQueryService;
	}

	/**
	 * {@code POST  /schools} : Create a new school.
	 *
	 * @param schoolDTO the schoolDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolDTO, or with status {@code 400 (Bad Request)} if
	 *         the school has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/schools")
	public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
		log.debug("REST request to save School : {}", schoolDTO);
		if (schoolDTO.getId() != null) {
			throw new BadRequestAlertException("A new school cannot already have an ID", ENTITY_NAME, "idexists");
		}
		SchoolDTO result = null;
		try {
			schoolDTO.setCreateDate(LocalDate.now());
			result = schoolService.save(schoolDTO);
		} catch (Exception e) {
			log.error("Error saving instance of school", e);
		}
		return ResponseEntity
				.created(new URI("/api/schools/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /schools/:id} : Updates an existing school.
	 *
	 * @param id        the id of the schoolDTO to save.
	 * @param schoolDTO the schoolDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolDTO, or with status {@code 400 (Bad Request)} if
	 *         the schoolDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/schools/{id}")
	public ResponseEntity<SchoolDTO> updateSchool(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
		log.debug("REST request to update School : {}, {}", id, schoolDTO);
		if (schoolDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolDTO.setLastModified(LocalDate.now());
		SchoolDTO result = schoolService.save(schoolDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDTO.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /schools/:id} : Partial updates given fields of an existing
	 * school, field will ignore if it is null
	 *
	 * @param id        the id of the schoolDTO to save.
	 * @param schoolDTO the schoolDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolDTO, or with status {@code 400 (Bad Request)} if
	 *         the schoolDTO is not valid, or with status {@code 404 (Not Found)} if
	 *         the schoolDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/schools/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolDTO> partialUpdateSchool(@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
		log.debug("REST request to partial update School partially : {}, {}", id, schoolDTO);
		if (schoolDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolDTO.setLastModified(LocalDate.now());
		Optional<SchoolDTO> result = schoolService.partialUpdate(schoolDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDTO.getId().toString()));
	}

	/**
	 * {@code GET  /schools} : get all the schools.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schools in body.
	 */
	@GetMapping("/schools")
	public ResponseEntity<List<SchoolDTO>> getAllSchools(SchoolCriteria criteria) {
		log.debug("REST request to get Schools by criteria: {}", criteria);
		List<SchoolDTO> entityList = schoolQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /schools/count} : count all the schools.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/schools/count")
	public ResponseEntity<Long> countSchools(SchoolCriteria criteria) {
		log.debug("REST request to count Schools by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /schools/:id} : get the "id" school.
	 *
	 * @param id the id of the schoolDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/schools/{id}")
	public ResponseEntity<SchoolDTO> getSchool(@PathVariable Long id) {
		log.debug("REST request to get School : {}", id);
		Optional<SchoolDTO> schoolDTO = schoolService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolDTO);
	}

	/**
	 * {@code DELETE  /schools/:id} : delete the "id" school.
	 *
	 * @param id the id of the schoolDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/schools/{id}")
	public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
		log.debug("REST request to delete School : {}", id);
		schoolService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
