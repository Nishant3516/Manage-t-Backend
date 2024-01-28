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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.repository.StudentHomeWorkTrackRepository;
import com.ssik.manageit.security.SecurityUtils;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentHomeWorkTrackQueryService;
import com.ssik.manageit.service.StudentHomeWorkTrackService;
import com.ssik.manageit.service.criteria.StudentHomeWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentHomeWorkTrack}.
 */
@RestController
@RequestMapping("/api")
public class StudentHomeWorkTrackResource {

	private final Logger log = LoggerFactory.getLogger(StudentHomeWorkTrackResource.class);

	private static final String ENTITY_NAME = "studentHomeWorkTrack";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentHomeWorkTrackService studentHomeWorkTrackService;

	private final StudentHomeWorkTrackRepository studentHomeWorkTrackRepository;

	private final StudentHomeWorkTrackQueryService studentHomeWorkTrackQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;

	public StudentHomeWorkTrackResource(StudentHomeWorkTrackService studentHomeWorkTrackService,
			StudentHomeWorkTrackRepository studentHomeWorkTrackRepository,
			StudentHomeWorkTrackQueryService studentHomeWorkTrackQueryService) {
		this.studentHomeWorkTrackService = studentHomeWorkTrackService;
		this.studentHomeWorkTrackRepository = studentHomeWorkTrackRepository;
		this.studentHomeWorkTrackQueryService = studentHomeWorkTrackQueryService;
	}

	/**
	 * {@code POST  /student-home-work-tracks} : Create a new studentHomeWorkTrack.
	 *
	 * @param studentHomeWorkTrackDTO the studentHomeWorkTrackDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentHomeWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentHomeWorkTrack has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/student-home-work-tracks")
	public ResponseEntity<StudentHomeWorkTrackDTO> createStudentHomeWorkTrack(
			@Valid @RequestBody StudentHomeWorkTrackDTO studentHomeWorkTrackDTO) throws URISyntaxException {
		log.debug("REST request to save StudentHomeWorkTrack : {}", studentHomeWorkTrackDTO);
		if (studentHomeWorkTrackDTO.getId() != null) {
			throw new BadRequestAlertException("A new studentHomeWorkTrack cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		studentHomeWorkTrackDTO.setCreateDate(LocalDate.now());
		StudentHomeWorkTrackDTO result = studentHomeWorkTrackService.save(studentHomeWorkTrackDTO);
		return ResponseEntity
				.created(new URI("/api/student-home-work-tracks/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /student-home-work-tracks/:id} : Updates an existing
	 * studentHomeWorkTrack.
	 *
	 * @param id                      the id of the studentHomeWorkTrackDTO to save.
	 * @param studentHomeWorkTrackDTO the studentHomeWorkTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentHomeWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentHomeWorkTrackDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         studentHomeWorkTrackDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/student-home-work-tracks/{id}")
	public ResponseEntity<StudentHomeWorkTrackDTO> updateStudentHomeWorkTrack(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody StudentHomeWorkTrackDTO studentHomeWorkTrackDTO) throws URISyntaxException {
		log.debug("REST request to update StudentHomeWorkTrack : {}, {}", id, studentHomeWorkTrackDTO);
		if (studentHomeWorkTrackDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, studentHomeWorkTrackDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!studentHomeWorkTrackRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		studentHomeWorkTrackDTO.setLastModified(LocalDate.now());
		StudentHomeWorkTrackDTO result = studentHomeWorkTrackService.save(studentHomeWorkTrackDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				studentHomeWorkTrackDTO.getId().toString())).body(result);
	}

	@PutMapping("/student-home-work-tracks")
	public ResponseEntity<List<StudentHomeWorkTrack>> finalizeStudentHomeWorkTrack(
			@Valid @RequestBody List<StudentHomeWorkTrack> studentHomeWorkTracks) throws URISyntaxException {
		List<StudentHomeWorkTrack> updatedStudentHomeWorkTracks = new ArrayList<StudentHomeWorkTrack>();
		if (studentHomeWorkTracks != null && studentHomeWorkTracks.size() > 0) {
			log.debug("REST request to update StudentHomeWorkTrack :  {}", studentHomeWorkTracks.size());
			for (StudentHomeWorkTrack studentHomeWorkTrack : studentHomeWorkTracks) {
				if (studentHomeWorkTrack.getId() == null) {
					throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
				}

				if (!studentHomeWorkTrackRepository.existsById(studentHomeWorkTrack.getId())) {
					throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
				}
				// cancel date indicate that these records cannot further be modified from the
				// UI
				studentHomeWorkTrack.setCancelDate(LocalDate.now());
			}
			updatedStudentHomeWorkTracks = studentHomeWorkTrackService.saveAll(studentHomeWorkTracks);
		}
		return ResponseEntity.ok().body(updatedStudentHomeWorkTracks);
	}

	/**
	 * {@code PATCH  /student-home-work-tracks/:id} : Partial updates given fields
	 * of an existing studentHomeWorkTrack, field will ignore if it is null
	 *
	 * @param id                      the id of the studentHomeWorkTrackDTO to save.
	 * @param studentHomeWorkTrackDTO the studentHomeWorkTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentHomeWorkTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentHomeWorkTrackDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         studentHomeWorkTrackDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the studentHomeWorkTrackDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/student-home-work-tracks/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<StudentHomeWorkTrackDTO> partialUpdateStudentHomeWorkTrack(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody StudentHomeWorkTrackDTO studentHomeWorkTrackDTO) throws URISyntaxException {
		log.debug("REST request to partial update StudentHomeWorkTrack partially : {}, {}", id,
				studentHomeWorkTrackDTO);
		if (studentHomeWorkTrackDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, studentHomeWorkTrackDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!studentHomeWorkTrackRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		studentHomeWorkTrackDTO.setLastModified(LocalDate.now());
		Optional<StudentHomeWorkTrackDTO> result = studentHomeWorkTrackService.partialUpdate(studentHomeWorkTrackDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, studentHomeWorkTrackDTO.getId().toString()));
	}

	/**
	 * {@code GET  /student-home-work-tracks} : get all the studentHomeWorkTracks.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentHomeWorkTracks in body.
	 */
	@GetMapping("/student-home-work-tracks")
	public ResponseEntity<List<StudentHomeWorkTrack>> getAllStudentHomeWorkTracks(@RequestParam Long schoolClassId,
			@RequestParam(required = false) Long classSubjectId, @RequestParam(required = false) Long classStudentId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

		StudentHomeWorkTrackCriteria criteria = new StudentHomeWorkTrackCriteria();
		LocalDateFilter startDateFilter = new LocalDateFilter();
		startDateFilter.setGreaterThanOrEqual(startDate);
		criteria.setCreateDate(startDateFilter);

		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setLessThanOrEqual(endDate);
		criteria.setCreateDate(endDateFilter);

		List<StudentHomeWorkTrack> studentHomeWorkTrackToReturn = new ArrayList<StudentHomeWorkTrack>();
		log.debug("REST request to get StudentHomeWorkTracks by criteria: {}", criteria);
		studentHomeWorkTrackToReturn = studentHomeWorkTrackQueryService.findByCriteria(criteria);

		if (schoolClassId > 0) {
			studentHomeWorkTrackToReturn = studentHomeWorkTrackToReturn.stream()
					.filter(s -> s.getClassStudent().getSchoolClass().getId().equals(schoolClassId))
					.collect(Collectors.toList());
		}

		if (classSubjectId > 0) {
			studentHomeWorkTrackToReturn = studentHomeWorkTrackToReturn.stream().filter(s -> s.getClassHomeWork()
					.getChapterSection().getSubjectChapter().getClassSubject().getId().equals(classSubjectId))
					.collect(Collectors.toList());
		}

		Optional<String> loggedInUserOpt = SecurityUtils.getCurrentUserLogin();
		if (classStudentId > 0) {
			studentHomeWorkTrackToReturn = studentHomeWorkTrackToReturn.stream()
					.filter(s -> s.getClassStudent().getId().equals(classStudentId)).collect(Collectors.toList());
		} else if (loggedInUserOpt.isPresent()) {
			ClassStudent loggedInClassStudent = schoolCommonService.getClassStudent(loggedInUserOpt.get());
			if (loggedInClassStudent != null) {
				studentHomeWorkTrackToReturn = studentHomeWorkTrackToReturn.stream()
						.filter(s -> s.getClassStudent().getId().equals(loggedInClassStudent.getId()))
						.collect(Collectors.toList());
			}

		}

		studentHomeWorkTrackToReturn = studentHomeWorkTrackToReturn.stream().map(s -> setRemarks(s))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(studentHomeWorkTrackToReturn);
	}

	private StudentHomeWorkTrack setRemarks(StudentHomeWorkTrack studentHomeWorkTrack) {
		studentHomeWorkTrack.setRemarks(studentHomeWorkTrack.getClassHomeWork().getChapterSection().getSubjectChapter()
				.getClassSubject().getSubjectName() + "--"
				+ studentHomeWorkTrack.getClassHomeWork().getChapterSection().getSubjectChapter().getChapterName());
		return studentHomeWorkTrack;

	}

	/**
	 * {@code GET  /student-home-work-tracks/count} : count all the
	 * studentHomeWorkTracks.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/student-home-work-tracks/count")
	public ResponseEntity<Long> countStudentHomeWorkTracks(StudentHomeWorkTrackCriteria criteria) {
		log.debug("REST request to count StudentHomeWorkTracks by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentHomeWorkTrackQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-home-work-tracks/:id} : get the "id"
	 * studentHomeWorkTrack.
	 *
	 * @param id the id of the studentHomeWorkTrackDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentHomeWorkTrackDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/student-home-work-tracks/{id}")
	public ResponseEntity<StudentHomeWorkTrack> getStudentHomeWorkTrack(@PathVariable Long id) {
		log.debug("REST request to get StudentHomeWorkTrack : {}", id);
		Optional<StudentHomeWorkTrack> studentHomeWorkTrack = studentHomeWorkTrackService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentHomeWorkTrack);
	}

	/**
	 * {@code DELETE  /student-home-work-tracks/:id} : delete the "id"
	 * studentHomeWorkTrack.
	 *
	 * @param id the id of the studentHomeWorkTrackDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/student-home-work-tracks/{id}")
	public ResponseEntity<Void> deleteStudentHomeWorkTrack(@PathVariable Long id) {
		log.debug("REST request to delete StudentHomeWorkTrack : {}", id);
		studentHomeWorkTrackService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
