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

import com.ssik.manageit.repository.SchoolUserRepository;
import com.ssik.manageit.service.SchoolUserQueryService;
import com.ssik.manageit.service.SchoolUserService;
import com.ssik.manageit.service.criteria.SchoolUserCriteria;
import com.ssik.manageit.service.dto.SchoolUserDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SchoolUser}.
 */
@RestController
@RequestMapping("/api")
public class SchoolUserResource {

	private final Logger log = LoggerFactory.getLogger(SchoolUserResource.class);

	private static final String ENTITY_NAME = "schoolUser";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolUserService schoolUserService;

	private final SchoolUserRepository schoolUserRepository;

	private final SchoolUserQueryService schoolUserQueryService;

	public SchoolUserResource(SchoolUserService schoolUserService, SchoolUserRepository schoolUserRepository,
			SchoolUserQueryService schoolUserQueryService) {
		this.schoolUserService = schoolUserService;
		this.schoolUserRepository = schoolUserRepository;
		this.schoolUserQueryService = schoolUserQueryService;
	}

	/**
	 * {@code POST  /school-users} : Create a new schoolUser.
	 *
	 * @param schoolUserDTO the schoolUserDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolUserDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolUser has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-users")
	public ResponseEntity<SchoolUserDTO> createSchoolUser(@Valid @RequestBody SchoolUserDTO schoolUserDTO)
			throws URISyntaxException {
		log.debug("REST request to save SchoolUser : {}", schoolUserDTO);
		if (schoolUserDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolUser cannot already have an ID", ENTITY_NAME, "idexists");
		}
		schoolUserDTO.setCreateDate(LocalDate.now());
		SchoolUserDTO result = schoolUserService.save(schoolUserDTO);
		return ResponseEntity
				.created(new URI("/api/school-users/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-users/:id} : Updates an existing schoolUser.
	 *
	 * @param id            the id of the schoolUserDTO to save.
	 * @param schoolUserDTO the schoolUserDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolUserDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolUserDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolUserDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-users/{id}")
	public ResponseEntity<SchoolUserDTO> updateSchoolUser(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolUserDTO schoolUserDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolUser : {}, {}", id, schoolUserDTO);
		if (schoolUserDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolUserDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolUserRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolUserDTO.setLastModified(LocalDate.now());
		SchoolUserDTO result = schoolUserService.save(schoolUserDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolUserDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-users/:id} : Partial updates given fields of an
	 * existing schoolUser, field will ignore if it is null
	 *
	 * @param id            the id of the schoolUserDTO to save.
	 * @param schoolUserDTO the schoolUserDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolUserDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolUserDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the schoolUserDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the schoolUserDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-users/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolUserDTO> partialUpdateSchoolUser(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolUserDTO schoolUserDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolUser partially : {}, {}", id, schoolUserDTO);
		if (schoolUserDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolUserDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolUserRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolUserDTO.setLastModified(LocalDate.now());

		Optional<SchoolUserDTO> result = schoolUserService.partialUpdate(schoolUserDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolUserDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-users} : get all the schoolUsers.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolUsers in body.
	 */
	@GetMapping("/school-users")
	public ResponseEntity<List<SchoolUserDTO>> getAllSchoolUsers(SchoolUserCriteria criteria, Pageable pageable) {
		log.debug("REST request to get SchoolUsers by criteria: {}", criteria);
		Page<SchoolUserDTO> page = schoolUserQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /school-users/count} : count all the schoolUsers.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-users/count")
	public ResponseEntity<Long> countSchoolUsers(SchoolUserCriteria criteria) {
		log.debug("REST request to count SchoolUsers by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolUserQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-users/:id} : get the "id" schoolUser.
	 *
	 * @param id the id of the schoolUserDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolUserDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-users/{id}")
	public ResponseEntity<SchoolUserDTO> getSchoolUser(@PathVariable Long id) {
		log.debug("REST request to get SchoolUser : {}", id);
		Optional<SchoolUserDTO> schoolUserDTO = schoolUserService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolUserDTO);
	}

	/**
	 * {@code DELETE  /school-users/:id} : delete the "id" schoolUser.
	 *
	 * @param id the id of the schoolUserDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-users/{id}")
	public ResponseEntity<Void> deleteSchoolUser(@PathVariable Long id) {
		log.debug("REST request to delete SchoolUser : {}", id);
		// schoolUserService.delete(id);
		Optional<SchoolUserDTO> schoolUserOpt = schoolUserService.findOne(id);
		if (schoolUserOpt.isPresent()) {
			SchoolUserDTO schoolUser = schoolUserOpt.get();
			schoolUser.setCancelDate(LocalDate.now());
			schoolUserService.save(schoolUser);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
