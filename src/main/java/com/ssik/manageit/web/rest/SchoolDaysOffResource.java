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

import com.ssik.manageit.repository.SchoolDaysOffRepository;
import com.ssik.manageit.service.SchoolDaysOffQueryService;
import com.ssik.manageit.service.SchoolDaysOffService;
import com.ssik.manageit.service.criteria.SchoolDaysOffCriteria;
import com.ssik.manageit.service.dto.SchoolDaysOffDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SchoolDaysOff}.
 */
@RestController
@RequestMapping("/api")
public class SchoolDaysOffResource {

	private final Logger log = LoggerFactory.getLogger(SchoolDaysOffResource.class);

	private static final String ENTITY_NAME = "schoolDaysOff";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolDaysOffService schoolDaysOffService;

	private final SchoolDaysOffRepository schoolDaysOffRepository;

	private final SchoolDaysOffQueryService schoolDaysOffQueryService;

	public SchoolDaysOffResource(SchoolDaysOffService schoolDaysOffService,
			SchoolDaysOffRepository schoolDaysOffRepository, SchoolDaysOffQueryService schoolDaysOffQueryService) {
		this.schoolDaysOffService = schoolDaysOffService;
		this.schoolDaysOffRepository = schoolDaysOffRepository;
		this.schoolDaysOffQueryService = schoolDaysOffQueryService;
	}

	/**
	 * {@code POST  /school-days-offs} : Create a new schoolDaysOff.
	 *
	 * @param schoolDaysOffDTO the schoolDaysOffDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolDaysOffDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolDaysOff has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-days-offs")
	public ResponseEntity<SchoolDaysOffDTO> createSchoolDaysOff(@Valid @RequestBody SchoolDaysOffDTO schoolDaysOffDTO)
			throws URISyntaxException {
		log.debug("REST request to save SchoolDaysOff : {}", schoolDaysOffDTO);
		if (schoolDaysOffDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolDaysOff cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		schoolDaysOffDTO.setCreateDate(LocalDate.now());
		SchoolDaysOffDTO result = schoolDaysOffService.save(schoolDaysOffDTO);
		return ResponseEntity
				.created(new URI("/api/school-days-offs/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-days-offs/:id} : Updates an existing schoolDaysOff.
	 *
	 * @param id               the id of the schoolDaysOffDTO to save.
	 * @param schoolDaysOffDTO the schoolDaysOffDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolDaysOffDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolDaysOffDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         schoolDaysOffDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-days-offs/{id}")
	public ResponseEntity<SchoolDaysOffDTO> updateSchoolDaysOff(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolDaysOffDTO schoolDaysOffDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolDaysOff : {}, {}", id, schoolDaysOffDTO);
		if (schoolDaysOffDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolDaysOffDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolDaysOffRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolDaysOffDTO.setLastModified(LocalDate.now());
		SchoolDaysOffDTO result = schoolDaysOffService.save(schoolDaysOffDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolDaysOffDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-days-offs/:id} : Partial updates given fields of an
	 * existing schoolDaysOff, field will ignore if it is null
	 *
	 * @param id               the id of the schoolDaysOffDTO to save.
	 * @param schoolDaysOffDTO the schoolDaysOffDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolDaysOffDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolDaysOffDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the schoolDaysOffDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         schoolDaysOffDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-days-offs/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolDaysOffDTO> partialUpdateSchoolDaysOff(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolDaysOffDTO schoolDaysOffDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolDaysOff partially : {}, {}", id, schoolDaysOffDTO);
		if (schoolDaysOffDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolDaysOffDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolDaysOffRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolDaysOffDTO.setLastModified(LocalDate.now());
		Optional<SchoolDaysOffDTO> result = schoolDaysOffService.partialUpdate(schoolDaysOffDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolDaysOffDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-days-offs} : get all the schoolDaysOffs.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolDaysOffs in body.
	 */
	@GetMapping("/school-days-offs")
	public ResponseEntity<List<SchoolDaysOffDTO>> getAllSchoolDaysOffs(SchoolDaysOffCriteria criteria) {
		log.debug("REST request to get SchoolDaysOffs by criteria: {}", criteria);
		List<SchoolDaysOffDTO> entityList = schoolDaysOffQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /school-days-offs/count} : count all the schoolDaysOffs.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-days-offs/count")
	public ResponseEntity<Long> countSchoolDaysOffs(SchoolDaysOffCriteria criteria) {
		log.debug("REST request to count SchoolDaysOffs by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolDaysOffQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-days-offs/:id} : get the "id" schoolDaysOff.
	 *
	 * @param id the id of the schoolDaysOffDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolDaysOffDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-days-offs/{id}")
	public ResponseEntity<SchoolDaysOffDTO> getSchoolDaysOff(@PathVariable Long id) {
		log.debug("REST request to get SchoolDaysOff : {}", id);
		Optional<SchoolDaysOffDTO> schoolDaysOffDTO = schoolDaysOffService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolDaysOffDTO);
	}

	/**
	 * {@code DELETE  /school-days-offs/:id} : delete the "id" schoolDaysOff.
	 *
	 * @param id the id of the schoolDaysOffDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-days-offs/{id}")
	public ResponseEntity<Void> deleteSchoolDaysOff(@PathVariable Long id) {
		log.debug("REST request to delete SchoolDaysOff : {}", id);
		schoolDaysOffService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
