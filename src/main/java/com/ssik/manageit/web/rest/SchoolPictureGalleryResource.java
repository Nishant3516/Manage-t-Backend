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

import com.ssik.manageit.repository.SchoolPictureGalleryRepository;
import com.ssik.manageit.service.SchoolPictureGalleryService;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolPictureGallery}.
 */
@RestController
@RequestMapping("/api")
public class SchoolPictureGalleryResource {

	private final Logger log = LoggerFactory.getLogger(SchoolPictureGalleryResource.class);

	private static final String ENTITY_NAME = "schoolPictureGallery";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolPictureGalleryService schoolPictureGalleryService;

	private final SchoolPictureGalleryRepository schoolPictureGalleryRepository;

	public SchoolPictureGalleryResource(SchoolPictureGalleryService schoolPictureGalleryService,
			SchoolPictureGalleryRepository schoolPictureGalleryRepository) {
		this.schoolPictureGalleryService = schoolPictureGalleryService;
		this.schoolPictureGalleryRepository = schoolPictureGalleryRepository;
	}

	/**
	 * {@code POST  /school-picture-galleries} : Create a new schoolPictureGallery.
	 *
	 * @param schoolPictureGalleryDTO the schoolPictureGalleryDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolPictureGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolPictureGallery has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-picture-galleries")
	public ResponseEntity<SchoolPictureGalleryDTO> createSchoolPictureGallery(
			@Valid @RequestBody SchoolPictureGalleryDTO schoolPictureGalleryDTO) throws URISyntaxException {
		log.debug("REST request to save SchoolPictureGallery : {}", schoolPictureGalleryDTO);
		if (schoolPictureGalleryDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolPictureGallery cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		schoolPictureGalleryDTO.setCreateDate(LocalDate.now());
		SchoolPictureGalleryDTO result = schoolPictureGalleryService.save(schoolPictureGalleryDTO);
		return ResponseEntity
				.created(new URI("/api/school-picture-galleries/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-picture-galleries/:id} : Updates an existing
	 * schoolPictureGallery.
	 *
	 * @param id                      the id of the schoolPictureGalleryDTO to save.
	 * @param schoolPictureGalleryDTO the schoolPictureGalleryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolPictureGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolPictureGalleryDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         schoolPictureGalleryDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-picture-galleries/{id}")
	public ResponseEntity<SchoolPictureGalleryDTO> updateSchoolPictureGallery(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolPictureGalleryDTO schoolPictureGalleryDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolPictureGallery : {}, {}", id, schoolPictureGalleryDTO);
		if (schoolPictureGalleryDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolPictureGalleryDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolPictureGalleryRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolPictureGalleryDTO.setLastModified(LocalDate.now());
		SchoolPictureGalleryDTO result = schoolPictureGalleryService.save(schoolPictureGalleryDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolPictureGalleryDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-picture-galleries/:id} : Partial updates given fields
	 * of an existing schoolPictureGallery, field will ignore if it is null
	 *
	 * @param id                      the id of the schoolPictureGalleryDTO to save.
	 * @param schoolPictureGalleryDTO the schoolPictureGalleryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolPictureGalleryDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolPictureGalleryDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         schoolPictureGalleryDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolPictureGalleryDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-picture-galleries/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolPictureGalleryDTO> partialUpdateSchoolPictureGallery(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolPictureGalleryDTO schoolPictureGalleryDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolPictureGallery partially : {}, {}", id,
				schoolPictureGalleryDTO);
		if (schoolPictureGalleryDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolPictureGalleryDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolPictureGalleryRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolPictureGalleryDTO.setLastModified(LocalDate.now());
		Optional<SchoolPictureGalleryDTO> result = schoolPictureGalleryService.partialUpdate(schoolPictureGalleryDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolPictureGalleryDTO.getId().toString()));
	}

	/**
	 * {@code DELETE  /school-picture-galleries/:id} : delete the "id"
	 * schoolPictureGallery.
	 *
	 * @param id the id of the schoolPictureGalleryDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-picture-galleries/{id}")
	public ResponseEntity<Void> deleteSchoolPictureGallery(@PathVariable Long id) {
		log.debug("REST request to delete SchoolPictureGallery : {}", id);
		schoolPictureGalleryService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
