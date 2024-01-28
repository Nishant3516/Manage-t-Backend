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

import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassLessionPlanTrack;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.repository.ClassLessionPlanTrackRepository;
import com.ssik.manageit.security.SecurityUtils;
import com.ssik.manageit.service.ClassLessionPlanQueryService;
import com.ssik.manageit.service.ClassLessionPlanTrackQueryService;
import com.ssik.manageit.service.ClassLessionPlanTrackService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.criteria.ClassLessionPlanCriteria;
import com.ssik.manageit.service.criteria.ClassLessionPlanTrackCriteria;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.ClassLessionPlanTrack}.
 */
@RestController
@RequestMapping("/api")
public class ClassLessionPlanTrackResource {

	private final Logger log = LoggerFactory.getLogger(ClassLessionPlanTrackResource.class);

	private static final String ENTITY_NAME = "classLessionPlanTrack";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassLessionPlanTrackService classLessionPlanTrackService;

	private final ClassLessionPlanTrackRepository classLessionPlanTrackRepository;

	private final ClassLessionPlanTrackQueryService classLessionPlanTrackQueryService;

	@Autowired
	private ClassLessionPlanQueryService classLessionPlanQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;

	public ClassLessionPlanTrackResource(ClassLessionPlanTrackService classLessionPlanTrackService,
			ClassLessionPlanTrackRepository classLessionPlanTrackRepository,
			ClassLessionPlanTrackQueryService classLessionPlanTrackQueryService) {
		this.classLessionPlanTrackService = classLessionPlanTrackService;
		this.classLessionPlanTrackRepository = classLessionPlanTrackRepository;
		this.classLessionPlanTrackQueryService = classLessionPlanTrackQueryService;
	}

	/**
	 * {@code POST  /class-lession-plan-tracks} : Create a new
	 * classLessionPlanTrack.
	 *
	 * @param classLessionPlanTrackDTO the classLessionPlanTrackDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classLessionPlanTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlanTrack has already an
	 *         ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-lession-plan-tracks")
	public ResponseEntity<ClassLessionPlanTrackDTO> createClassLessionPlanTrack(
			@Valid @RequestBody ClassLessionPlanTrackDTO classLessionPlanTrackDTO) throws URISyntaxException {
		log.debug("REST request to save ClassLessionPlanTrack : {}", classLessionPlanTrackDTO);
		if (classLessionPlanTrackDTO.getId() != null) {
			throw new BadRequestAlertException("A new classLessionPlanTrack cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		classLessionPlanTrackDTO.setCreateDate(LocalDate.now());
		ClassLessionPlanTrackDTO result = classLessionPlanTrackService.save(classLessionPlanTrackDTO);
		return ResponseEntity
				.created(new URI("/api/class-lession-plan-tracks/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-lession-plan-tracks/:id} : Updates an existing
	 * classLessionPlanTrack.
	 *
	 * @param id                       the id of the classLessionPlanTrackDTO to
	 *                                 save.
	 * @param classLessionPlanTrackDTO the classLessionPlanTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classLessionPlanTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlanTrackDTO is not
	 *         valid, or with status {@code 500 (Internal Server Error)} if the
	 *         classLessionPlanTrackDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-lession-plan-tracks/{id}")
	public ResponseEntity<ClassLessionPlanTrackDTO> updateClassLessionPlanTrack(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassLessionPlanTrackDTO classLessionPlanTrackDTO) throws URISyntaxException {
		log.debug("REST request to update ClassLessionPlanTrack : {}, {}", id, classLessionPlanTrackDTO);
		if (classLessionPlanTrackDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classLessionPlanTrackDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classLessionPlanTrackRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classLessionPlanTrackDTO.setLastModified(LocalDate.now());

		ClassLessionPlanTrackDTO result = classLessionPlanTrackService.save(classLessionPlanTrackDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classLessionPlanTrackDTO.getId().toString())).body(result);
	}

	@PutMapping("/class-lession-plan-tracks")
	public ResponseEntity<List<ClassLessionPlanTrack>> finalizeClassLessionPlanTrack(
			@Valid @RequestBody List<ClassLessionPlanTrack> classLessionPlanTracks) throws URISyntaxException {

		List<ClassLessionPlanTrack> updatedClassLessionPlanTracks = new ArrayList<ClassLessionPlanTrack>();

		if (classLessionPlanTracks != null && classLessionPlanTracks.size() > 0) {
			log.debug("REST request to update ClassLessionPlanTrack :  {}", classLessionPlanTracks.size());
			for (ClassLessionPlanTrack classLessionPlanTrack : classLessionPlanTracks) {
				if (classLessionPlanTrack.getId() == null) {
					throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
				}

				if (!classLessionPlanTrackRepository.existsById(classLessionPlanTrack.getId())) {
					throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
				}
				// cancel date is a way to identify that the tracking of the lession plan cannot
				// be changed
				classLessionPlanTrack.setCancelDate(LocalDate.now());
			}

			updatedClassLessionPlanTracks = classLessionPlanTrackRepository.saveAll(classLessionPlanTracks);

		}
		return ResponseEntity.ok().body(updatedClassLessionPlanTracks);
	}

	/**
	 * {@code PATCH  /class-lession-plan-tracks/:id} : Partial updates given fields
	 * of an existing classLessionPlanTrack, field will ignore if it is null
	 *
	 * @param id                       the id of the classLessionPlanTrackDTO to
	 *                                 save.
	 * @param classLessionPlanTrackDTO the classLessionPlanTrackDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classLessionPlanTrackDTO, or with status
	 *         {@code 400 (Bad Request)} if the classLessionPlanTrackDTO is not
	 *         valid, or with status {@code 404 (Not Found)} if the
	 *         classLessionPlanTrackDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the classLessionPlanTrackDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-lession-plan-tracks/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassLessionPlanTrackDTO> partialUpdateClassLessionPlanTrack(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassLessionPlanTrackDTO classLessionPlanTrackDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassLessionPlanTrack partially : {}, {}", id,
				classLessionPlanTrackDTO);
		if (classLessionPlanTrackDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classLessionPlanTrackDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classLessionPlanTrackRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classLessionPlanTrackDTO.setLastModified(LocalDate.now());
		Optional<ClassLessionPlanTrackDTO> result = classLessionPlanTrackService
				.partialUpdate(classLessionPlanTrackDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classLessionPlanTrackDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-lession-plan-tracks} : get all the classLessionPlanTracks.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classLessionPlanTracks in body.
	 */
	@GetMapping("/class-lession-plan-tracks")
	public ResponseEntity<List<ClassLessionPlanTrack>> getAllClassLessionPlanTracks(
			@RequestParam(required = false) Long schoolClassId, @RequestParam(required = false) Long classSubjectId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

		ClassLessionPlanCriteria classLessionPlanCriteria = new ClassLessionPlanCriteria();
		if (schoolClassId == null || schoolClassId <= 0) {
			Optional<String> loggedInUserOpt = SecurityUtils.getCurrentUserLogin();
			if (loggedInUserOpt.isPresent()) {
				ClassStudent loggedInClassStudent = schoolCommonService.getClassStudent(loggedInUserOpt.get());
				schoolClassId = loggedInClassStudent.getSchoolClass().getId();
			}

		}
		LongFilter schoolClassFilter = new LongFilter();
		schoolClassFilter.setEquals(schoolClassId);
		classLessionPlanCriteria.setSchoolClassId(schoolClassFilter);

		if (classSubjectId != null && classSubjectId > 0) {
			LongFilter classSubjectFilter = new LongFilter();
			classSubjectFilter.setEquals(classSubjectId);
			classLessionPlanCriteria.setClassSubjectId(classSubjectFilter);

		}

		List<ClassLessionPlan> classLessionPlans = classLessionPlanQueryService
				.findByCriteria(classLessionPlanCriteria);
		List<Long> classLessionPlanIds = classLessionPlans.stream().map(ClassLessionPlan::getId)
				.collect(Collectors.toList());

		LongFilter classLessionPlanFilter = new LongFilter();
		classLessionPlanFilter.setIn(classLessionPlanIds);
		ClassLessionPlanTrackCriteria criteria = new ClassLessionPlanTrackCriteria();
		log.debug("REST request to get ClassLessionPlanTracks by criteria: {}", criteria);

		// set class lession plan Ids
		criteria.setClassLessionPlanId(classLessionPlanFilter);

		// set start date and end date for the class lession plan
		LocalDateFilter startDateFilter = new LocalDateFilter();
		startDateFilter.setGreaterThanOrEqual(startDate);
		criteria.setCreateDate(startDateFilter);

		// sset end date filter
		LocalDateFilter endDateFilter = new LocalDateFilter();
		endDateFilter.setLessThanOrEqual(endDate);
		criteria.setCreateDate(endDateFilter);

		List<ClassLessionPlanTrack> classLessionPlanTracks = classLessionPlanTrackQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(classLessionPlanTracks);
	}

	/**
	 * {@code GET  /class-lession-plan-tracks/count} : count all the
	 * classLessionPlanTracks.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-lession-plan-tracks/count")
	public ResponseEntity<Long> countClassLessionPlanTracks(ClassLessionPlanTrackCriteria criteria) {
		log.debug("REST request to count ClassLessionPlanTracks by criteria: {}", criteria);
		return ResponseEntity.ok().body(classLessionPlanTrackQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-lession-plan-tracks/:id} : get the "id"
	 * classLessionPlanTrack.
	 *
	 * @param id the id of the classLessionPlanTrackDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classLessionPlanTrackDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-lession-plan-tracks/{id}")
	public ResponseEntity<ClassLessionPlanTrackDTO> getClassLessionPlanTrack(@PathVariable Long id) {
		log.debug("REST request to get ClassLessionPlanTrack : {}", id);
		Optional<ClassLessionPlanTrackDTO> classLessionPlanTrackDTO = classLessionPlanTrackService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classLessionPlanTrackDTO);
	}

	/**
	 * {@code DELETE  /class-lession-plan-tracks/:id} : delete the "id"
	 * classLessionPlanTrack.
	 *
	 * @param id the id of the classLessionPlanTrackDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-lession-plan-tracks/{id}")
	public ResponseEntity<Void> deleteClassLessionPlanTrack(@PathVariable Long id) {
		log.debug("REST request to delete ClassLessionPlanTrack : {}", id);
		classLessionPlanTrackService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
