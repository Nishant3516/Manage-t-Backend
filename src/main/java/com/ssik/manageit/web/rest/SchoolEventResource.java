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

import com.ssik.manageit.repository.SchoolEventRepository;
import com.ssik.manageit.service.SchoolEventQueryService;
import com.ssik.manageit.service.SchoolEventService;
import com.ssik.manageit.service.criteria.SchoolEventCriteria;
import com.ssik.manageit.service.dto.SchoolEventDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SchoolEvent}.
 */
@RestController
@RequestMapping("/api")
public class SchoolEventResource {

	private final Logger log = LoggerFactory.getLogger(SchoolEventResource.class);

	private static final String ENTITY_NAME = "schoolEvent";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolEventService schoolEventService;

	private final SchoolEventRepository schoolEventRepository;

	private final SchoolEventQueryService schoolEventQueryService;

	public SchoolEventResource(SchoolEventService schoolEventService, SchoolEventRepository schoolEventRepository,
			SchoolEventQueryService schoolEventQueryService) {
		this.schoolEventService = schoolEventService;
		this.schoolEventRepository = schoolEventRepository;
		this.schoolEventQueryService = schoolEventQueryService;
	}

	/**
	 * {@code POST  /school-events} : Create a new schoolEvent.
	 *
	 * @param schoolEventDTO the schoolEventDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolEventDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolEvent has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-events")
	public ResponseEntity<SchoolEventDTO> createSchoolEvent(@Valid @RequestBody SchoolEventDTO schoolEventDTO)
			throws URISyntaxException {
		log.debug("REST request to save SchoolEvent : {}", schoolEventDTO);
		if (schoolEventDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolEvent cannot already have an ID", ENTITY_NAME, "idexists");
		}
		schoolEventDTO.setCreateDate(LocalDate.now());
		SchoolEventDTO result = schoolEventService.save(schoolEventDTO);
		return ResponseEntity
				.created(new URI("/api/school-events/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-events/:id} : Updates an existing schoolEvent.
	 *
	 * @param id             the id of the schoolEventDTO to save.
	 * @param schoolEventDTO the schoolEventDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolEventDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolEventDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolEventDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-events/{id}")
	public ResponseEntity<SchoolEventDTO> updateSchoolEvent(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolEventDTO schoolEventDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolEvent : {}, {}", id, schoolEventDTO);
		if (schoolEventDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolEventDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolEventRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolEventDTO.setLastModified(LocalDate.now());
		SchoolEventDTO result = schoolEventService.save(schoolEventDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolEventDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-events/:id} : Partial updates given fields of an
	 * existing schoolEvent, field will ignore if it is null
	 *
	 * @param id             the id of the schoolEventDTO to save.
	 * @param schoolEventDTO the schoolEventDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolEventDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolEventDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the schoolEventDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the schoolEventDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-events/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolEventDTO> partialUpdateSchoolEvent(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolEventDTO schoolEventDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolEvent partially : {}, {}", id, schoolEventDTO);
		if (schoolEventDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolEventDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolEventRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolEventDTO.setLastModified(LocalDate.now());
		Optional<SchoolEventDTO> result = schoolEventService.partialUpdate(schoolEventDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolEventDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-events} : get all the schoolEvents.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolEvents in body.
	 */
	@GetMapping("/school-events")
	public ResponseEntity<List<SchoolEventDTO>> getAllSchoolEvents(SchoolEventCriteria criteria) {
		log.debug("REST request to get SchoolEvents by criteria: {}", criteria);
		List<SchoolEventDTO> entityList = schoolEventQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /school-events/count} : count all the schoolEvents.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-events/count")
	public ResponseEntity<Long> countSchoolEvents(SchoolEventCriteria criteria) {
		log.debug("REST request to count SchoolEvents by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolEventQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-events/:id} : get the "id" schoolEvent.
	 *
	 * @param id the id of the schoolEventDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolEventDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-events/{id}")
	public ResponseEntity<SchoolEventDTO> getSchoolEvent(@PathVariable Long id) {
		log.debug("REST request to get SchoolEvent : {}", id);
		Optional<SchoolEventDTO> schoolEventDTO = schoolEventService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolEventDTO);
	}

	/**
	 * {@code DELETE  /school-events/:id} : delete the "id" schoolEvent.
	 *
	 * @param id the id of the schoolEventDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-events/{id}")
	public ResponseEntity<Void> deleteSchoolEvent(@PathVariable Long id) {
		log.debug("REST request to delete SchoolEvent : {}", id);
		schoolEventService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
