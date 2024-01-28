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
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.repository.ChapterSectionRepository;
import com.ssik.manageit.repository.SubjectChapterRepository;
import com.ssik.manageit.service.SubjectChapterQueryService;
import com.ssik.manageit.service.SubjectChapterService;
import com.ssik.manageit.service.criteria.SubjectChapterCriteria;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import com.ssik.manageit.service.mapper.SubjectChapterMapper;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SubjectChapter}.
 */
@RestController
@RequestMapping("/api")
public class SubjectChapterResource {

	private final Logger log = LoggerFactory.getLogger(SubjectChapterResource.class);

	private static final String ENTITY_NAME = "subjectChapter";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SubjectChapterService subjectChapterService;

	private final SubjectChapterRepository subjectChapterRepository;

	private final SubjectChapterQueryService subjectChapterQueryService;

	@Autowired
	private ChapterSectionRepository chapterSectionRepo;

	@Autowired
	private SubjectChapterMapper subjectChapterMapper;

	public SubjectChapterResource(SubjectChapterService subjectChapterService,
			SubjectChapterRepository subjectChapterRepository, SubjectChapterQueryService subjectChapterQueryService) {
		this.subjectChapterService = subjectChapterService;
		this.subjectChapterRepository = subjectChapterRepository;
		this.subjectChapterQueryService = subjectChapterQueryService;
	}

	/**
	 * {@code POST  /subject-chapters} : Create a new subjectChapter.
	 *
	 * @param subjectChapterDTO the subjectChapterDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new subjectChapterDTO, or with status
	 *         {@code 400 (Bad Request)} if the subjectChapter has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/subject-chapters")
	public ResponseEntity<SubjectChapterDTO> createSubjectChapter(
			@Valid @RequestBody SubjectChapterDTO subjectChapterDTO) throws URISyntaxException {
		log.debug("REST request to save SubjectChapter : {}", subjectChapterDTO);
		if (subjectChapterDTO.getId() != null) {
			throw new BadRequestAlertException("A new subjectChapter cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		List<SubjectChapter> chapterWithSameName = subjectChapterRepository
				.findByChapterName(subjectChapterDTO.getChapterName().trim());
		if (chapterWithSameName != null && chapterWithSameName.size() > 0) {
			throw new BadRequestAlertException("A  Chapter cannot have same name", ENTITY_NAME, "duplicate name");
		}

		if (subjectChapterDTO.getClassSubject() == null) {
			throw new BadRequestAlertException("A  Chapter cannot be created without Subject", ENTITY_NAME,
					"subject missing");
		}
		subjectChapterDTO.setCreateDate(LocalDate.now());
		SubjectChapterDTO result = subjectChapterService.save(subjectChapterDTO);

		ChapterSection defaultSection = new ChapterSection();
		defaultSection.setSectionName("Sec- " + subjectChapterDTO.getChapterName());
		defaultSection.setSubjectChapter(subjectChapterMapper.toEntity(result));
		chapterSectionRepo.save(defaultSection);

		return ResponseEntity
				.created(new URI("/api/subject-chapters/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /subject-chapters/:id} : Updates an existing subjectChapter.
	 *
	 * @param id                the id of the subjectChapterDTO to save.
	 * @param subjectChapterDTO the subjectChapterDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated subjectChapterDTO, or with status
	 *         {@code 400 (Bad Request)} if the subjectChapterDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         subjectChapterDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/subject-chapters/{id}")
	public ResponseEntity<SubjectChapterDTO> updateSubjectChapter(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SubjectChapterDTO subjectChapterDTO) throws URISyntaxException {
		log.debug("REST request to update SubjectChapter : {}, {}", id, subjectChapterDTO);
		if (subjectChapterDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, subjectChapterDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!subjectChapterRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		subjectChapterDTO.setLastModified(LocalDate.now());
		SubjectChapterDTO result = subjectChapterService.save(subjectChapterDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				subjectChapterDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /subject-chapters/:id} : Partial updates given fields of an
	 * existing subjectChapter, field will ignore if it is null
	 *
	 * @param id                the id of the subjectChapterDTO to save.
	 * @param subjectChapterDTO the subjectChapterDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated subjectChapterDTO, or with status
	 *         {@code 400 (Bad Request)} if the subjectChapterDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the subjectChapterDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         subjectChapterDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/subject-chapters/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SubjectChapterDTO> partialUpdateSubjectChapter(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SubjectChapterDTO subjectChapterDTO) throws URISyntaxException {
		log.debug("REST request to partial update SubjectChapter partially : {}, {}", id, subjectChapterDTO);
		if (subjectChapterDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, subjectChapterDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!subjectChapterRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		subjectChapterDTO.setLastModified(LocalDate.now());
		Optional<SubjectChapterDTO> result = subjectChapterService.partialUpdate(subjectChapterDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, subjectChapterDTO.getId().toString()));
	}

	/**
	 * {@code GET  /subject-chapters} : get all the subjectChapters.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of subjectChapters in body.
	 */
	@GetMapping("/subject-chapters")
	public ResponseEntity<List<SubjectChapter>> getAllSubjectChapters(SubjectChapterCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get SubjectChapters by criteria: {}", criteria);
		Page<SubjectChapter> page = subjectChapterQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /subject-chapters/count} : count all the subjectChapters.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/subject-chapters/count")
	public ResponseEntity<Long> countSubjectChapters(SubjectChapterCriteria criteria) {
		log.debug("REST request to count SubjectChapters by criteria: {}", criteria);
		return ResponseEntity.ok().body(subjectChapterQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /subject-chapters/:id} : get the "id" subjectChapter.
	 *
	 * @param id the id of the subjectChapterDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the subjectChapterDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/subject-chapters/{id}")
	public ResponseEntity<SubjectChapter> getSubjectChapter(@PathVariable Long id) {
		log.debug("REST request to get SubjectChapter : {}", id);
		Optional<SubjectChapter> subjectChapter = subjectChapterService.findOne(id);
		return ResponseUtil.wrapOrNotFound(subjectChapter);
	}

	/**
	 * {@code DELETE  /subject-chapters/:id} : delete the "id" subjectChapter.
	 *
	 * @param id the id of the subjectChapterDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/subject-chapters/{id}")
	public ResponseEntity<Void> deleteSubjectChapter(@PathVariable Long id) {
		log.debug("REST request to delete SubjectChapter : {}", id);
		subjectChapterService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
