package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.StudentAttendence;
import com.ssik.manageit.repository.StudentAttendenceRepository;
import com.ssik.manageit.security.SecurityUtils;
import com.ssik.manageit.service.ClassStudentQueryService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentAttendenceQueryService;
import com.ssik.manageit.service.StudentAttendenceService;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.criteria.StudentAttendenceCriteria;
import com.ssik.manageit.service.dto.StudentAttendenceDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentAttendence}.
 */
@RestController
@RequestMapping("/api")
public class StudentAttendenceResource {

	private final Logger log = LoggerFactory.getLogger(StudentAttendenceResource.class);

	private static final String ENTITY_NAME = "studentAttendence";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentAttendenceService studentAttendenceService;

	private final StudentAttendenceRepository studentAttendenceRepository;

	private final StudentAttendenceQueryService studentAttendenceQueryService;

	private final ClassStudentQueryService classStudentQueryService;

	@Autowired
	private SchoolCommonService schoolCommonService;

	public StudentAttendenceResource(StudentAttendenceService studentAttendenceService,
			StudentAttendenceRepository studentAttendenceRepository,
			StudentAttendenceQueryService studentAttendenceQueryService,
			ClassStudentQueryService classStudentQueryService

	) {
		this.studentAttendenceService = studentAttendenceService;
		this.studentAttendenceRepository = studentAttendenceRepository;
		this.studentAttendenceQueryService = studentAttendenceQueryService;
		this.classStudentQueryService = classStudentQueryService;
	}

	/**
	 * {@code POST  /student-attendences} : Create a new studentAttendence.
	 *
	 * @param studentAttendence the studentAttendenceDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentAttendenceDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAttendence has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-attendences")
	public ResponseEntity<StudentAttendence> createStudentAttendence(
			@Valid @RequestBody StudentAttendence studentAttendence) throws URISyntaxException {
		log.debug("REST request to save StudentAttendence : {}", studentAttendence);
		if (studentAttendence.getId() != null) {
			throw new BadRequestAlertException("A new studentAttendence cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentAttendence.setCreateDate(LocalDate.now());
		School school = schoolCommonService.getUserSchool();
		studentAttendence.setSchool(school);
		studentAttendence.setTenant(school.getTenant());
		StudentAttendence result = studentAttendenceRepository.save(studentAttendence);
		return ResponseEntity
				.created(new URI("/api/student-attendences/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-attendences/:id} : Updates an existing
	 * studentAttendence.
	 *
	 * @param id                   the id of the studentAttendenceDTO to save.
	 * @param studentAttendenceDTO the studentAttendenceDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentAttendenceDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAttendenceDTO is not valid,
	 *         or with status {@code 500 (Internal Server Error)} if the
	 *         studentAttendenceDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/student-attendences/{id}")
	public ResponseEntity<StudentAttendenceDTO> updateStudentAttendence(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody StudentAttendenceDTO studentAttendenceDTO) throws URISyntaxException {
		log.debug("REST request to update StudentAttendence : {}, {}", id, studentAttendenceDTO);
		if (studentAttendenceDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, studentAttendenceDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!studentAttendenceRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		studentAttendenceDTO.setLastModified(LocalDate.now());
		StudentAttendenceDTO result = studentAttendenceService.save(studentAttendenceDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				studentAttendenceDTO.getId().toString())).body(result);
	}

	@PutMapping("/student-attendences")
	public ResponseEntity<List<StudentAttendence>> finalizeStudentAttendence(
			@Valid @RequestBody List<StudentAttendence> studentAttendences) throws URISyntaxException {
		List<StudentAttendence> result = new ArrayList<StudentAttendence>();
		if (studentAttendences != null && studentAttendences.size() > 0) {
			log.debug("REST request to update StudentAttendences : {}, {}", studentAttendences.size());
			for (StudentAttendence studentAttendenceDTO : studentAttendences) {
				if (studentAttendenceDTO.getId() == null) {
					throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
				}

				if (!studentAttendenceRepository.existsById(studentAttendenceDTO.getId())) {
					throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
				}
				// this is to lock the attendance from further being modified
				studentAttendenceDTO.setCancelDate(LocalDate.now());
			}
			result = studentAttendenceRepository.saveAll(studentAttendences);

		}
		return ResponseEntity.ok().body(result);
	}

	/**
	 * {@code PATCH  /student-attendences/:id} : Partial updates given fields of an
	 * existing studentAttendence, field will ignore if it is null
	 *
	 * @param id                   the id of the studentAttendenceDTO to save.
	 * @param studentAttendenceDTO the studentAttendenceDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentAttendenceDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentAttendenceDTO is not valid,
	 *         or with status {@code 404 (Not Found)} if the studentAttendenceDTO is
	 *         not found, or with status {@code 500 (Internal Server Error)} if the
	 *         studentAttendenceDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/student-attendences/{id}", consumes = "application/merge-patch+json")
//	public ResponseEntity<StudentAttendenceDTO> partialUpdateStudentAttendence(
//			@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody StudentAttendenceDTO studentAttendenceDTO) throws URISyntaxException {
//		log.debug("REST request to partial update StudentAttendence partially : {}, {}", id, studentAttendenceDTO);
//		if (studentAttendenceDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentAttendenceDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentAttendenceRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<StudentAttendenceDTO> result = studentAttendenceService.partialUpdate(studentAttendenceDTO);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, studentAttendenceDTO.getId().toString()));
//	}

	/**
	 * {@code GET  /student-attendences} : get all the studentAttendences.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentAttendences in body.
	 */
	@GetMapping("/student-attendences")
	public ResponseEntity<List<StudentAttendence>> getAllStudentAttendences(
			@RequestParam(required = false) Long classId, @RequestParam(required = false) Long studentId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
		log.debug("REST request to get attendance for class  :{}  student Id : {} and startDate :{} and endDate: {} ",
				classId, studentId, startDate, endDate);
		// log.debug("REST request to get StudentAttendences by criteria: {}",
		// criteria);
		if (startDate.isEqual(endDate) && startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new BadRequestAlertException("Invalid Date", " For Attendance", "No Attendance for Sunday");
		}

		ClassStudent loggedInClassStudent = null;
		if ((classId == null || classId == 0) && studentId == null) {
			Optional<String> loggedInUserOpt = SecurityUtils.getCurrentUserLogin();
			if (loggedInUserOpt.isPresent()) {
				loggedInClassStudent = schoolCommonService.getClassStudent(loggedInUserOpt.get());

			} else {
				throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
			}
		}

		if (loggedInClassStudent != null) {
			classId = loggedInClassStudent.getSchoolClass().getId();

		}
		List<Long> studentIds = null;

		ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
		LongFilter classIdFilter = new LongFilter();
		classIdFilter.setEquals(classId);
		classStudentCriteria.setSchoolClassId(classIdFilter);

		List<ClassStudent> classStudents = classStudentQueryService.findEntitiesByCriteria(classStudentCriteria);

		if (studentId != null) {
			studentIds = classStudents.stream().filter(s -> s.getId().equals(studentId)).map(ClassStudent::getId)
					.collect(Collectors.toList());
		} else if (loggedInClassStudent != null && loggedInClassStudent.getId() != null) {
			Long id = loggedInClassStudent.getId();
			studentIds = classStudents.stream().filter(s -> s.getId().equals(id)).map(ClassStudent::getId)
					.collect(Collectors.toList());
		} else {
			studentIds = classStudents.stream().map(ClassStudent::getId).collect(Collectors.toList());
		}

		StudentAttendenceCriteria studentAttendanceCriteria = new StudentAttendenceCriteria();

		LocalDateFilter attendenceDateFilter = new LocalDateFilter();
		attendenceDateFilter.setGreaterThanOrEqual(startDate);
		attendenceDateFilter.setLessThanOrEqual(endDate);
		studentAttendanceCriteria.setSchoolDate(attendenceDateFilter);

		LongFilter studentIdFilter = new LongFilter();
		studentIdFilter.setIn(studentIds);
		studentAttendanceCriteria.setClassStudentId(studentIdFilter);

		// if for single student, i.e. when a student Id has been specified , no need to
		// create the attendence , just return the result
		List<StudentAttendence> entityList = studentAttendenceQueryService
				.findEntitiesByCriteria(studentAttendanceCriteria);

		// if(entityList.isEmpty()&&startDate.isEqual(endDate)&&studentId==null) {
		if (entityList.isEmpty() && startDate.isEqual(endDate) && studentId == null) {
			// create an entry for each student with default attendance status as false
			// List<StudentAttendence>
			// studentAttendences=classStudents.stream().forEach(this::createAttendenceForAStudent(null,
			// adate));
			List<StudentAttendence> studentAttendences = classStudents.stream()
					.map(classStudent -> createAttendenceForAStudent(classStudent, startDate))
					.collect(Collectors.toList());

			// studentAttendences.stream().(s->))
			studentAttendenceRepository.saveAll(studentAttendences);

//        	List<StudentAttendenceDTO> studentAttendencesDtos =studentAttendenceMapper.toDto(studentAttendenceRepository.saveAll(studentAttendences));
			return ResponseEntity.ok().body(studentAttendences);
		}
		return ResponseEntity.ok().body(entityList);
		// return ResponseEntity.ok().body(new ArrayList<StudentAttendence>());
	}

	private StudentAttendence createAttendenceForAStudent(ClassStudent classStudent, LocalDate attendanceDate) {

		StudentAttendence studentAttendence = new StudentAttendence();
		studentAttendence.setAttendence(false);
		studentAttendence.setClassStudent(classStudent);
		studentAttendence.setSchoolDate(attendanceDate);
		studentAttendence.setCreateDate(LocalDate.now());
		return studentAttendence;
	}

	@GetMapping("/student-attendences-class")
	public ResponseEntity<List<StudentAttendenceDTO>> getAllStudentAttendencesForADate(@RequestParam Long adate,
			@RequestParam String classId) {
		log.debug("REST request to get attendance for class  :{} and date :{} ", classId, adate);

		// List<StudentAttendenceDTO> entityList =
		// studentAttendenceQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(new ArrayList<StudentAttendenceDTO>());
	}

	@GetMapping("/school-attendences/{adate}/{classId}")
	public ResponseEntity<List<StudentAttendenceDTO>> classAttendanceForADate(@PathVariable Long adate,
			@PathVariable String classId) {
		log.debug("REST request to get attendance for class  :{} and date :{} ", classId, adate);
		return ResponseEntity.ok().body(new ArrayList<StudentAttendenceDTO>());
	}

	/**
	 * {@code GET  /student-attendences/count} : count all the studentAttendences.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-attendences/count")
	public ResponseEntity<Long> countStudentAttendences(StudentAttendenceCriteria criteria) {
		log.debug("REST request to count StudentAttendences by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentAttendenceQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-attendences/:id} : get the "id" studentAttendence.
	 *
	 * @param id the id of the studentAttendenceDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentAttendenceDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-attendences/{id}")
	public ResponseEntity<StudentAttendenceDTO> getStudentAttendence(@PathVariable Long id) {
		log.debug("REST request to get StudentAttendence : {}", id);
		Optional<StudentAttendenceDTO> studentAttendenceDTO = studentAttendenceService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentAttendenceDTO);
	}

	/**
	 * {@code DELETE  /student-attendences/:id} : delete the "id" studentAttendence.
	 *
	 * @param id the id of the studentAttendenceDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/student-attendences/{id}")
	public ResponseEntity<Void> deleteStudentAttendence(@PathVariable Long id) {
		log.debug("REST request to delete StudentAttendence : {}", id);
		studentAttendenceService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
