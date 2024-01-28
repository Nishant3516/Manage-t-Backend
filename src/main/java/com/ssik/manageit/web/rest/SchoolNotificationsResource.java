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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.repository.SchoolNotificationsRepository;
import com.ssik.manageit.service.SchoolNotificationsQueryService;
import com.ssik.manageit.service.SchoolNotificationsService;
import com.ssik.manageit.service.criteria.SchoolNotificationsCriteria;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolNotifications}.
 */
@RestController
@RequestMapping("/api")
public class SchoolNotificationsResource {

	private final Logger log = LoggerFactory.getLogger(SchoolNotificationsResource.class);

	private static final String ENTITY_NAME = "schoolNotifications";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolNotificationsService schoolNotificationsService;

	private final SchoolNotificationsRepository schoolNotificationsRepository;

	private final SchoolNotificationsQueryService schoolNotificationsQueryService;

	public SchoolNotificationsResource(SchoolNotificationsService schoolNotificationsService,
			SchoolNotificationsRepository schoolNotificationsRepository,
			SchoolNotificationsQueryService schoolNotificationsQueryService) {
		this.schoolNotificationsService = schoolNotificationsService;
		this.schoolNotificationsRepository = schoolNotificationsRepository;
		this.schoolNotificationsQueryService = schoolNotificationsQueryService;
	}

	/**
	 * {@code POST  /school-notifications} : Create a new schoolNotifications.
	 *
	 * @param schoolNotificationsDTO the schoolNotificationsDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolNotificationsDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolNotifications has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-notifications")
	public ResponseEntity<SchoolNotificationsDTO> createSchoolNotifications(
			@Valid @RequestBody SchoolNotificationsDTO schoolNotificationsDTO) throws URISyntaxException {
		log.debug("REST request to save SchoolNotifications : {}", schoolNotificationsDTO);
		if (schoolNotificationsDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolNotifications cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		schoolNotificationsDTO.setCreateDate(LocalDate.now());
		SchoolNotificationsDTO result = schoolNotificationsService.save(schoolNotificationsDTO);
		return ResponseEntity
				.created(new URI("/api/school-notifications/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-notifications/:id} : Updates an existing
	 * schoolNotifications.
	 *
	 * @param id                     the id of the schoolNotificationsDTO to save.
	 * @param schoolNotificationsDTO the schoolNotificationsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolNotificationsDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolNotificationsDTO is not valid,
	 *         or with status {@code 500 (Internal Server Error)} if the
	 *         schoolNotificationsDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-notifications/{id}")
	public ResponseEntity<SchoolNotificationsDTO> updateSchoolNotifications(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolNotificationsDTO schoolNotificationsDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolNotifications : {}, {}", id, schoolNotificationsDTO);
		if (schoolNotificationsDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolNotificationsDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolNotificationsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolNotificationsDTO.setLastModified(LocalDate.now());
		SchoolNotificationsDTO result = schoolNotificationsService.save(schoolNotificationsDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolNotificationsDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-notifications/:id} : Partial updates given fields of an
	 * existing schoolNotifications, field will ignore if it is null
	 *
	 * @param id                     the id of the schoolNotificationsDTO to save.
	 * @param schoolNotificationsDTO the schoolNotificationsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolNotificationsDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolNotificationsDTO is not valid,
	 *         or with status {@code 404 (Not Found)} if the schoolNotificationsDTO
	 *         is not found, or with status {@code 500 (Internal Server Error)} if
	 *         the schoolNotificationsDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-notifications/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolNotificationsDTO> partialUpdateSchoolNotifications(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolNotificationsDTO schoolNotificationsDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolNotifications partially : {}, {}", id, schoolNotificationsDTO);
		if (schoolNotificationsDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolNotificationsDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolNotificationsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolNotificationsDTO.setLastModified(LocalDate.now());
		Optional<SchoolNotificationsDTO> result = schoolNotificationsService.partialUpdate(schoolNotificationsDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolNotificationsDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-notifications} : get all the schoolNotifications.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolNotifications in body.
	 */
	@GetMapping("/school-notifications")
	public ResponseEntity<List<SchoolNotificationsDTO>> getAllSchoolNotifications(SchoolNotificationsCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get SchoolNotifications by criteria: {}", criteria);
		Page<SchoolNotificationsDTO> page = schoolNotificationsQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /school-notifications/count} : count all the schoolNotifications.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-notifications/count")
	public ResponseEntity<Long> countSchoolNotifications(SchoolNotificationsCriteria criteria) {
		log.debug("REST request to count SchoolNotifications by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolNotificationsQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-notifications/:id} : get the "id" schoolNotifications.
	 *
	 * @param id the id of the schoolNotificationsDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolNotificationsDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-notifications/{id}")
	public ResponseEntity<SchoolNotificationsDTO> getSchoolNotifications(@PathVariable Long id) {
		log.debug("REST request to get SchoolNotifications : {}", id);
		Optional<SchoolNotificationsDTO> schoolNotificationsDTO = schoolNotificationsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolNotificationsDTO);
	}

	/**
	 * {@code DELETE  /school-notifications/:id} : delete the "id"
	 * schoolNotifications.
	 *
	 * @param id the id of the schoolNotificationsDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-notifications/{id}")
	public ResponseEntity<Void> deleteSchoolNotifications(@PathVariable Long id) {
		log.debug("REST request to delete SchoolNotifications : {}", id);
		schoolNotificationsService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
