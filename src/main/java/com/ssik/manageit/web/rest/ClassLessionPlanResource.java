package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import com.ssik.manageit.domain.enumeration.TaskStatus;
import com.ssik.manageit.domain.enumeration.WorkStatus;
import com.ssik.manageit.repository.ClassLessionPlanRepository;
import com.ssik.manageit.service.ChapterSectionService;
import com.ssik.manageit.service.ClassClassWorkService;
import com.ssik.manageit.service.ClassHomeWorkService;
import com.ssik.manageit.service.ClassLessionPlanQueryService;
import com.ssik.manageit.service.ClassLessionPlanService;
import com.ssik.manageit.service.ClassLessionPlanTrackService;
import com.ssik.manageit.service.ClassStudentQueryService;
import com.ssik.manageit.service.StudentClassWorkTrackService;
import com.ssik.manageit.service.StudentHomeWorkTrackService;
import com.ssik.manageit.service.criteria.ClassLessionPlanCriteria;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import com.ssik.manageit.service.dto.ClassLessionPlanDTO;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;
import com.ssik.manageit.service.mapper.StudentClassWorkTrackMapper;
import com.ssik.manageit.service.mapper.StudentHomeWorkTrackMapper;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.ClassLessionPlan}.
 */
@RestController
@RequestMapping("/api")
public class ClassLessionPlanResource {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanResource.class);

	private static final String ENTITY_NAME = "classLessionPlan";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassLessionPlanService classLessionPlanService;

	private final ClassLessionPlanRepository classLessionPlanRepository;

	private final ClassLessionPlanQueryService classLessionPlanQueryService;
	private final ChapterSectionService chapterSectionService;

	@Autowired
	private ClassClassWorkService classClassWorkService;

	@Autowired
	private ClassHomeWorkService classHomeWorkService;

	@Autowired
	private ClassLessionPlanTrackService classLessionPlanTrackService;

	@Autowired
	private StudentHomeWorkTrackService studentHomeWorkTrackService;

	@Autowired
	private StudentClassWorkTrackService studentClassWorkTrackService;

	@Autowired
	private ClassStudentQueryService classStudentQueryService;

	@Autowired
	private StudentClassWorkTrackMapper studentClassWorkTrackMapper;

	@Autowired
	private StudentHomeWorkTrackMapper studentHomeWorkTrackMapper;
	@Autowired
	private ClassStudentMapper classStudentMapper;

	public ClassLessionPlanResource(ClassLessionPlanService classLessionPlanService,
			ClassLessionPlanRepository classLessionPlanRepository,
			ClassLessionPlanQueryService classLessionPlanQueryService, ChapterSectionService chapterSectionService

	) {
		this.classLessionPlanService = classLessionPlanService;
		this.classLessionPlanRepository = classLessionPlanRepository;
		this.classLessionPlanQueryService = classLessionPlanQueryService;
		this.chapterSectionService = chapterSectionService;
	}

	/**
	 * {@code POST  /class-lession-plans} : Create a new classLessionPlan.
	 *
	 * @param classLessionPlanDTO the classLessionPlanDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classLessionPlanDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlan has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-lession-plans")
	public ResponseEntity<ClassLessionPlanDTO> createClassLessionPlan(
			@Valid @RequestBody ClassLessionPlanDTO classLessionPlanDTO) throws URISyntaxException {
		log.debug("REST request to save ClassLessionPlan : {}", classLessionPlanDTO);
		if (classLessionPlanDTO.getId() != null) {
			throw new BadRequestAlertException("A new classLessionPlan cannot already have an ID", ENTITY_NAME,
					"idexists");
		}

		if (classLessionPlanDTO.getSchoolClass() == null || classLessionPlanDTO.getClassSubject() == null
				|| classLessionPlanDTO.getSubjectChapter() == null || classLessionPlanDTO.getChapterSection() == null) {
			throw new BadRequestAlertException("Missing details to create a lession plan", ENTITY_NAME,
					"missing details");
		}

		// if (classLessionPlanDTO.getWorkStatus() == null) {
		classLessionPlanDTO.setCreateDate(LocalDate.now());
		classLessionPlanDTO.setWorkStatus(TaskStatus.NotStarted);
		// }

		// Get list of all the students for this class
		ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
		LongFilter classIdFilter = new LongFilter();
		classIdFilter.setEquals(classLessionPlanDTO.getSchoolClass().getId());
		classStudentCriteria.setSchoolClassId(classIdFilter);

		List<ClassStudent> classStudents = classStudentQueryService.findByCriteria(classStudentCriteria);

		// if class work has been specified, create a classwork for that day
		if (classLessionPlanDTO.getClassWorkText() != null) {
			List<StudentClassWorkTrack> studentClassWorkTracks = new ArrayList<StudentClassWorkTrack>();
			// Create a classwork
			ClassClassWorkDTO classClassWork = new ClassClassWorkDTO();
			classClassWork.setClassWorkText(classLessionPlanDTO.getClassWorkText());
			classClassWork.setSchoolDate(classLessionPlanDTO.getSchoolDate());
			classClassWork.setStudentAssignmentType(StudentAssignmentType.READING_WRITING);
			classClassWork.setChapterSection(classLessionPlanDTO.getChapterSection());

			ClassClassWorkDTO classClassWorkDto = classClassWorkService.save(classClassWork);

			// if class work is specified then create a class work track for the same for
			// each student of the class

			for (ClassStudent classStudent : classStudents) {
				StudentClassWorkTrackDTO studentClassWorkTrackDTO = new StudentClassWorkTrackDTO();
				studentClassWorkTrackDTO.setCreateDate(classLessionPlanDTO.getSchoolDate());
				studentClassWorkTrackDTO.setClassClassWork(classClassWorkDto);
				studentClassWorkTrackDTO.setWorkStatus(WorkStatus.NotDone);
				studentClassWorkTrackDTO.setClassStudent(classStudentMapper.toDto(classStudent));
				studentClassWorkTracks.add(studentClassWorkTrackMapper.toEntity(studentClassWorkTrackDTO));

			}

			studentClassWorkTrackService.saveAll(studentClassWorkTracks);
		}

		// if class home work has been specified then create a class homework for that
		// day

		if (classLessionPlanDTO.getHomeWorkText() != null) {
			List<StudentHomeWorkTrack> studentHomeWorkTracks = new ArrayList<StudentHomeWorkTrack>();

			// create a homework
			ClassHomeWorkDTO classHomeWorkDTO = new ClassHomeWorkDTO();
			classHomeWorkDTO.setHomeWorkText(classLessionPlanDTO.getHomeWorkText());
			classHomeWorkDTO.setSchoolDate(classLessionPlanDTO.getSchoolDate());
			classHomeWorkDTO.setStudentAssignmentType(StudentAssignmentType.READING_WRITING);
			classHomeWorkDTO.setChapterSection(classLessionPlanDTO.getChapterSection());

			ClassHomeWorkDTO classHomeWorkSaved = classHomeWorkService.save(classHomeWorkDTO);

			for (ClassStudent classStudent : classStudents) {

				StudentHomeWorkTrackDTO studentHomeWorkTrackDto = new StudentHomeWorkTrackDTO();
				studentHomeWorkTrackDto.setCreateDate(classLessionPlanDTO.getSchoolDate());
				studentHomeWorkTrackDto.setClassHomeWork(classHomeWorkSaved);
				studentHomeWorkTrackDto.setWorkStatus(WorkStatus.NotDone);
				studentHomeWorkTrackDto.setClassStudent(classStudentMapper.toDto(classStudent));
				studentHomeWorkTracks.add(studentHomeWorkTrackMapper.toEntity(studentHomeWorkTrackDto));

			}
			studentHomeWorkTrackService.saveAll(studentHomeWorkTracks);
			// if class home work is created then create a homework track for that day
		}

		ClassLessionPlanDTO result = classLessionPlanService.save(classLessionPlanDTO);

		// create a lession plan track
		ClassLessionPlanTrackDTO classLessionPlanTrackDto = new ClassLessionPlanTrackDTO();
		classLessionPlanTrackDto.setCreateDate(classLessionPlanDTO.getSchoolDate());
		classLessionPlanTrackDto.setClassLessionPlan(result);
		classLessionPlanTrackDto.setWorkStatus(TaskStatus.NotStarted);

		classLessionPlanTrackService.save(classLessionPlanTrackDto);

		return ResponseEntity
				.created(new URI("/api/class-lession-plans/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-lession-plans/:id} : Updates an existing classLessionPlan.
	 *
	 * @param id                  the id of the classLessionPlanDTO to save.
	 * @param classLessionPlanDTO the classLessionPlanDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classLessionPlanDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlanDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         classLessionPlanDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-lession-plans/{id}")
	public ResponseEntity<ClassLessionPlanDTO> updateClassLessionPlan(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassLessionPlanDTO classLessionPlanDTO) throws URISyntaxException {
		log.debug("REST request to update ClassLessionPlan : {}, {}", id, classLessionPlanDTO);
		if (classLessionPlanDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classLessionPlanDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classLessionPlanRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classLessionPlanDTO.setLastModified(LocalDate.now());
		ClassLessionPlanDTO result = classLessionPlanService.save(classLessionPlanDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classLessionPlanDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /class-lession-plans/:id} : Partial updates given fields of an
	 * existing classLessionPlan, field will ignore if it is null
	 *
	 * @param id                  the id of the classLessionPlanDTO to save.
	 * @param classLessionPlanDTO the classLessionPlanDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classLessionPlanDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlanDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the classLessionPlanDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         classLessionPlanDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-lession-plans/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassLessionPlanDTO> partialUpdateClassLessionPlan(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassLessionPlanDTO classLessionPlanDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassLessionPlan partially : {}, {}", id, classLessionPlanDTO);
		if (classLessionPlanDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classLessionPlanDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classLessionPlanRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classLessionPlanDTO.setLastModified(LocalDate.now());
		Optional<ClassLessionPlanDTO> result = classLessionPlanService.partialUpdate(classLessionPlanDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classLessionPlanDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-lession-plans} : get all the classLessionPlans.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classLessionPlans in body.
	 */
	@GetMapping("/class-lession-plans")
	public ResponseEntity<List<ClassLessionPlan>> getAllClassHomeWorks(@RequestParam Long classId,
			@RequestParam(required = false) Long subjectId, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		// Get all the sections for the subject passed or all the sections for all
		// chapters of a subjects in a class

		// get all the homework for all the sections
		List<Long> sectionIds = chapterSectionService.getAllSectionsForAclassAndSubject(classId, subjectId);

		ClassLessionPlanCriteria classLessionPlanCriteria = new ClassLessionPlanCriteria();
		LongFilter sectionIdsFilter = new LongFilter();
		sectionIdsFilter.setIn(sectionIds);
		classLessionPlanCriteria.setChapterSectionId(sectionIdsFilter);

		LocalDateFilter classLessionPlanDateFilter = new LocalDateFilter();
		classLessionPlanDateFilter.setGreaterThanOrEqual(startDate);
		classLessionPlanDateFilter.setLessThanOrEqual(endDate);
		classLessionPlanCriteria.setSchoolDate(classLessionPlanDateFilter);

		List<ClassLessionPlan> sectionClassWorks = classLessionPlanQueryService
				.findByCriteria(classLessionPlanCriteria);

		return ResponseEntity.ok().body(sectionClassWorks);
	}

	/**
	 * {@code GET  /class-lession-plans/count} : count all the classLessionPlans.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-lession-plans/count")
	public ResponseEntity<Long> countClassLessionPlans(ClassLessionPlanCriteria criteria) {
		log.debug("REST request to count ClassLessionPlans by criteria: {}", criteria);
		return ResponseEntity.ok().body(classLessionPlanQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-lession-plans/:id} : get the "id" classLessionPlan.
	 *
	 * @param id the id of the classLessionPlanDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classLessionPlanDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-lession-plans/{id}")
	public ResponseEntity<ClassLessionPlan> getClassLessionPlan(@PathVariable Long id) {
		log.debug("REST request to get ClassLessionPlan : {}", id);
		Optional<ClassLessionPlan> classLessionPlanDTO = classLessionPlanService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classLessionPlanDTO);
	}

	/**
	 * {@code DELETE  /class-lession-plans/:id} : delete the "id" classLessionPlan.
	 *
	 * @param id the id of the classLessionPlanDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-lession-plans/{id}")
	public ResponseEntity<Void> deleteClassLessionPlan(@PathVariable Long id) {
		log.debug("REST request to delete ClassLessionPlan : {}", id);
		classLessionPlanService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
