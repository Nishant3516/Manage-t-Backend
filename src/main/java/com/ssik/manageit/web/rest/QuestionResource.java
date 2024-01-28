package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.repository.QuestionRepository;
import com.ssik.manageit.repository.QuestionTypeRepository;
import com.ssik.manageit.service.PdfService;
import com.ssik.manageit.service.QuestionPaperService;
import com.ssik.manageit.service.QuestionQueryService;
import com.ssik.manageit.service.QuestionService;
import com.ssik.manageit.service.WordDocumentService;
import com.ssik.manageit.service.criteria.QuestionCriteria;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

	private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

	private static final String ENTITY_NAME = "question";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final QuestionService questionService;

	private final QuestionRepository questionRepository;

	private final QuestionQueryService questionQueryService;

	@Autowired
	private QuestionTypeRepository questionTypeRepository;

	@Autowired
	private WordDocumentService wordDocumentService;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private QuestionPaperService questionPaperService;

	public QuestionResource(QuestionService questionService, QuestionRepository questionRepository,
			QuestionQueryService questionQueryService) {
		this.questionService = questionService;
		this.questionRepository = questionRepository;
		this.questionQueryService = questionQueryService;
	}

	/**
	 * {@code POST  /questions} : Create a new question.
	 *
	 * @param question the question to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new question, or with status {@code 400 (Bad Request)} if
	 *         the question has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/questions")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question) throws URISyntaxException {
		log.debug("REST request to save Question : {}", question);
		if (question.getId() != null) {
			throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
		}
		QuestionType questionType = null;
		if (question.getQuestionType() == null) {
			questionType = questionTypeRepository.findByQuestionType("NotCategorized");
		} else {
			questionType = question.getQuestionType();
		}
		Question result = new Question();
		List<String> questionsFromFile = new ArrayList<String>();
		if (question.getQuestionImportFileContentType() != null) {
			if (question.getQuestionImportFileContentType().endsWith("pdf")) {
				questionsFromFile = pdfService.readFromPdf(question.getQuestionImportFile());
			} else {
				questionsFromFile = wordDocumentService.processWordDocument(question.getQuestionImportFile());
			}
			if (!questionsFromFile.isEmpty()) {
				for (String questionText : questionsFromFile) {
					Question questionToSave = new Question();
					questionToSave.setQuestionText(questionText);
					questionToSave.setQuestionType(questionType);
					questionToSave.setCreateDate(LocalDate.now());
					questionToSave.setSchoolClass(question.getSchoolClass());
					questionToSave.setClassSubject(question.getClassSubject());
					questionToSave.setSubjectChapter(question.getSubjectChapter());
					result = questionService.save(questionToSave);
				}
			}
		} else {

			result = questionService.save(question);
		}

		return ResponseEntity
				.created(new URI("/api/questions/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /questions/:id} : Updates an existing question.
	 *
	 * @param id       the id of the question to save.
	 * @param question the question to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated question, or with status {@code 400 (Bad Request)} if the
	 *         question is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the question couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/questions/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody Question question) throws URISyntaxException {
		log.debug("REST request to update Question : {}, {}", id, question);
		if (question.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, question.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		Optional<Question> questionRepoOpt = questionRepository.findById(question.getId());
		if (questionRepoOpt.isPresent()) {
			Question questionInRepo = questionRepoOpt.get();
			System.out.println(questionInRepo.getQuestionPapers() == null ? "NULL" : "NOT NULLS");
		}
		// Question result = questionService.save(question);
		// Really really bad hack .. but if this works this is good for now rather
		// banging head for
		// getting the m:n to work here!!
		// Weightage will be not null only if the question has been added to some
		// question paper, for a new question
		// this is going to be null.. this is a hack, the weightage will contain
		// question paper ID so that it can be edited as well
		if (question.getWeightage() != null) {
			@SuppressWarnings("deprecation")
			Long questionPaperId = new Long(question.getWeightage());
			Optional<QuestionPaper> questionPaperOpt = questionPaperService
					.findOne(questionPaperId < 0 ? Math.abs(questionPaperId) : questionPaperId);
			if (questionPaperOpt.isPresent()) {
				QuestionPaper questionPaper = questionPaperOpt.get();
				if (questionPaperId > 0) {
					// Set<QuestionPaper> questionPapers = new HashSet<>();
					// questionPapers.add(questionPaper);
					// result.setQuestionPapers(questionPapers);
					question.addQuestionPaper(questionPaper);
				} else {
					question.removeQuestionPaper(questionPaper);
				}
				// Question paper is the parent relation to the many to many
				// so parent must be saved
				questionPaperService.save(questionPaper);
				// result.addQuestionPaper(questionPaperOpt.get());
			}
		}
		Question result = questionService.save(question);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, question.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /questions/:id} : Partial updates given fields of an existing
	 * question, field will ignore if it is null
	 *
	 * @param id       the id of the question to save.
	 * @param question the question to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated question, or with status {@code 400 (Bad Request)} if the
	 *         question is not valid, or with status {@code 404 (Not Found)} if the
	 *         question is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the question couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Question> partialUpdateQuestion(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody Question question) throws URISyntaxException {
		log.debug("REST request to partial update Question partially : {}, {}", id, question);
		if (question.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, question.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<Question> result = questionService.partialUpdate(question);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, question.getId().toString()));
	}

	/**
	 * {@code GET  /questions} : get all the questions.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of questions in body.
	 */
	@GetMapping("/questions")
	public ResponseEntity<List<Question>> getAllQuestions(QuestionCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Questions by criteria: {}", criteria);
		Page<Question> page = questionQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /questions/count} : count all the questions.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/questions/count")
	public ResponseEntity<Long> countQuestions(QuestionCriteria criteria) {
		log.debug("REST request to count Questions by criteria: {}", criteria);
		return ResponseEntity.ok().body(questionQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /questions/:id} : get the "id" question.
	 *
	 * @param id the id of the question to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the question, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/questions/{id}")
	public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
		log.debug("REST request to get Question : {}", id);
		Optional<Question> question = questionService.findOne(id);
		return ResponseUtil.wrapOrNotFound(question);
	}

	/**
	 * {@code DELETE  /questions/:id} : delete the "id" question.
	 *
	 * @param id the id of the question to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/questions/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		log.debug("REST request to delete Question : {}", id);
		Optional<Question> question = questionService.findOne(id);
		if (question.isPresent()) {
			// Remove question from the question paper
			Question foundQuestion = question.get();
			if (foundQuestion.getQuestionPapers() != null) {
				for (QuestionPaper qPaper : foundQuestion.getQuestionPapers()) {
					@SuppressWarnings("deprecation")
					Long questionPaperId = new Long(qPaper.getId());
					Optional<QuestionPaper> questionPaperOpt = questionPaperService
							.findOne(questionPaperId < 0 ? Math.abs(questionPaperId) : questionPaperId);
					if (questionPaperOpt.isPresent()) {
						QuestionPaper questionPaperFound = questionPaperOpt.get();

						foundQuestion.removeQuestionPaper(questionPaperFound);

						// Question paper is the parent relation to the many to many
						// so parent must be saved
						questionPaperService.save(questionPaperFound);
						// result.addQuestionPaper(questionPaperOpt.get());
					}

				}
//				foundQuestion.removeQuestionPaper(qPaper);
//				questionPaperService.save(qPaper);

			}
			questionService.delete(id);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
