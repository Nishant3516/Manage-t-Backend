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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.repository.SchoolClassRepository;
import com.ssik.manageit.service.BackupService;
import com.ssik.manageit.service.SchoolClassQueryService;
import com.ssik.manageit.service.SchoolClassService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.criteria.SchoolClassCriteria;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.task.EmailTask;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SchoolClass}.
 */
@RestController
@RequestMapping("/api")
public class SchoolClassResource {

	private final Logger log = LoggerFactory.getLogger(SchoolClassResource.class);

	private static final String ENTITY_NAME = "schoolClass";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolClassService schoolClassService;

	private final SchoolClassRepository schoolClassRepository;

	private final SchoolClassQueryService schoolClassQueryService;
	private final SchoolCommonService schoolCommonService;
//	@Autowired
//	BackupService backupService;
	@Autowired
	EmailTask task;

	public SchoolClassResource(SchoolClassService schoolClassService, SchoolClassRepository schoolClassRepository,
			SchoolClassQueryService schoolClassQueryService, SchoolCommonService schoolCommonService) {
		this.schoolClassService = schoolClassService;
		this.schoolClassRepository = schoolClassRepository;
		this.schoolClassQueryService = schoolClassQueryService;
		this.schoolCommonService = schoolCommonService;
	}

	/**
	 * {@code POST  /school-classes} : Create a new schoolClass.
	 *
	 * @param schoolClassDTO the schoolClassDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolClassDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolClass has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-classes")
	public ResponseEntity<SchoolClassDTO> createSchoolClass(@Valid @RequestBody SchoolClassDTO schoolClassDTO)
			throws URISyntaxException {
		log.debug("REST request to save SchoolClass : {}", schoolClassDTO);
		if (schoolClassDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolClass cannot already have an ID", ENTITY_NAME, "idexists");
		}
		schoolClassDTO.setCreateDate(LocalDate.now());
		SchoolClassDTO result = schoolClassService.save(schoolClassDTO);
		return ResponseEntity
				.created(new URI("/api/school-classes/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-classes/:id} : Updates an existing schoolClass.
	 *
	 * @param id             the id of the schoolClassDTO to save.
	 * @param schoolClassDTO the schoolClassDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolClassDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolClassDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolClassDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-classes/{id}")
	public ResponseEntity<SchoolClassDTO> updateSchoolClass(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolClassDTO schoolClassDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolClass : {}, {}", id, schoolClassDTO);
		if (schoolClassDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolClassDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolClassRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolClassDTO.setLastModified(LocalDate.now());
		SchoolClassDTO result = schoolClassService.save(schoolClassDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolClassDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-classes/:id} : Partial updates given fields of an
	 * existing schoolClass, field will ignore if it is null
	 *
	 * @param id             the id of the schoolClassDTO to save.
	 * @param schoolClassDTO the schoolClassDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolClassDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolClassDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the schoolClassDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the schoolClassDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-classes/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolClassDTO> partialUpdateSchoolClass(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolClassDTO schoolClassDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolClass partially : {}, {}", id, schoolClassDTO);
		if (schoolClassDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolClassDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolClassRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolClassDTO.setLastModified(LocalDate.now());

		Optional<SchoolClassDTO> result = schoolClassService.partialUpdate(schoolClassDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolClassDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-classes} : get all the schoolClasses.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolClasses in body.
	 */
	@GetMapping("/school-classes")
	public ResponseEntity<List<SchoolClass>> getAllSchoolClasses(SchoolClassCriteria criteria, Pageable pageable) {
		log.debug("REST request to get SchoolClasses by criteria: {}", criteria);
		// This is just fo trying out the backup service , if this works or not
		// backupService.attemptScheduledDbBackup();
		School foundSchool = schoolCommonService.getUserSchool();
		LongFilter schoolFilter = new LongFilter();
		schoolFilter.setEquals(foundSchool.getId());
		criteria.setSchoolId(schoolFilter);
		List<SchoolClass> schoolClasses = schoolClassQueryService.findByCriteria(criteria, pageable);
		return ResponseEntity.ok().body(schoolClasses);
	}

	/**
	 * {@code GET  /school-classes/count} : count all the schoolClasses.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-classes/count")
	public ResponseEntity<Long> countSchoolClasses(SchoolClassCriteria criteria) {
		log.debug("REST request to count SchoolClasses by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolClassQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-classes/:id} : get the "id" schoolClass.
	 *
	 * @param id the id of the schoolClassDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolClassDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-classes/{id}")
	public ResponseEntity<SchoolClassDTO> getSchoolClass(@PathVariable Long id) {
		log.debug("REST request to get SchoolClass : {}", id);
		Optional<SchoolClassDTO> schoolClassDTO = schoolClassService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolClassDTO);
	}

	/**
	 * {@code DELETE  /school-classes/:id} : delete the "id" schoolClass.
	 *
	 * @param id the id of the schoolClassDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-classes/{id}")
	public ResponseEntity<Void> deleteSchoolClass(@PathVariable Long id) {
		log.debug("REST request to delete SchoolClass : {}", id);
		// schoolClassService.delete(id);
		Optional<SchoolClassDTO> schoolClassOpt = schoolClassService.findOne(id);
		if (schoolClassOpt.isPresent()) {
			SchoolClassDTO schoolClass = schoolClassOpt.get();
			schoolClass.setCancelDate(LocalDate.now());
			schoolClassService.save(schoolClass);
		}

		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
