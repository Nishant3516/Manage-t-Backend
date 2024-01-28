package com.ssik.manageit.web.rest;

import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.repository.QuestionTypeRepository;
import com.ssik.manageit.service.QuestionTypeQueryService;
import com.ssik.manageit.service.QuestionTypeService;
import com.ssik.manageit.service.criteria.QuestionTypeCriteria;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.QuestionType}.
 */
@RestController
@RequestMapping("/api")
public class QuestionTypeResource {

	private final Logger log = LoggerFactory.getLogger(QuestionTypeResource.class);

	private static final String ENTITY_NAME = "questionType";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final QuestionTypeService questionTypeService;

	private final QuestionTypeRepository questionTypeRepository;

	private final QuestionTypeQueryService questionTypeQueryService;

	public QuestionTypeResource(QuestionTypeService questionTypeService, QuestionTypeRepository questionTypeRepository,
			QuestionTypeQueryService questionTypeQueryService) {
		this.questionTypeService = questionTypeService;
		this.questionTypeRepository = questionTypeRepository;
		this.questionTypeQueryService = questionTypeQueryService;
	}

	/**
	 * {@code POST  /question-types} : Create a new questionType.
	 *
	 * @param questionType the questionType to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new questionType, or with status {@code 400 (Bad Request)}
	 *         if the questionType has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/question-types")
	public ResponseEntity<QuestionType> createQuestionType(@RequestBody QuestionType questionType)
			throws URISyntaxException {
		log.debug("REST request to save QuestionType : {}", questionType);
		if (questionType.getId() != null) {
			throw new BadRequestAlertException("A new questionType cannot already have an ID", ENTITY_NAME, "idexists");
		}
		QuestionType result = questionTypeService.save(questionType);
		return ResponseEntity
				.created(new URI("/api/question-types/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /question-types/:id} : Updates an existing questionType.
	 *
	 * @param id           the id of the questionType to save.
	 * @param questionType the questionType to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated questionType, or with status {@code 400 (Bad Request)} if
	 *         the questionType is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the questionType couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/question-types/{id}")
	public ResponseEntity<QuestionType> updateQuestionType(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody QuestionType questionType) throws URISyntaxException {
		log.debug("REST request to update QuestionType : {}, {}", id, questionType);
		if (questionType.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, questionType.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionTypeRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		QuestionType result = questionTypeService.save(questionType);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionType.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /question-types/:id} : Partial updates given fields of an
	 * existing questionType, field will ignore if it is null
	 *
	 * @param id           the id of the questionType to save.
	 * @param questionType the questionType to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated questionType, or with status {@code 400 (Bad Request)} if
	 *         the questionType is not valid, or with status {@code 404 (Not Found)}
	 *         if the questionType is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the questionType couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/question-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<QuestionType> partialUpdateQuestionType(
			@PathVariable(value = "id", required = false) final Long id, @RequestBody QuestionType questionType)
			throws URISyntaxException {
		log.debug("REST request to partial update QuestionType partially : {}, {}", id, questionType);
		if (questionType.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, questionType.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionTypeRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<QuestionType> result = questionTypeService.partialUpdate(questionType);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, questionType.getId().toString()));
	}

	/**
	 * {@code GET  /question-types} : get all the questionTypes.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of questionTypes in body.
	 */
	@GetMapping("/question-types")
	public ResponseEntity<List<QuestionType>> getAllQuestionTypes(QuestionTypeCriteria criteria, Pageable pageable) {
		log.debug("REST request to get QuestionTypes by criteria: {}", criteria);
		Page<QuestionType> page = questionTypeQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /question-types/count} : count all the questionTypes.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/question-types/count")
	public ResponseEntity<Long> countQuestionTypes(QuestionTypeCriteria criteria) {
		log.debug("REST request to count QuestionTypes by criteria: {}", criteria);
		return ResponseEntity.ok().body(questionTypeQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /question-types/:id} : get the "id" questionType.
	 *
	 * @param id the id of the questionType to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the questionType, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/question-types/{id}")
	public ResponseEntity<QuestionType> getQuestionType(@PathVariable Long id) {
		log.debug("REST request to get QuestionType : {}", id);
		Optional<QuestionType> questionType = questionTypeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(questionType);
	}

	/**
	 * {@code DELETE  /question-types/:id} : delete the "id" questionType.
	 *
	 * @param id the id of the questionType to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/question-types/{id}")
	public ResponseEntity<Void> deleteQuestionType(@PathVariable Long id) {
		log.debug("REST request to delete QuestionType : {}", id);
		questionTypeService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
