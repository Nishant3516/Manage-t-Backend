package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.repository.StudentClassWorkTrackRepository;
import com.ssik.manageit.security.SecurityUtils;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentClassWorkTrackQueryService;
import com.ssik.manageit.service.StudentClassWorkTrackService;
import com.ssik.manageit.service.criteria.StudentClassWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentClassWorkTrack}.
 */
@RestController
@RequestMapping("/api")
public class StudentClassWorkTrackResource {

	private final Logger log = LoggerFactory.getLogger(StudentClassWorkTrackResource.class);

	private static final String ENTITY_NAME = "studentClassWorkTrack";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentClassWorkTrackService studentClassWorkTrackService;

	private final StudentClassWorkTrackRepository studentClassWorkTrackRepository;

	private final StudentClassWorkTrackQueryService studentClassWorkTrackQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;

	public StudentClassWorkTrackResource(StudentClassWorkTrackService studentClassWorkTrackService,
			StudentClassWorkTrackRepository studentClassWorkTrackRepository,
			StudentClassWorkTrackQueryService studentClassWorkTrackQueryService) {
		this.studentClassWorkTrackService = studentClassWorkTrackService;
		this.studentClassWorkTrackRepository = studentClassWorkTrackRepository;
		this.studentClassWorkTrackQueryService = studentClassWorkTrackQueryService;
	}

	/**
	 * {@code POST  /student-class-work-tracks} : Create a new
	 * studentClassWorkTrack.
	 *
	 * @param studentClassWorkTrackDTO the studentClassWorkTrackDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentClassWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentClassWorkTrack has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-class-work-tracks")
	public ResponseEntity<StudentClassWorkTrackDTO> createStudentClassWorkTrack(
			@Valid @RequestBody StudentClassWorkTrackDTO studentClassWorkTrackDTO) throws URISyntaxException {
		log.debug("REST request to save StudentClassWorkTrack : {}", studentClassWorkTrackDTO);
		if (studentClassWorkTrackDTO.getId() != null) {
			throw new BadRequestAlertException("A new studentClassWorkTrack cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentClassWorkTrackDTO.setCreateDate(LocalDate.now());
		StudentClassWorkTrackDTO result = studentClassWorkTrackService.save(studentClassWorkTrackDTO);
		return ResponseEntity
				.created(new URI("/api/student-class-work-tracks/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-class-work-tracks/:id} : Updates an existing
	 * studentClassWorkTrack.
	 *
	 * @param id                       the id of the studentClassWorkTrackDTO to
	 *                                 save.
	 * @param studentClassWorkTrackDTO the studentClassWorkTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentClassWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentClassWorkTrackDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         studentClassWorkTrackDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/student-class-work-tracks/{id}")
	public ResponseEntity<StudentClassWorkTrackDTO> updateStudentClassWorkTrack(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody StudentClassWorkTrackDTO studentClassWorkTrackDTO) throws URISyntaxException {
		log.debug("REST request to update StudentClassWorkTrack : {}, {}", id, studentClassWorkTrackDTO);
		if (studentClassWorkTrackDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, studentClassWorkTrackDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!studentClassWorkTrackRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		studentClassWorkTrackDTO.setLastModified(LocalDate.now());
		StudentClassWorkTrackDTO result = studentClassWorkTrackService.save(studentClassWorkTrackDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				studentClassWorkTrackDTO.getId().toString())).body(result);
	}

	@PutMapping("/student-class-work-tracks")
	public ResponseEntity<List<StudentClassWorkTrack>> finalizeStudentClassWorkTrack(
			@Valid @RequestBody List<StudentClassWorkTrack> studentClassWorkTracks) throws URISyntaxException {
		List<StudentClassWorkTrack> updatedStudentClassWorks = new ArrayList<StudentClassWorkTrack>();
		if (studentClassWorkTracks != null && studentClassWorkTracks.size() > 0) {
			log.debug("REST request to update StudentClassWorkTrack :  {}", studentClassWorkTracks.size());
			for (StudentClassWorkTrack studentClassWorkTrack : studentClassWorkTracks) {
				if (studentClassWorkTrack.getId() == null) {
					throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
				}

				if (!studentClassWorkTrackRepository.existsById(studentClassWorkTrack.getId())) {
					throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
				}
				// cancel date is set to indicate that these records cannot be modified further
				studentClassWorkTrack.setCancelDate(LocalDate.now());
			}
			updatedStudentClassWorks = studentClassWorkTrackRepository.saveAll(studentClassWorkTracks);

		}
		return ResponseEntity.ok().body(updatedStudentClassWorks);
	}

	/**
	 * {@code PATCH  /student-class-work-tracks/:id} : Partial updates given fields
	 * of an existing studentClassWorkTrack, field will ignore if it is null
	 *
	 * @param id                       the id of the studentClassWorkTrackDTO to
	 *                                 save.
	 * @param studentClassWorkTrackDTO the studentClassWorkTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentClassWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentClassWorkTrackDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         studentClassWorkTrackDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the studentClassWorkTrackDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/student-class-work-tracks/{id}", consumes = "application/merge-patch+json")
//	public ResponseEntity<StudentClassWorkTrackDTO> partialUpdateStudentClassWorkTrack(
//			@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody StudentClassWorkTrackDTO studentClassWorkTrackDTO) throws URISyntaxException {
//		log.debug("REST request to partial update StudentClassWorkTrack partially : {}, {}", id,
//				studentClassWorkTrackDTO);
//		if (studentClassWorkTrackDTO.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, studentClassWorkTrackDTO.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!studentClassWorkTrackRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<StudentClassWorkTrackDTO> result = studentClassWorkTrackService
//				.partialUpdate(studentClassWorkTrackDTO);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, studentClassWorkTrackDTO.getId().toString()));
//	}

	/**
	 * {@code GET  /student-class-work-tracks} : get all the studentClassWorkTracks.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentClassWorkTracks in body.
	 */
	@GetMapping("/student-class-work-tracks")
	public ResponseEntity<List<StudentClassWorkTrack>> getAllStudentClassWorkTracks(@RequestParam Long schoolClassId,
			@RequestParam(required = false) Long classSubjectId, @RequestParam(required = false) Long classStudentId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

		StudentClassWorkTrackCriteria criteria = new StudentClassWorkTrackCriteria();
		LocalDateFilter startDateFilter = new LocalDateFilter();
		startDateFilter.setGreaterThanOrEqual(startDate);
		criteria.setCreateDate(startDateFilter);

		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setLessThanOrEqual(endDate);
		criteria.setCreateDate(endDateFilter);

		List<StudentClassWorkTrack> studentClassWorkTrackToReturn = new ArrayList<StudentClassWorkTrack>();
		log.debug("REST request to get StudentClassWorkTracks by criteria: {}", criteria);
		studentClassWorkTrackToReturn = studentClassWorkTrackQueryService.findByCriteria(criteria);

		if (schoolClassId > 0) {
			studentClassWorkTrackToReturn = studentClassWorkTrackToReturn.stream()
					.filter(s -> s.getClassStudent().getSchoolClass().getId().equals(schoolClassId))
					.collect(Collectors.toList());
		}

		if (classSubjectId > 0) {
			studentClassWorkTrackToReturn = studentClassWorkTrackToReturn.stream().filter(s -> s.getClassClassWork()
					.getChapterSection().getSubjectChapter().getClassSubject().getId().equals(classSubjectId))
					.collect(Collectors.toList());
		}
		Optional<String> loggedInUserOpt = SecurityUtils.getCurrentUserLogin();
		if (classStudentId > 0) {
			studentClassWorkTrackToReturn = studentClassWorkTrackToReturn.stream()
					.filter(s -> s.getClassStudent().getId().equals(classStudentId)).collect(Collectors.toList());
		} else if (loggedInUserOpt.isPresent()) {
			ClassStudent loggedInClassStudent = schoolCommonService.getClassStudent(loggedInUserOpt.get());
			if (loggedInClassStudent != null) {
				studentClassWorkTrackToReturn = studentClassWorkTrackToReturn.stream()
						.filter(s -> s.getClassStudent().getId().equals(loggedInClassStudent.getId()))
						.collect(Collectors.toList());
			}

		}

		studentClassWorkTrackToReturn = studentClassWorkTrackToReturn.stream().map(s -> setRemarks(s))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(studentClassWorkTrackToReturn);
	}

	private StudentClassWorkTrack setRemarks(StudentClassWorkTrack studentClassWorkTrack) {
		studentClassWorkTrack.setRemarks(studentClassWorkTrack.getClassClassWork().getChapterSection()
				.getSubjectChapter().getClassSubject().getSubjectName() + "--"
				+ studentClassWorkTrack.getClassClassWork().getChapterSection().getSubjectChapter().getChapterName());
		return studentClassWorkTrack;

	}

	/**
	 * {@code GET  /student-class-work-tracks/count} : count all the
	 * studentClassWorkTracks.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-class-work-tracks/count")
	public ResponseEntity<Long> countStudentClassWorkTracks(StudentClassWorkTrackCriteria criteria) {
		log.debug("REST request to count StudentClassWorkTracks by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentClassWorkTrackQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-class-work-tracks/:id} : get the "id"
	 * studentClassWorkTrack.
	 *
	 * @param id the id of the studentClassWorkTrackDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentClassWorkTrackDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-class-work-tracks/{id}")
	public ResponseEntity<StudentClassWorkTrack> getStudentClassWorkTrack(@PathVariable Long id) {
		log.debug("REST request to get StudentClassWorkTrack : {}", id);
		Optional<StudentClassWorkTrack> studentClassWorkTrack = studentClassWorkTrackService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentClassWorkTrack);
	}

	/**
	 * {@code DELETE  /student-class-work-tracks/:id} : delete the "id"
	 * studentClassWorkTrack.
	 *
	 * @param id the id of the studentClassWorkTrackDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/student-class-work-tracks/{id}")
	public ResponseEntity<Void> deleteStudentClassWorkTrack(@PathVariable Long id) {
		log.debug("REST request to delete StudentClassWorkTrack : {}", id);
		studentClassWorkTrackService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
