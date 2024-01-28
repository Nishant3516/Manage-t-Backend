package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.ClassStudentQueryService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentAdditionalChargesQueryService;
import com.ssik.manageit.service.StudentAdditionalChargesService;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.criteria.StudentAdditionalChargesCriteria;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import liquibase.pro.packaged.s;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentAdditionalCharges}.
 */
@RestController
@RequestMapping("/api")
public class StudentAdditionalChargesResource {

	private final Logger log = LoggerFactory.getLogger(StudentAdditionalChargesResource.class);

	private static final String ENTITY_NAME = "studentAdditionalCharges";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentAdditionalChargesService studentAdditionalChargesService;

	private final StudentAdditionalChargesRepository studentAdditionalChargesRepository;

	private final StudentAdditionalChargesQueryService studentAdditionalChargesQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;
	@Autowired
	private ClassStudentQueryService classStudentQueryService;

	public StudentAdditionalChargesResource(StudentAdditionalChargesService studentAdditionalChargesService,
			StudentAdditionalChargesRepository studentAdditionalChargesRepository,
			StudentAdditionalChargesQueryService studentAdditionalChargesQueryService) {
		this.studentAdditionalChargesService = studentAdditionalChargesService;
		this.studentAdditionalChargesRepository = studentAdditionalChargesRepository;
		this.studentAdditionalChargesQueryService = studentAdditionalChargesQueryService;
	}

	/**
	 * {@code POST  /student-additional-charges} : Create a new
	 * studentAdditionalCharges.
	 *
	 * @param studentAdditionalChargesDTO the studentAdditionalChargesDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentAdditionalChargesDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAdditionalCharges has already
	 *         an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-additional-charges")
	public ResponseEntity<StudentAdditionalCharges> createStudentAdditionalCharges(
			@Valid @RequestBody StudentAdditionalCharges studentAdditionalCharges) throws URISyntaxException {
		log.debug("REST request to save StudentAdditionalCharges : {}", studentAdditionalCharges);
		if (studentAdditionalCharges.getId() != null) {
			throw new BadRequestAlertException("A new studentAdditionalCharges cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentAdditionalCharges.setCreateDate(LocalDate.now());
		School school = schoolCommonService.getUserSchool();
		studentAdditionalCharges.setSchool(school);
		studentAdditionalCharges.setTenant(school.getTenant());
		StudentAdditionalCharges result = studentAdditionalChargesRepository.save(studentAdditionalCharges);
		return ResponseEntity
				.created(new URI("/api/student-additional-charges/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-additional-charges/:id} : Updates an existing
	 * studentAdditionalCharges.
	 *
	 * @param id                          the id of the studentAdditionalChargesDTO
	 *                                    to save.
	 * @param studentAdditionalChargesDTO the studentAdditionalChargesDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentAdditionalChargesDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAdditionalChargesDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         studentAdditionalChargesDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//    @PutMapping("/student-additional-charges/{id}")
//    public ResponseEntity<StudentAdditionalChargesDTO> updateStudentAdditionalCharges(
//        @PathVariable(value = "id", required = false) final Long id,
//        @Valid @RequestBody StudentAdditionalChargesDTO studentAdditionalChargesDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to update StudentAdditionalCharges : {}, {}", id, studentAdditionalChargesDTO);
//        if (studentAdditionalChargesDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, studentAdditionalChargesDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!studentAdditionalChargesRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        StudentAdditionalChargesDTO result = studentAdditionalChargesService.save(studentAdditionalChargesDTO);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentAdditionalChargesDTO.getId().toString()))
//            .body(result);
//    }

	/**
	 * {@code PATCH  /student-additional-charges/:id} : Partial updates given fields
	 * of an existing studentAdditionalCharges, field will ignore if it is null
	 *
	 * @param id                          the id of the studentAdditionalChargesDTO
	 *                                    to save.
	 * @param studentAdditionalChargesDTO the studentAdditionalChargesDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentAdditionalChargesDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAdditionalChargesDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         studentAdditionalChargesDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the
	 *         studentAdditionalChargesDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//    @PatchMapping(value = "/student-additional-charges/{id}", consumes = "application/merge-patch+json")
//    public ResponseEntity<StudentAdditionalChargesDTO> partialUpdateStudentAdditionalCharges(
//        @PathVariable(value = "id", required = false) final Long id,
//        @NotNull @RequestBody StudentAdditionalChargesDTO studentAdditionalChargesDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update StudentAdditionalCharges partially : {}, {}", id, studentAdditionalChargesDTO);
//        if (studentAdditionalChargesDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, studentAdditionalChargesDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!studentAdditionalChargesRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<StudentAdditionalChargesDTO> result = studentAdditionalChargesService.partialUpdate(studentAdditionalChargesDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentAdditionalChargesDTO.getId().toString())
//        );
//    }

	/**
	 * {@code GET  /student-additional-charges} : get all the
	 * studentAdditionalCharges.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentAdditionalCharges in body.
	 */
	@GetMapping("/student-additional-charges")
	public ResponseEntity<List<StudentAdditionalChargesDTO>> getAllStudentAdditionalCharges(
			StudentAdditionalChargesCriteria criteria, @RequestParam Long studentClassId) {
		log.debug("REST request to get StudentAdditionalCharges by criteria: {}", criteria);
		// if classStudentId has not been provided, it means details of all the class
		// student is needed ## this check is not working, LongFilter is initializes as
		// empty
		// if there is no value supplied for the student Id filter!! so class Id will be
		// sent as -ve
		// where it is intended to have only a student details
		// and hence get the class Id and get all the students for it
		if (studentClassId > 0) {
			ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
			LongFilter classIdFilter = new LongFilter();
			classIdFilter.setEquals(studentClassId);
			classStudentCriteria.setSchoolClassId(classIdFilter);
			List<ClassStudent> classStudents = classStudentQueryService.findByCriteria(classStudentCriteria);

			List<Long> studentIds = classStudents.stream().map(s -> s.getId()).collect(Collectors.toList());
			// reset the class student id filter sent from the UI via a hack
			criteria.setClassStudentId(null);

			LongFilter studentIdsFilter = new LongFilter();
			studentIdsFilter.setIn(studentIds);
			criteria.setClassStudentId(studentIdsFilter);
		}
		// but if class Id is -ve , it means only student details is expected

		List<StudentAdditionalChargesDTO> entityList = studentAdditionalChargesQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /student-additional-charges/count} : count all the
	 * studentAdditionalCharges.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-additional-charges/count")
	public ResponseEntity<Long> countStudentAdditionalCharges(StudentAdditionalChargesCriteria criteria) {
		log.debug("REST request to count StudentAdditionalCharges by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentAdditionalChargesQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-additional-charges/:id} : get the "id"
	 * studentAdditionalCharges.
	 *
	 * @param id the id of the studentAdditionalChargesDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentAdditionalChargesDTO, or with status
	 *         {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-additional-charges/{id}")
	public ResponseEntity<StudentAdditionalChargesDTO> getStudentAdditionalCharges(@PathVariable Long id) {
		log.debug("REST request to get StudentAdditionalCharges : {}", id);
		Optional<StudentAdditionalChargesDTO> studentAdditionalChargesDTO = studentAdditionalChargesService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentAdditionalChargesDTO);
	}

	/**
	 * {@code DELETE  /student-additional-charges/:id} : delete the "id"
	 * studentAdditionalCharges.
	 *
	 * @param id the id of the studentAdditionalChargesDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/student-additional-charges/{id}")
	public ResponseEntity<Void> deleteStudentAdditionalCharges(@PathVariable Long id) {
		log.debug("REST request to delete StudentAdditionalCharges : {}", id);
		// studentAdditionalChargesService.delete(id);
		Optional<StudentAdditionalChargesDTO> studentAdditionalChargesOpt = studentAdditionalChargesService.findOne(id);
		if (studentAdditionalChargesOpt.isPresent()) {
			StudentAdditionalChargesDTO studentAdditionalCharges = studentAdditionalChargesOpt.get();
			studentAdditionalCharges.setCancelDate(LocalDate.now());
			studentAdditionalChargesService.save(studentAdditionalCharges);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
