package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.repository.SchoolVideoGalleryRepository;
import com.ssik.manageit.service.SchoolVideoGalleryService;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolVideoGallery}.
 */
@RestController
@RequestMapping("/api")
public class SchoolVideoGalleryResource {

	private final Logger log = LoggerFactory.getLogger(SchoolVideoGalleryResource.class);

	private static final String ENTITY_NAME = "schoolVideoGallery";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolVideoGalleryService schoolVideoGalleryService;

	private final SchoolVideoGalleryRepository schoolVideoGalleryRepository;

	public SchoolVideoGalleryResource(SchoolVideoGalleryService schoolVideoGalleryService,
			SchoolVideoGalleryRepository schoolVideoGalleryRepository) {
		this.schoolVideoGalleryService = schoolVideoGalleryService;
		this.schoolVideoGalleryRepository = schoolVideoGalleryRepository;
	}

	/**
	 * {@code POST  /school-video-galleries} : Create a new schoolVideoGallery.
	 *
	 * @param schoolVideoGalleryDTO the schoolVideoGalleryDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolVideoGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolVideoGallery has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-video-galleries")
	public ResponseEntity<SchoolVideoGalleryDTO> createSchoolVideoGallery(
			@Valid @RequestBody SchoolVideoGalleryDTO schoolVideoGalleryDTO) throws URISyntaxException {
		log.debug("REST request to save SchoolVideoGallery : {}", schoolVideoGalleryDTO);
		if (schoolVideoGalleryDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolVideoGallery cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		schoolVideoGalleryDTO.setCreateDate(LocalDate.now());
		SchoolVideoGalleryDTO result = schoolVideoGalleryService.save(schoolVideoGalleryDTO);
		return ResponseEntity
				.created(new URI("/api/school-video-galleries/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-video-galleries/:id} : Updates an existing
	 * schoolVideoGallery.
	 *
	 * @param id                    the id of the schoolVideoGalleryDTO to save.
	 * @param schoolVideoGalleryDTO the schoolVideoGalleryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolVideoGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolVideoGalleryDTO is not valid,
	 *         or with status {@code 500 (Internal Server Error)} if the
	 *         schoolVideoGalleryDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-video-galleries/{id}")
	public ResponseEntity<SchoolVideoGalleryDTO> updateSchoolVideoGallery(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolVideoGalleryDTO schoolVideoGalleryDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolVideoGallery : {}, {}", id, schoolVideoGalleryDTO);
		if (schoolVideoGalleryDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolVideoGalleryDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolVideoGalleryRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolVideoGalleryDTO.setLastModified(LocalDate.now());
		SchoolVideoGalleryDTO result = schoolVideoGalleryService.save(schoolVideoGalleryDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolVideoGalleryDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-video-galleries/:id} : Partial updates given fields of
	 * an existing schoolVideoGallery, field will ignore if it is null
	 *
	 * @param id                    the id of the schoolVideoGalleryDTO to save.
	 * @param schoolVideoGalleryDTO the schoolVideoGalleryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolVideoGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolVideoGalleryDTO is not valid,
	 *         or with status {@code 404 (Not Found)} if the schoolVideoGalleryDTO
	 *         is not found, or with status {@code 500 (Internal Server Error)} if
	 *         the schoolVideoGalleryDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-video-galleries/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolVideoGalleryDTO> partialUpdateSchoolVideoGallery(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolVideoGalleryDTO schoolVideoGalleryDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolVideoGallery partially : {}, {}", id, schoolVideoGalleryDTO);
		if (schoolVideoGalleryDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolVideoGalleryDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolVideoGalleryRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolVideoGalleryDTO.setLastModified(LocalDate.now());
		Optional<SchoolVideoGalleryDTO> result = schoolVideoGalleryService.partialUpdate(schoolVideoGalleryDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolVideoGalleryDTO.getId().toString()));
	}

	/**
	 * {@code DELETE  /school-video-galleries/:id} : delete the "id"
	 * schoolVideoGallery.
	 *
	 * @param id the id of the schoolVideoGalleryDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-video-galleries/{id}")
	public ResponseEntity<Void> deleteSchoolVideoGallery(@PathVariable Long id) {
		log.debug("REST request to delete SchoolVideoGallery : {}", id);
		schoolVideoGalleryService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
