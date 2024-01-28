package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.SchoolLedgerHeadQueryService;
import com.ssik.manageit.service.SchoolLedgerHeadService;
import com.ssik.manageit.service.criteria.SchoolLedgerHeadCriteria;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.mapper.SchoolMapper;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolLedgerHead}.
 */
@RestController
@RequestMapping("/api")
public class SchoolLedgerHeadResource {

	private final Logger log = LoggerFactory.getLogger(SchoolLedgerHeadResource.class);

	private static final String ENTITY_NAME = "schoolLedgerHead";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolLedgerHeadService schoolLedgerHeadService;

	private final SchoolLedgerHeadRepository schoolLedgerHeadRepository;

	private final SchoolLedgerHeadQueryService schoolLedgerHeadQueryService;

	@Autowired
	private SchoolCommonService schoolCommonService;
	@Autowired
	private SchoolMapper schoolMapper;

	public SchoolLedgerHeadResource(SchoolLedgerHeadService schoolLedgerHeadService,
			SchoolLedgerHeadRepository schoolLedgerHeadRepository,
			SchoolLedgerHeadQueryService schoolLedgerHeadQueryService) {
		this.schoolLedgerHeadService = schoolLedgerHeadService;
		this.schoolLedgerHeadRepository = schoolLedgerHeadRepository;
		this.schoolLedgerHeadQueryService = schoolLedgerHeadQueryService;
	}

	/**
	 * {@code POST  /school-ledger-heads} : Create a new schoolLedgerHead.
	 *
	 * @param schoolLedgerHeadDTO the schoolLedgerHeadDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolLedgerHeadDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolLedgerHead has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/school-ledger-heads")
	public ResponseEntity<SchoolLedgerHeadDTO> createSchoolLedgerHead(
			@Valid @RequestBody SchoolLedgerHeadDTO schoolLedgerHeadDTO) throws URISyntaxException {
		log.debug("REST request to save SchoolLedgerHead : {}", schoolLedgerHeadDTO);
		if (schoolLedgerHeadDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolLedgerHead cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		schoolLedgerHeadDTO.setCreateDate(LocalDate.now());
		School school = schoolCommonService.getUserSchool();
		schoolLedgerHeadDTO.setSchool(schoolMapper.toDto(school));
		schoolLedgerHeadDTO.setTenant(school.getTenant());
		SchoolLedgerHeadDTO result = schoolLedgerHeadService.save(schoolLedgerHeadDTO);
		return ResponseEntity
				.created(new URI("/api/school-ledger-heads/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /school-ledger-heads/:id} : Updates an existing schoolLedgerHead.
	 *
	 * @param id                  the id of the schoolLedgerHeadDTO to save.
	 * @param schoolLedgerHeadDTO the schoolLedgerHeadDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolLedgerHeadDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolLedgerHeadDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         schoolLedgerHeadDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-ledger-heads/{id}")
	public ResponseEntity<SchoolLedgerHeadDTO> updateSchoolLedgerHead(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolLedgerHeadDTO schoolLedgerHeadDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolLedgerHead : {}, {}", id, schoolLedgerHeadDTO);
		if (schoolLedgerHeadDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolLedgerHeadDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolLedgerHeadRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolLedgerHeadDTO.setLastModified(LocalDate.now());
		SchoolLedgerHeadDTO result = schoolLedgerHeadService.save(schoolLedgerHeadDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolLedgerHeadDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-ledger-heads/:id} : Partial updates given fields of an
	 * existing schoolLedgerHead, field will ignore if it is null
	 *
	 * @param id                  the id of the schoolLedgerHeadDTO to save.
	 * @param schoolLedgerHeadDTO the schoolLedgerHeadDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolLedgerHeadDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolLedgerHeadDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the schoolLedgerHeadDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         schoolLedgerHeadDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-ledger-heads/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolLedgerHeadDTO> partialUpdateSchoolLedgerHead(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolLedgerHeadDTO schoolLedgerHeadDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolLedgerHead partially : {}, {}", id, schoolLedgerHeadDTO);
		if (schoolLedgerHeadDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolLedgerHeadDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolLedgerHeadRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		schoolLedgerHeadDTO.setLastModified(LocalDate.now());
		Optional<SchoolLedgerHeadDTO> result = schoolLedgerHeadService.partialUpdate(schoolLedgerHeadDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolLedgerHeadDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-ledger-heads} : get all the schoolLedgerHeads.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolLedgerHeads in body.
	 */
	@GetMapping("/school-ledger-heads")
	public ResponseEntity<List<SchoolLedgerHead>> getAllSchoolLedgerHeads(SchoolLedgerHeadCriteria criteria) {
		log.debug("REST request to get SchoolLedgerHeads by criteria: {}", criteria);
		School school = schoolCommonService.getUserSchool();
		List<SchoolLedgerHead> entityList = schoolLedgerHeadQueryService.findByCriteria(criteria);
		entityList = entityList.stream().filter(lh -> lh.getTenant().equals(school.getTenant()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /school-ledger-heads/count} : count all the schoolLedgerHeads.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-ledger-heads/count")
	public ResponseEntity<Long> countSchoolLedgerHeads(SchoolLedgerHeadCriteria criteria) {
		log.debug("REST request to count SchoolLedgerHeads by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolLedgerHeadQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-ledger-heads/:id} : get the "id" schoolLedgerHead.
	 *
	 * @param id the id of the schoolLedgerHeadDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolLedgerHeadDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-ledger-heads/{id}")
	public ResponseEntity<SchoolLedgerHeadDTO> getSchoolLedgerHead(@PathVariable Long id) {
		log.debug("REST request to get SchoolLedgerHead : {}", id);
		Optional<SchoolLedgerHeadDTO> schoolLedgerHeadDTO = schoolLedgerHeadService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolLedgerHeadDTO);
	}

	/**
	 * {@code DELETE  /school-ledger-heads/:id} : delete the "id" schoolLedgerHead.
	 *
	 * @param id the id of the schoolLedgerHeadDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-ledger-heads/{id}")
	public ResponseEntity<Void> deleteSchoolLedgerHead(@PathVariable Long id) {
		log.debug("REST request to delete SchoolLedgerHead : {}", id);
		// schoolLedgerHeadService.delete(id);
		Optional<SchoolLedgerHeadDTO> schoolLedgerHEadOpt = schoolLedgerHeadService.findOne(id);
		if (schoolLedgerHEadOpt.isPresent()) {
			SchoolLedgerHeadDTO schoolLedgerHead = schoolLedgerHEadOpt.get();
			schoolLedgerHead.setCancelDate(LocalDate.now());
			schoolLedgerHeadService.save(schoolLedgerHead);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
