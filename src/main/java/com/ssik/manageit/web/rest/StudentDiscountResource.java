package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.repository.StudentDiscountRepository;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentDiscountQueryService;
import com.ssik.manageit.service.StudentDiscountService;
import com.ssik.manageit.service.criteria.StudentDiscountCriteria;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentDiscount}.
 */
@RestController
@RequestMapping("/api")
public class StudentDiscountResource {

	private final Logger log = LoggerFactory.getLogger(StudentDiscountResource.class);

	private static final String ENTITY_NAME = "studentDiscount";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentDiscountService studentDiscountService;

	private final StudentDiscountQueryService studentDiscountQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;

	@Autowired
	private StudentDiscountRepository studentDiscountRepository;

	public StudentDiscountResource(StudentDiscountService studentDiscountService,
			StudentDiscountQueryService studentDiscountQueryService) {
		this.studentDiscountService = studentDiscountService;
		this.studentDiscountQueryService = studentDiscountQueryService;
	}

	/**
	 * {@code POST  /student-discounts} : Create a new studentDiscount.
	 *
	 * @param studentDiscount the studentDiscountDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentDiscountDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentDiscount has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-discounts")
	public ResponseEntity<StudentDiscount> createStudentDiscount(@Valid @RequestBody StudentDiscount studentDiscount)
			throws URISyntaxException {
		log.debug("REST request to save StudentDiscount : {}", studentDiscount);
		if (studentDiscount.getId() != null) {
			throw new BadRequestAlertException("A new studentDiscount cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentDiscount.setCreateDate(LocalDate.now());
		School school = schoolCommonService.getUserSchool();
		studentDiscount.setSchool(school);
		studentDiscount.setTenant(school.getTenant());
		StudentDiscount result = studentDiscountRepository.save(studentDiscount);
		return ResponseEntity
				.created(new URI("/api/student-discounts/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-discounts/:id} : Updates an existing studentDiscount.
	 *
	 * @param id                 the id of the studentDiscountDTO to save.
	 * @param studentDiscountDTO the studentDiscountDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentDiscountDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentDiscountDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         studentDiscountDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PutMapping("/student-discounts/{id}")
//	public ResponseEntity<StudentDiscountDTO> updateStudentDiscount(
//			@PathVariable(value = "id", required = false) final Long id,
//			@Valid @RequestBody StudentDiscountDTO studentDiscountDTO) throws URISyntaxException {
//		log.debug("REST request to update StudentDiscount : {}, {}", id, studentDiscountDTO);
//		if (studentDiscountDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentDiscountDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentDiscountRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		StudentDiscountDTO result = studentDiscountService.save(studentDiscountDTO);
//		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
//				studentDiscountDTO.getId().toString())).body(result);
//	}

	/**
	 * {@code PATCH  /student-discounts/:id} : Partial updates given fields of an
	 * existing studentDiscount, field will ignore if it is null
	 *
	 * @param id                 the id of the studentDiscountDTO to save.
	 * @param studentDiscountDTO the studentDiscountDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentDiscountDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentDiscountDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the studentDiscountDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         studentDiscountDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/student-discounts/{id}", consumes = "application/merge-patch+json")
//	public ResponseEntity<StudentDiscountDTO> partialUpdateStudentDiscount(
//			@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody StudentDiscountDTO studentDiscountDTO) throws URISyntaxException {
//		log.debug("REST request to partial update StudentDiscount partially : {}, {}", id, studentDiscountDTO);
//		if (studentDiscountDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentDiscountDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentDiscountRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<StudentDiscountDTO> result = studentDiscountService.partialUpdate(studentDiscountDTO);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, studentDiscountDTO.getId().toString()));
//	}

	/**
	 * {@code GET  /student-discounts} : get all the studentDiscounts.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentDiscounts in body.
	 */
	@GetMapping("/student-discounts")
	public ResponseEntity<List<StudentDiscountDTO>> getAllStudentDiscounts(StudentDiscountCriteria criteria) {
		log.debug("REST request to get StudentDiscounts by criteria: {}", criteria);
		List<StudentDiscountDTO> entityList = studentDiscountQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /student-discounts/count} : count all the studentDiscounts.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-discounts/count")
	public ResponseEntity<Long> countStudentDiscounts(StudentDiscountCriteria criteria) {
		log.debug("REST request to count StudentDiscounts by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentDiscountQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-discounts/:id} : get the "id" studentDiscount.
	 *
	 * @param id the id of the studentDiscountDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentDiscountDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-discounts/{id}")
	public ResponseEntity<StudentDiscountDTO> getStudentDiscount(@PathVariable Long id) {
		log.debug("REST request to get StudentDiscount : {}", id);
		Optional<StudentDiscountDTO> studentDiscountDTO = studentDiscountService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentDiscountDTO);
	}

	/**
	 * {@code DELETE  /student-discounts/:id} : delete the "id" studentDiscount.
	 *
	 * @param id the id of the studentDiscountDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/student-discounts/{id}")
	public ResponseEntity<Void> deleteStudentDiscount(@PathVariable Long id) {
		log.debug("REST request to delete StudentDiscount : {}", id);
		// studentDiscountService.delete(id);
		Optional<StudentDiscountDTO> schoolStudentOpt = studentDiscountService.findOne(id);
		if (schoolStudentOpt.isPresent()) {
			StudentDiscountDTO schoolDiscount = schoolStudentOpt.get();
			schoolDiscount.setCancelDate(LocalDate.now());
			studentDiscountService.save(schoolDiscount);
		}

		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
