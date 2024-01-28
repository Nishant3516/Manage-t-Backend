package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.repository.StudentChargesSummaryRepository;
import com.ssik.manageit.service.StudentChargesSummaryQueryService;
import com.ssik.manageit.service.StudentChargesSummaryQueryServiceForDues;
import com.ssik.manageit.service.StudentChargesSummaryService;
import com.ssik.manageit.service.criteria.StudentChargesSummaryCriteria;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentChargesSummary}.
 */
@RestController
@RequestMapping("/api")
public class StudentChargesSummaryResource {

	private final Logger log = LoggerFactory.getLogger(StudentChargesSummaryResource.class);

	private static final String ENTITY_NAME = "studentChargesSummary";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentChargesSummaryService studentChargesSummaryService;

	private final StudentChargesSummaryRepository studentChargesSummaryRepository;

	private final StudentChargesSummaryQueryService studentChargesSummaryQueryService;

	@Autowired
	private StudentChargesSummaryQueryServiceForDues studentChargesSummaryQueryServiceForDues;

	public StudentChargesSummaryResource(StudentChargesSummaryService studentChargesSummaryService,
			StudentChargesSummaryRepository studentChargesSummaryRepository,
			StudentChargesSummaryQueryService studentChargesSummaryQueryService) {
		this.studentChargesSummaryService = studentChargesSummaryService;
		this.studentChargesSummaryRepository = studentChargesSummaryRepository;
		this.studentChargesSummaryQueryService = studentChargesSummaryQueryService;
	}

	/**
	 * {@code POST  /student-charges-summaries} : Create a new
	 * studentChargesSummary.
	 *
	 * @param studentChargesSummaryDTO the studentChargesSummaryDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentChargesSummaryDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentChargesSummary has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-charges-summaries")
	public ResponseEntity<StudentChargesSummaryDTO> createStudentChargesSummary(
			@RequestBody StudentChargesSummaryDTO studentChargesSummaryDTO) throws URISyntaxException {
		log.debug("REST request to save StudentChargesSummary : {}", studentChargesSummaryDTO);
		if (studentChargesSummaryDTO.getId() != null) {
			throw new BadRequestAlertException("A new studentChargesSummary cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentChargesSummaryDTO.setCreateDate(LocalDate.now());
		StudentChargesSummaryDTO result = studentChargesSummaryService.save(studentChargesSummaryDTO);
		return ResponseEntity
				.created(new URI("/api/student-charges-summaries/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-charges-summaries/:id} : Updates an existing
	 * studentChargesSummary.
	 *
	 * @param id                       the id of the studentChargesSummaryDTO to
	 *                                 save.
	 * @param studentChargesSummaryDTO the studentChargesSummaryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentChargesSummaryDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentChargesSummaryDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         studentChargesSummaryDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PutMapping("/student-charges-summaries/{id}")
//	public ResponseEntity<StudentChargesSummaryDTO> updateStudentChargesSummary(
//			@PathVariable(value = "id", required = false) final Long id,
//			@RequestBody StudentChargesSummaryDTO studentChargesSummaryDTO) throws URISyntaxException {
//		log.debug("REST request to update StudentChargesSummary : {}, {}", id, studentChargesSummaryDTO);
//		if (studentChargesSummaryDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentChargesSummaryDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentChargesSummaryRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		StudentChargesSummaryDTO result = studentChargesSummaryService.save(studentChargesSummaryDTO);
//		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
//				studentChargesSummaryDTO.getId().toString())).body(result);
//	}

	/**
	 * {@code PATCH  /student-charges-summaries/:id} : Partial updates given fields
	 * of an existing studentChargesSummary, field will ignore if it is null
	 *
	 * @param id                       the id of the studentChargesSummaryDTO to
	 *                                 save.
	 * @param studentChargesSummaryDTO the studentChargesSummaryDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentChargesSummaryDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentChargesSummaryDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         studentChargesSummaryDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the studentChargesSummaryDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/student-charges-summaries/{id}", consumes = "application/merge-patch+json")
//	public ResponseEntity<StudentChargesSummaryDTO> partialUpdateStudentChargesSummary(
//			@PathVariable(value = "id", required = false) final Long id,
//			@RequestBody StudentChargesSummaryDTO studentChargesSummaryDTO) throws URISyntaxException {
//		log.debug("REST request to partial update StudentChargesSummary partially : {}, {}", id,
//				studentChargesSummaryDTO);
//		if (studentChargesSummaryDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentChargesSummaryDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentChargesSummaryRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<StudentChargesSummaryDTO> result = studentChargesSummaryService
//				.partialUpdate(studentChargesSummaryDTO);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, studentChargesSummaryDTO.getId().toString()));
//	}

	/**
	 * {@code GET  /student-charges-summaries} : get all the
	 * studentChargesSummaries.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentChargesSummaries in body.
	 */
	@GetMapping("/student-charges-summaries")
	public ResponseEntity<List<StudentChargesSummaryDTO>> getAllStudentChargesSummaries(
			@RequestParam List<String> studentIds, Boolean onlyDues) {
		log.debug("REST request to get getAllStudentChargesSummaries by studentId: {}", studentIds.size());
		// List<String> studentIds=new ArrayList<String>();
		// studentIds.add(studentId);
		List<StudentChargesSummaryDTO> summaries = new ArrayList<>();
		if (onlyDues) {
			summaries = studentChargesSummaryQueryServiceForDues.findByStudentIds(studentIds, onlyDues);
		} else {
			summaries = studentChargesSummaryQueryService.findByStudentIds(studentIds, onlyDues);
		}
		return ResponseEntity.ok().body(summaries);
	}

	// public ResponseEntity<List<StudentChargesSummaryDTO>>
	// getAllStudentChargesSummaries(
	// StudentChargesSummaryCriteria criteria,
	// Pageable pageable
	// ) {
	// log.debug("REST request to get StudentChargesSummaries by criteria: {}",
	// criteria);
	// Page<StudentChargesSummaryDTO> page =
	// studentChargesSummaryQueryService.findByCriteria(criteria, pageable);
	// HttpHeaders headers =
	// PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
	// page);
	// return ResponseEntity.ok().headers(headers).body(page.getContent());
	// }

	/**
	 * {@code GET  /student-charges-summaries/count} : count all the
	 * studentChargesSummaries.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-charges-summaries/count")
	public ResponseEntity<Long> countStudentChargesSummaries(StudentChargesSummaryCriteria criteria) {
		log.debug("REST request to count StudentChargesSummaries by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentChargesSummaryQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-charges-summaries/:id} : get the "id"
	 * studentChargesSummary.
	 *
	 * @param id the id of the studentChargesSummaryDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentChargesSummaryDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-charges-summaries/{id}")
	public ResponseEntity<StudentChargesSummaryDTO> getStudentChargesSummary(@PathVariable Long id) {
		log.debug("REST request to get StudentChargesSummary : {}", id);
		Optional<StudentChargesSummaryDTO> studentChargesSummaryDTO = studentChargesSummaryService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentChargesSummaryDTO);
	}

	/**
	 * {@code DELETE  /student-charges-summaries/:id} : delete the "id"
	 * studentChargesSummary.
	 *
	 * @param id the id of the studentChargesSummaryDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
//	@DeleteMapping("/student-charges-summaries/{id}")
//	public ResponseEntity<Void> deleteStudentChargesSummary(@PathVariable Long id) {
//		log.debug("REST request to delete StudentChargesSummary : {}", id);
//		studentChargesSummaryService.delete(id);
//		return ResponseEntity.noContent()
//				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//				.build();
//	}
}
