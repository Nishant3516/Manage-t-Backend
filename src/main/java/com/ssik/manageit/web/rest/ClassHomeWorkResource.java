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

import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.repository.ClassHomeWorkRepository;
import com.ssik.manageit.service.ChapterSectionService;
import com.ssik.manageit.service.ClassHomeWorkQueryService;
import com.ssik.manageit.service.ClassHomeWorkService;
import com.ssik.manageit.service.criteria.ClassHomeWorkCriteria;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ClassHomeWork}.
 */
@RestController
@RequestMapping("/api")
public class ClassHomeWorkResource {

	private final Logger log = LoggerFactory.getLogger(ClassHomeWorkResource.class);

	private static final String ENTITY_NAME = "classHomeWork";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassHomeWorkService classHomeWorkService;

	private final ClassHomeWorkRepository classHomeWorkRepository;

	private final ClassHomeWorkQueryService classHomeWorkQueryService;
	private final ChapterSectionService chapterSectionService;

	public ClassHomeWorkResource(ClassHomeWorkService classHomeWorkService,
			ClassHomeWorkRepository classHomeWorkRepository, ClassHomeWorkQueryService classHomeWorkQueryService,
			ChapterSectionService chapterSectionService) {
		this.classHomeWorkService = classHomeWorkService;
		this.classHomeWorkRepository = classHomeWorkRepository;
		this.classHomeWorkQueryService = classHomeWorkQueryService;
		this.chapterSectionService = chapterSectionService;
	}

	/**
	 * {@code POST  /class-home-works} : Create a new classHomeWork.
	 *
	 * @param classHomeWorkDTO the classHomeWorkDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classHomeWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classHomeWork has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-home-works")
	public ResponseEntity<ClassHomeWorkDTO> createClassHomeWork(@Valid @RequestBody ClassHomeWorkDTO classHomeWorkDTO)
			throws URISyntaxException {
		log.debug("REST request to save ClassHomeWork : {}", classHomeWorkDTO);
		if (classHomeWorkDTO.getId() != null) {
			throw new BadRequestAlertException("A new classHomeWork cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (classHomeWorkDTO.getChapterSection() == null) {
			throw new BadRequestAlertException("Missing details to create a classhomework", ENTITY_NAME,
					"missing details");
		}
		classHomeWorkDTO.setCreateDate(LocalDate.now());
		ClassHomeWorkDTO result = classHomeWorkService.save(classHomeWorkDTO);
		return ResponseEntity
				.created(new URI("/api/class-home-works/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /class-home-works/:id} : Updates an existing classHomeWork.
	 *
	 * @param id               the id of the classHomeWorkDTO to save.
	 * @param classHomeWorkDTO the classHomeWorkDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classHomeWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classHomeWorkDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         classHomeWorkDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-home-works/{id}")
	public ResponseEntity<ClassHomeWorkDTO> updateClassHomeWork(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassHomeWorkDTO classHomeWorkDTO) throws URISyntaxException {
		log.debug("REST request to update ClassHomeWork : {}, {}", id, classHomeWorkDTO);
		if (classHomeWorkDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classHomeWorkDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classHomeWorkRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classHomeWorkDTO.setLastModified(LocalDate.now());
		ClassHomeWorkDTO result = classHomeWorkService.save(classHomeWorkDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classHomeWorkDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /class-home-works/:id} : Partial updates given fields of an
	 * existing classHomeWork, field will ignore if it is null
	 *
	 * @param id               the id of the classHomeWorkDTO to save.
	 * @param classHomeWorkDTO the classHomeWorkDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classHomeWorkDTO, or with status
	 *         {@code 400 (Bad Request)} if the classHomeWorkDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the classHomeWorkDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         classHomeWorkDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-home-works/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassHomeWorkDTO> partialUpdateClassHomeWork(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassHomeWorkDTO classHomeWorkDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassHomeWork partially : {}, {}", id, classHomeWorkDTO);
		if (classHomeWorkDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classHomeWorkDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classHomeWorkRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classHomeWorkDTO.setLastModified(LocalDate.now());
		Optional<ClassHomeWorkDTO> result = classHomeWorkService.partialUpdate(classHomeWorkDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classHomeWorkDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-home-works} : get all the classHomeWorks.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classHomeWorks in body.
	 */
	@GetMapping("/class-home-works")
	public ResponseEntity<List<ClassHomeWork>> getAllClassHomeWorks(@RequestParam Long classId,
			@RequestParam(required = false) Long subjectId, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		// Get all the subjects for a given class
//    	ClassSubjectCriteria classSubjectCriteria = new ClassSubjectCriteria();
//    	LongFilter classIdFilter=new LongFilter();
//    	classIdFilter.setEquals(classId);
//    	classSubjectCriteria.setSchoolClassId(classIdFilter);
//    	List<ClassSubjectDTO> classSubjects=classSubjectQueryService.findByCriteria(classSubjectCriteria);
//    	
//    	//collect all the subject ID
//    	List<Long>subjectIds=new ArrayList<Long>();
//    	if(subjectId!=null) {
//        	subjectIds=classSubjects.stream().filter(cs->cs.getId().equals(subjectId)).map(ClassSubjectDTO::getId).collect(Collectors.toList());
//    		
//    	}else {
//    		//no subject Id has been passed so get all the list of subject taught in the class
//        	subjectIds=classSubjects.stream().map(ClassSubjectDTO::getId).collect(Collectors.toList());
//    		
//    	}
//    	
//    	//get all the chapters for a given subject if subject ID has been passed
//    	SubjectChapterCriteria subjectChapterCriteria=new SubjectChapterCriteria();
//    	LongFilter classSubjectFilter =new LongFilter();
//    	classSubjectFilter.setIn(subjectIds);
//    	subjectChapterCriteria.setClassSubjectId(classSubjectFilter);
//    	List<SubjectChapterDTO> subjectChapters=subjectChapterQueryService.findByCriteria(subjectChapterCriteria);
//    	List<Long> chapterIds=subjectChapters.stream().map(SubjectChapterDTO::getId).collect(Collectors.toList());
//    	
//    	ChapterSectionCriteria chapterSectionCriteria = new ChapterSectionCriteria();
//    	LongFilter chapterSectionFilter =new LongFilter();
//    	chapterSectionFilter.setIn(chapterIds);
//    	chapterSectionCriteria.setSubjectChapterId(chapterSectionFilter);
//    	List<ChapterSectionDTO> chapterSections =chapterSectionQueryService.findByCriteria(chapterSectionCriteria);
//    	List<Long> sectionIds=chapterSections.stream().map(ChapterSectionDTO::getId).collect(Collectors.toList());

		// Get all the sections for the subject passed or all the sections for all
		// chapters of a subjects in a class

		// get all the homework for all the sections
		List<Long> sectionIds = chapterSectionService.getAllSectionsForAclassAndSubject(classId, subjectId);

		ClassHomeWorkCriteria classHomeWorkCriteria = new ClassHomeWorkCriteria();
		LongFilter sectionIdsFilter = new LongFilter();
		sectionIdsFilter.setIn(sectionIds);
		classHomeWorkCriteria.setChapterSectionId(sectionIdsFilter);

		LocalDateFilter classHomeWorkDateFilter = new LocalDateFilter();
		classHomeWorkDateFilter.setGreaterThanOrEqual(startDate);
		classHomeWorkDateFilter.setLessThanOrEqual(endDate);
		classHomeWorkCriteria.setSchoolDate(classHomeWorkDateFilter);

		List<ClassHomeWork> sectionHomeWorks = classHomeWorkQueryService.findByCriteria(classHomeWorkCriteria);

		return ResponseEntity.ok().body(sectionHomeWorks);
	}

	/**
	 * {@code GET  /class-home-works/count} : count all the classHomeWorks.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-home-works/count")
	public ResponseEntity<Long> countClassHomeWorks(ClassHomeWorkCriteria criteria) {
		log.debug("REST request to count ClassHomeWorks by criteria: {}", criteria);
		return ResponseEntity.ok().body(classHomeWorkQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-home-works/:id} : get the "id" classHomeWork.
	 *
	 * @param id the id of the classHomeWorkDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classHomeWorkDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-home-works/{id}")
	public ResponseEntity<ClassHomeWork> getClassHomeWork(@PathVariable Long id) {
		log.debug("REST request to get ClassHomeWork : {}", id);
		Optional<ClassHomeWork> classHomeWork = classHomeWorkService.findOne(id);
		return ResponseUtil.wrapOrNotFound(classHomeWork);
	}

	/**
	 * {@code DELETE  /class-home-works/:id} : delete the "id" classHomeWork.
	 *
	 * @param id the id of the classHomeWorkDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-home-works/{id}")
	public ResponseEntity<Void> deleteClassHomeWork(@PathVariable Long id) {
		log.debug("REST request to delete ClassHomeWork : {}", id);
		classHomeWorkService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
