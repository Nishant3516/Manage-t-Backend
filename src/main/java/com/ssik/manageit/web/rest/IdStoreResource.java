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

import com.ssik.manageit.repository.IdStoreRepository;
import com.ssik.manageit.service.IdStoreQueryService;
import com.ssik.manageit.service.IdStoreService;
import com.ssik.manageit.service.criteria.IdStoreCriteria;
import com.ssik.manageit.service.dto.IdStoreDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.IdStore}.
 */
@RestController
@RequestMapping("/api")
public class IdStoreResource {

	private final Logger log = LoggerFactory.getLogger(IdStoreResource.class);

	private static final String ENTITY_NAME = "idStore";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final IdStoreService idStoreService;

	private final IdStoreRepository idStoreRepository;

	private final IdStoreQueryService idStoreQueryService;

	public IdStoreResource(IdStoreService idStoreService, IdStoreRepository idStoreRepository,
			IdStoreQueryService idStoreQueryService) {
		this.idStoreService = idStoreService;
		this.idStoreRepository = idStoreRepository;
		this.idStoreQueryService = idStoreQueryService;
	}

	/**
	 * {@code POST  /id-stores} : Create a new idStore.
	 *
	 * @param idStoreDTO the idStoreDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new idStoreDTO, or with status {@code 400 (Bad Request)} if
	 *         the idStore has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/id-stores")
	public ResponseEntity<IdStoreDTO> createIdStore(@Valid @RequestBody IdStoreDTO idStoreDTO)
			throws URISyntaxException {
		log.debug("REST request to save IdStore : {}", idStoreDTO);
		if (idStoreDTO.getId() != null) {
			throw new BadRequestAlertException("A new idStore cannot already have an ID", ENTITY_NAME, "idexists");
		}
		IdStoreDTO result = idStoreService.save(idStoreDTO);
		return ResponseEntity
				.created(new URI("/api/id-stores/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /id-stores/:id} : Updates an existing idStore.
	 *
	 * @param id         the id of the idStoreDTO to save.
	 * @param idStoreDTO the idStoreDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated idStoreDTO, or with status {@code 400 (Bad Request)} if
	 *         the idStoreDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the idStoreDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/id-stores/{id}")
	public ResponseEntity<IdStoreDTO> updateIdStore(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody IdStoreDTO idStoreDTO) throws URISyntaxException {
		log.debug("REST request to update IdStore : {}, {}", id, idStoreDTO);
		if (idStoreDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, idStoreDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!idStoreRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		idStoreDTO.setCreateDate(LocalDate.now());

		IdStoreDTO result = idStoreService.save(idStoreDTO);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idStoreDTO.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /id-stores/:id} : Partial updates given fields of an existing
	 * idStore, field will ignore if it is null
	 *
	 * @param id         the id of the idStoreDTO to save.
	 * @param idStoreDTO the idStoreDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated idStoreDTO, or with status {@code 400 (Bad Request)} if
	 *         the idStoreDTO is not valid, or with status {@code 404 (Not Found)}
	 *         if the idStoreDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the idStoreDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/id-stores/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<IdStoreDTO> partialUpdateIdStore(@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody IdStoreDTO idStoreDTO) throws URISyntaxException {
		log.debug("REST request to partial update IdStore partially : {}, {}", id, idStoreDTO);
		if (idStoreDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, idStoreDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!idStoreRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		idStoreDTO.setLastModified(LocalDate.now());

		Optional<IdStoreDTO> result = idStoreService.partialUpdate(idStoreDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idStoreDTO.getId().toString()));
	}

	/**
	 * {@code GET  /id-stores} : get all the idStores.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of idStores in body.
	 */
	@GetMapping("/id-stores")
	public ResponseEntity<List<IdStoreDTO>> getAllIdStores(IdStoreCriteria criteria) {
		log.debug("REST request to get IdStores by criteria: {}", criteria);
		List<IdStoreDTO> entityList = idStoreQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /id-stores/count} : count all the idStores.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/id-stores/count")
	public ResponseEntity<Long> countIdStores(IdStoreCriteria criteria) {
		log.debug("REST request to count IdStores by criteria: {}", criteria);
		return ResponseEntity.ok().body(idStoreQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /id-stores/:id} : get the "id" idStore.
	 *
	 * @param id the id of the idStoreDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the idStoreDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/id-stores/{id}")
	public ResponseEntity<IdStoreDTO> getIdStore(@PathVariable Long id) {
		log.debug("REST request to get IdStore : {}", id);
		Optional<IdStoreDTO> idStoreDTO = idStoreService.findOne(id);
		return ResponseUtil.wrapOrNotFound(idStoreDTO);
	}

	/**
	 * {@code DELETE  /id-stores/:id} : delete the "id" idStore.
	 *
	 * @param id the id of the idStoreDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/id-stores/{id}")
	public ResponseEntity<Void> deleteIdStore(@PathVariable Long id) {
		log.debug("REST request to delete IdStore : {}", id);
		idStoreService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
