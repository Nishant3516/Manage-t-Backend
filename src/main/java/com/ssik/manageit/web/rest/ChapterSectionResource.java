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
import com.ssik.manageit.repository.ChapterSectionRepository;
import com.ssik.manageit.service.ChapterSectionQueryService;
import com.ssik.manageit.service.ChapterSectionService;
import com.ssik.manageit.service.criteria.ChapterSectionCriteria;
import com.ssik.manageit.service.dto.ChapterSectionDTO;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ChapterSection}.
 */
@RestController
@RequestMapping("/api")
public class ChapterSectionResource {

	private final Logger log = LoggerFactory.getLogger(ChapterSectionResource.class);

	private static final String ENTITY_NAME = "chapterSection";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ChapterSectionService chapterSectionService;

	private final ChapterSectionRepository chapterSectionRepository;

	private final ChapterSectionQueryService chapterSectionQueryService;

	public ChapterSectionResource(ChapterSectionService chapterSectionService,
			ChapterSectionRepository chapterSectionRepository, ChapterSectionQueryService chapterSectionQueryService) {
		this.chapterSectionService = chapterSectionService;
		this.chapterSectionRepository = chapterSectionRepository;
		this.chapterSectionQueryService = chapterSectionQueryService;
	}

	/**
	 * {@code POST  /chapter-sections} : Create a new chapterSection.
	 *
	 * @param chapterSectionDTO the chapterSectionDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new chapterSectionDTO, or with status
	 *         {@code 400 (Bad Request)} if the chapterSection has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/chapter-sections")
	public ResponseEntity<ChapterSectionDTO> createChapterSection(
			@Valid @RequestBody ChapterSectionDTO chapterSectionDTO) throws URISyntaxException {
		log.debug("REST request to save ChapterSection : {}", chapterSectionDTO);
		if (chapterSectionDTO.getId() != null) {
			throw new BadRequestAlertException("A new chapterSection cannot already have an ID", ENTITY_NAME,
					"idexists");
		}

		List<ChapterSection> sectionWithSameName = chapterSectionRepository
				.findBySectionName(chapterSectionDTO.getSectionName().trim());
		if (sectionWithSameName != null && sectionWithSameName.size() > 0) {
			throw new BadRequestAlertException("A  Section cannot have same name", ENTITY_NAME, "duplicate name");
		}

		if (chapterSectionDTO.getSubjectChapter() == null) {
			throw new BadRequestAlertException("A  Section cannot be created without chapter", ENTITY_NAME,
					"chapter missing");
		}
		chapterSectionDTO.setCreateDate(LocalDate.now());
		ChapterSectionDTO result = chapterSectionService.save(chapterSectionDTO);
		return ResponseEntity
				.created(new URI("/api/chapter-sections/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /chapter-sections/:id} : Updates an existing chapterSection.
	 *
	 * @param id                the id of the chapterSectionDTO to save.
	 * @param chapterSectionDTO the chapterSectionDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated chapterSectionDTO, or with status
	 *         {@code 400 (Bad Request)} if the chapterSectionDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         chapterSectionDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/chapter-sections/{id}")
	public ResponseEntity<ChapterSectionDTO> updateChapterSection(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ChapterSectionDTO chapterSectionDTO) throws URISyntaxException {
		log.debug("REST request to update ChapterSection : {}, {}", id, chapterSectionDTO);
		if (chapterSectionDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, chapterSectionDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!chapterSectionRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		chapterSectionDTO.setLastModified(LocalDate.now());
		ChapterSectionDTO result = chapterSectionService.save(chapterSectionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				chapterSectionDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /chapter-sections/:id} : Partial updates given fields of an
	 * existing chapterSection, field will ignore if it is null
	 *
	 * @param id                the id of the chapterSectionDTO to save.
	 * @param chapterSectionDTO the chapterSectionDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated chapterSectionDTO, or with status
	 *         {@code 400 (Bad Request)} if the chapterSectionDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the chapterSectionDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         chapterSectionDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/chapter-sections/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ChapterSectionDTO> partialUpdateChapterSection(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ChapterSectionDTO chapterSectionDTO) throws URISyntaxException {
		log.debug("REST request to partial update ChapterSection partially : {}, {}", id, chapterSectionDTO);
		if (chapterSectionDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, chapterSectionDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!chapterSectionRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		chapterSectionDTO.setLastModified(LocalDate.now());
		Optional<ChapterSectionDTO> result = chapterSectionService.partialUpdate(chapterSectionDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, chapterSectionDTO.getId().toString()));
	}

	/**
	 * {@code GET  /chapter-sections} : get all the chapterSections.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of chapterSections in body.
	 */
	@GetMapping("/chapter-sections")
	public ResponseEntity<List<ChapterSection>> getAllChapterSections(ChapterSectionCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get ChapterSections by criteria: {}", criteria);
		Page<ChapterSection> page = chapterSectionQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /chapter-sections/count} : count all the chapterSections.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/chapter-sections/count")
	public ResponseEntity<Long> countChapterSections(ChapterSectionCriteria criteria) {
		log.debug("REST request to count ChapterSections by criteria: {}", criteria);
		return ResponseEntity.ok().body(chapterSectionQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /chapter-sections/:id} : get the "id" chapterSection.
	 *
	 * @param id the id of the chapterSectionDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the chapterSectionDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/chapter-sections/{id}")
	public ResponseEntity<ChapterSection> getChapterSection(@PathVariable Long id) {
		log.debug("REST request to get ChapterSection : {}", id);
		Optional<ChapterSection> chapterSectionDTO = chapterSectionService.findOne(id);
		return ResponseUtil.wrapOrNotFound(chapterSectionDTO);
	}

	/**
	 * {@code DELETE  /chapter-sections/:id} : delete the "id" chapterSection.
	 *
	 * @param id the id of the chapterSectionDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/chapter-sections/{id}")
	public ResponseEntity<Void> deleteChapterSection(@PathVariable Long id) {
		log.debug("REST request to delete ChapterSection : {}", id);
		chapterSectionService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
