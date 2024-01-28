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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.repository.ClassSubjectRepository;
import com.ssik.manageit.security.SecurityUtils;
import com.ssik.manageit.service.ClassSubjectQueryService;
import com.ssik.manageit.service.ClassSubjectService;
import com.ssik.manageit.service.SchoolClassQueryService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.criteria.ClassSubjectCriteria;
import com.ssik.manageit.service.criteria.SchoolClassCriteria;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ClassSubject}.
 */
@RestController
@RequestMapping("/api")
public class ClassSubjectResource {

	private final Logger log = LoggerFactory.getLogger(ClassSubjectResource.class);

	private static final String ENTITY_NAME = "classSubject";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassSubjectService classSubjectService;

	private final ClassSubjectRepository classSubjectRepository;

	private final ClassSubjectQueryService classSubjectQueryService;

	private final SchoolCommonService schoolCommonService;

	private final SchoolClassQueryService schoolClassQueryService;

	public ClassSubjectResource(ClassSubjectService classSubjectService, ClassSubjectRepository classSubjectRepository,
			ClassSubjectQueryService classSubjectQueryService, SchoolCommonService schoolCommonService,
			SchoolClassQueryService schoolClassQueryService) {
		this.classSubjectService = classSubjectService;
		this.classSubjectRepository = classSubjectRepository;
		this.classSubjectQueryService = classSubjectQueryService;
		this.schoolCommonService = schoolCommonService;
		this.schoolClassQueryService = schoolClassQueryService;
	}

	/**
	 * {@code POST  /class-subjects} : Create a new classSubject.
	 *
	 * @param classSubjectDTO the classSubjectDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classSubjectDTO, or with status
	 *         {@code 400 (Bad Request)} if the classSubject has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-subjects")
	public ResponseEntity<ClassSubjectDTO> createClassSubject(@Valid @RequestBody ClassSubjectDTO classSubjectDTO)
			throws URISyntaxException {
		log.debug("REST request to save ClassSubject : {}", classSubjectDTO);
		if (classSubjectDTO.getId() != null) {
			throw new BadRequestAlertException("A new classSubject cannot already have an ID", ENTITY_NAME, "idexists");
		}
		List<ClassSubject> subjectsWithSameName = classSubjectRepository
				.findBySubjectName(classSubjectDTO.getSubjectName().trim());
		if (subjectsWithSameName != null && subjectsWithSameName.size() > 0) {
			throw new BadRequestAlertException("A  classSubject cannot have same name", ENTITY_NAME, "duplicate name");
		}
		if (classSubjectDTO.getSchoolClasses() == null || classSubjectDTO.getSchoolClasses().size() <= 0) {
			throw new BadRequestAlertException("A  Subject cannot be created without class", ENTITY_NAME,
					"class missing");
		}
		classSubjectDTO.setCreateDate(LocalDate.now());
		ClassSubjectDTO result = classSubjectService.save(classSubjectDTO);
		return ResponseEntity
				.created(new URI("/api/class-subjects/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-subjects/:id} : Updates an existing classSubject.
	 *
	 * @param id              the id of the classSubjectDTO to save.
	 * @param classSubjectDTO the classSubjectDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classSubjectDTO, or with status {@code 400 (Bad Request)}
	 *         if the classSubjectDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the classSubjectDTO couldn't
	 *         be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-subjects/{id}")
	public ResponseEntity<ClassSubjectDTO> updateClassSubject(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassSubjectDTO classSubjectDTO) throws URISyntaxException {
		log.debug("REST request to update ClassSubject : {}, {}", id, classSubjectDTO);
		if (classSubjectDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classSubjectDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classSubjectRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classSubjectDTO.setLastModified(LocalDate.now());
		ClassSubjectDTO result = classSubjectService.save(classSubjectDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classSubjectDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /class-subjects/:id} : Partial updates given fields of an
	 * existing classSubject, field will ignore if it is null
	 *
	 * @param id              the id of the classSubjectDTO to save.
	 * @param classSubjectDTO the classSubjectDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classSubjectDTO, or with status {@code 400 (Bad Request)}
	 *         if the classSubjectDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the classSubjectDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the classSubjectDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-subjects/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassSubjectDTO> partialUpdateClassSubject(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassSubjectDTO classSubjectDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassSubject partially : {}, {}", id, classSubjectDTO);
		if (classSubjectDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classSubjectDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classSubjectRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		classSubjectDTO.setLastModified(LocalDate.now());
		Optional<ClassSubjectDTO> result = classSubjectService.partialUpdate(classSubjectDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classSubjectDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-subjects} : get all the classSubjects.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classSubjects in body.
	 */
	@GetMapping("/class-subjects")
	public ResponseEntity<List<ClassSubjectDTO>> getAllClassSubjects(ClassSubjectCriteria criteria, Pageable pageable) {
		log.debug("REST request to get ClassSubjects by criteria: {}", criteria);

		// if classid has not been supplied, then use all the classes else fetch only
		// for passed class ID

		if (criteria.getSchoolClassId() == null || criteria.getSchoolClassId().getEquals() <= 0) {
			// If there is a logged in user then only fetch his class
			Optional<String> loggedInUserOpt = SecurityUtils.getCurrentUserLogin();
			if (loggedInUserOpt.isPresent()) {
				ClassStudent loggedInClassStudent = schoolCommonService.getClassStudent(loggedInUserOpt.get());
				if (loggedInClassStudent != null) {
					LongFilter schoolClassId = new LongFilter();
					schoolClassId.setEquals(loggedInClassStudent.getSchoolClass().getId());
					criteria.setSchoolClassId(schoolClassId);
				}
			} else {
				// if not logged in user then fetch all subject across all class for this school
				// get the school name
				School school = schoolCommonService.getUserSchool();

				// get all the classes for this school
				SchoolClassCriteria schoolClassCriteria = new SchoolClassCriteria();
				LongFilter schoolIdFilter = new LongFilter();
				schoolIdFilter.setEquals(school.getId());
				schoolClassCriteria.setSchoolId(schoolIdFilter);
				List<SchoolClass> schoolClasses = schoolClassQueryService.findByCriteria(schoolClassCriteria);
				List<Long> classIds = schoolClasses.stream().map(a -> a.getId()).collect(Collectors.toList());

				// get subject only for the classes which are being taught in this school
				LongFilter schoolClassIds = new LongFilter();
				schoolClassIds.setIn(classIds);
				criteria.setSchoolClassId(schoolClassIds);
			}
		}
		Page<ClassSubjectDTO> page = classSubjectQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /class-subjects/count} : count all the classSubjects.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-subjects/count")
	public ResponseEntity<Long> countClassSubjects(ClassSubjectCriteria criteria) {
		log.debug("REST request to count ClassSubjects by criteria: {}", criteria);
		return ResponseEntity.ok().body(classSubjectQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-subjects/:id} : get the "id" classSubject.
	 *
	 * @param id the id of the classSubjectDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classSubjectDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-subjects/{id}")
	public ResponseEntity<ClassSubjectDTO> getClassSubject(@PathVariable Long id) {
		log.debug("REST request to get ClassSubject : {}", id);
		Optional<ClassSubjectDTO> classSubjectDTO = classSubjectService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classSubjectDTO);
	}

	/**
	 * {@code DELETE  /class-subjects/:id} : delete the "id" classSubject.
	 *
	 * @param id the id of the classSubjectDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-subjects/{id}")
	public ResponseEntity<Void> deleteClassSubject(@PathVariable Long id) {
		log.debug("REST request to delete ClassSubject : {}", id);
		classSubjectService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
