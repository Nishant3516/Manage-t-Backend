package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.repository.ClassClassWorkRepository;
import com.ssik.manageit.service.ChapterSectionService;
import com.ssik.manageit.service.ClassClassWorkQueryService;
import com.ssik.manageit.service.ClassClassWorkService;
import com.ssik.manageit.service.criteria.ClassClassWorkCriteria;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ClassClassWork}.
 */
@RestController
@RequestMapping("/api")
public class ClassClassWorkResource {

	private final Logger log = LoggerFactory.getLogger(ClassClassWorkResource.class);

	private static final String ENTITY_NAME = "classClassWork";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassClassWorkService classClassWorkService;

	private final ClassClassWorkRepository classClassWorkRepository;

	private final ClassClassWorkQueryService classClassWorkQueryService;
	private final ChapterSectionService chapterSectionService;

	public ClassClassWorkResource(ClassClassWorkService classClassWorkService,
			ClassClassWorkRepository classClassWorkRepository, ClassClassWorkQueryService classClassWorkQueryService,
			ChapterSectionService chapterSectionService

	) {
		this.classClassWorkService = classClassWorkService;
		this.classClassWorkRepository = classClassWorkRepository;
		this.classClassWorkQueryService = classClassWorkQueryService;
		this.chapterSectionService = chapterSectionService;
	}

	/**
	 * {@code POST  /class-class-works} : Create a new classClassWork.
	 *
	 * @param classClassWorkDTO the classClassWorkDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classClassWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classClassWork has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-class-works")
	public ResponseEntity<ClassClassWorkDTO> createClassClassWork(
			@Valid @RequestBody ClassClassWorkDTO classClassWorkDTO) throws URISyntaxException {
		log.debug("REST request to save ClassClassWork : {}", classClassWorkDTO);
		if (classClassWorkDTO.getId() != null) {
			throw new BadRequestAlertException("A new classClassWork cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (classClassWorkDTO.getChapterSection() == null) {
			throw new BadRequestAlertException("Missing details to create a classhomework", ENTITY_NAME,
					"missing details");
		}
		classClassWorkDTO.setCreateDate(LocalDate.now());
		ClassClassWorkDTO result = classClassWorkService.save(classClassWorkDTO);
		return ResponseEntity
				.created(new URI("/api/class-class-works/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-class-works/:id} : Updates an existing classClassWork.
	 *
	 * @param id                the id of the classClassWorkDTO to save.
	 * @param classClassWorkDTO the classClassWorkDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classClassWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classClassWorkDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         classClassWorkDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-class-works/{id}")
	public ResponseEntity<ClassClassWorkDTO> updateClassClassWork(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassClassWorkDTO classClassWorkDTO) throws URISyntaxException {
		log.debug("REST request to update ClassClassWork : {}, {}", id, classClassWorkDTO);
		if (classClassWorkDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classClassWorkDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classClassWorkRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classClassWorkDTO.setLastModified(LocalDate.now());
		ClassClassWorkDTO result = classClassWorkService.save(classClassWorkDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classClassWorkDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /class-class-works/:id} : Partial updates given fields of an
	 * existing classClassWork, field will ignore if it is null
	 *
	 * @param id                the id of the classClassWorkDTO to save.
	 * @param classClassWorkDTO the classClassWorkDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classClassWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classClassWorkDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the classClassWorkDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         classClassWorkDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-class-works/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassClassWorkDTO> partialUpdateClassClassWork(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassClassWorkDTO classClassWorkDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassClassWork partially : {}, {}", id, classClassWorkDTO);
		if (classClassWorkDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classClassWorkDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classClassWorkRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classClassWorkDTO.setLastModified(LocalDate.now());
		Optional<ClassClassWorkDTO> result = classClassWorkService.partialUpdate(classClassWorkDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classClassWorkDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-class-works} : get all the classClassWorks.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classClassWorks in body.
	 */
	@GetMapping("/class-class-works")
	public ResponseEntity<List<ClassClassWork>> getAllClassHomeWorks(@RequestParam Long classId,
			@RequestParam(required = false) Long subjectId, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		// Get all the sections for the subject passed or all the sections for all
		// chapters of a subjects in a class

		// get all the homework for all the sections
		List<Long> sectionIds = chapterSectionService.getAllSectionsForAclassAndSubject(classId, subjectId);

		ClassClassWorkCriteria classClassWorkCriteria = new ClassClassWorkCriteria();
		LongFilter sectionIdsFilter = new LongFilter();
		sectionIdsFilter.setIn(sectionIds);
		classClassWorkCriteria.setChapterSectionId(sectionIdsFilter);

		LocalDateFilter classClassWorkDateFilter = new LocalDateFilter();
		classClassWorkDateFilter.setGreaterThanOrEqual(startDate);
		classClassWorkDateFilter.setLessThanOrEqual(endDate);
		classClassWorkCriteria.setSchoolDate(classClassWorkDateFilter);

		List<ClassClassWork> sectionClassWorks = classClassWorkQueryService.findByCriteria(classClassWorkCriteria);

		return ResponseEntity.ok().body(sectionClassWorks);
	}

	/**
	 * {@code GET  /class-class-works/count} : count all the classClassWorks.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-class-works/count")
	public ResponseEntity<Long> countClassClassWorks(ClassClassWorkCriteria criteria) {
		log.debug("REST request to count ClassClassWorks by criteria: {}", criteria);
		return ResponseEntity.ok().body(classClassWorkQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-class-works/:id} : get the "id" classClassWork.
	 *
	 * @param id the id of the classClassWorkDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classClassWorkDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-class-works/{id}")
	public ResponseEntity<ClassClassWork> getClassClassWork(@PathVariable Long id) {
		log.debug("REST request to get ClassClassWork : {}", id);
		Optional<ClassClassWork> classClassWork = classClassWorkService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classClassWork);
	}

	/**
	 * {@code DELETE  /class-class-works/:id} : delete the "id" classClassWork.
	 *
	 * @param id the id of the classClassWorkDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-class-works/{id}")
	public ResponseEntity<Void> deleteClassClassWork(@PathVariable Long id) {
		log.debug("REST request to delete ClassClassWork : {}", id);
		classClassWorkService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
