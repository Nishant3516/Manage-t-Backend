package com.ssik.manageit.web.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.repository.QuestionPaperRepository;
import com.ssik.manageit.service.PdfService;
import com.ssik.manageit.service.QuestionPaperQueryService;
import com.ssik.manageit.service.QuestionPaperService;
import com.ssik.manageit.service.WordDocumentService;
import com.ssik.manageit.service.criteria.QuestionPaperCriteria;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.QuestionPaper}.
 */
@RestController
@RequestMapping("/api")
public class QuestionPaperResource {

	private final Logger log = LoggerFactory.getLogger(QuestionPaperResource.class);

	private static final String ENTITY_NAME = "questionPaper";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final QuestionPaperService questionPaperService;

	private final QuestionPaperRepository questionPaperRepository;

	private final QuestionPaperQueryService questionPaperQueryService;

	@Autowired
	private WordDocumentService wordDocumentService;
	@Autowired
	private PdfService pdfService;

	public QuestionPaperResource(QuestionPaperService questionPaperService,
			QuestionPaperRepository questionPaperRepository, QuestionPaperQueryService questionPaperQueryService) {
		this.questionPaperService = questionPaperService;
		this.questionPaperRepository = questionPaperRepository;
		this.questionPaperQueryService = questionPaperQueryService;
	}

	/**
	 * {@code POST  /question-papers} : Create a new questionPaper.
	 *
	 * @param questionPaper the questionPaper to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new questionPaper, or with status {@code 400 (Bad Request)}
	 *         if the questionPaper has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/question-papers")
	public ResponseEntity<QuestionPaper> createQuestionPaper(@RequestBody QuestionPaper questionPaper)
			throws URISyntaxException {
		log.debug("REST request to save QuestionPaper : {}", questionPaper);
		if (questionPaper.getId() != null) {
			throw new BadRequestAlertException("A new questionPaper cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		QuestionPaper result = questionPaperService.save(questionPaper);
		return ResponseEntity
				.created(new URI("/api/question-papers/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /question-papers/:id} : Updates an existing questionPaper.
	 *
	 * @param id            the id of the questionPaper to save.
	 * @param questionPaper the questionPaper to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated questionPaper, or with status {@code 400 (Bad Request)}
	 *         if the questionPaper is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the questionPaper couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/question-papers/{id}")
	public ResponseEntity<QuestionPaper> updateQuestionPaper(
			@PathVariable(value = "id", required = false) final Long id, @RequestBody QuestionPaper questionPaper)
			throws URISyntaxException {
		log.debug("REST request to update QuestionPaper : {}, {}", id, questionPaper);
		if (questionPaper.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, questionPaper.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionPaperRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		QuestionPaper result = questionPaperService.save(questionPaper);
		// There is no need for optional, if the question Paper does not exist, it will
		// not reach here
//		QuestionPaper qPaperWithQuestions = questionPaperService.findOne(id).get();
//		// result.setQuestionPaperFile(wordDocumentService.dumpQuestionPaperToWordDoc(qPaperWithQuestions));
//		pdfService.writeToPdf(qPaperWithQuestions);
//		String path = "/tmp/" + questionPaper.getId() + "_" + questionPaper.getQuestionPaperName() + ".pdf";
//		File file = new File(path);
//		byte[] fileContent = null;
//		try {
//			fileContent = Files.readAllBytes(file.toPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		result = questionPaperService.save(result);
//		if (fileContent != null) {
//			result.setQuestionPaperFile(fileContent);
//		}
		HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				questionPaper.getId().toString());
		headers.setContentType(MediaType.APPLICATION_PDF);
		// Here you have to set the actual filename of your pdf
		String filename = "output.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return ResponseEntity.ok().headers(headers).body(result);
	}

	/**
	 * {@code PATCH  /question-papers/:id} : Partial updates given fields of an
	 * existing questionPaper, field will ignore if it is null
	 *
	 * @param id            the id of the questionPaper to save.
	 * @param questionPaper the questionPaper to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated questionPaper, or with status {@code 400 (Bad Request)}
	 *         if the questionPaper is not valid, or with status
	 *         {@code 404 (Not Found)} if the questionPaper is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the questionPaper
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/question-papers/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<QuestionPaper> partialUpdateQuestionPaper(
			@PathVariable(value = "id", required = false) final Long id, @RequestBody QuestionPaper questionPaper)
			throws URISyntaxException {
		log.debug("REST request to partial update QuestionPaper partially : {}, {}", id, questionPaper);
		if (questionPaper.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, questionPaper.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!questionPaperRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<QuestionPaper> result = questionPaperService.partialUpdate(questionPaper);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, questionPaper.getId().toString()));
	}

	/**
	 * {@code GET  /question-papers} : get all the questionPapers.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of questionPapers in body.
	 */
	@GetMapping("/question-papers")
	public ResponseEntity<List<QuestionPaper>> getAllQuestionPapers(QuestionPaperCriteria criteria, Pageable pageable) {
		log.debug("REST request to get QuestionPapers by criteria: {}", criteria);
		Page<QuestionPaper> page = questionPaperQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /question-papers/count} : count all the questionPapers.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/question-papers/count")
	public ResponseEntity<Long> countQuestionPapers(QuestionPaperCriteria criteria) {
		log.debug("REST request to count QuestionPapers by criteria: {}", criteria);
		return ResponseEntity.ok().body(questionPaperQueryService.countByCriteria(criteria));
	}

	@GetMapping("/download/question-papers/{id}")
	public ResponseEntity<byte[]> qPaperForPreview(@PathVariable Long id, HttpServletResponse response) {

		Optional<QuestionPaper> questionPaper = questionPaperService.findOne(id);
		if (questionPaper.isPresent()) {
			QuestionPaper qPaperFound = getQPaperAsPDF(questionPaper);
			return ResponseEntity.ok().body(qPaperFound.getQuestionPaperFile());
		}
		return ResponseEntity.ok().body("Np Question Paper Found".getBytes());
	}

	/**
	 * {@code GET  /question-papers/:id} : get the "id" questionPaper.
	 *
	 * @param id the id of the questionPaper to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the questionPaper, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/question-papers/{id}")
	public ResponseEntity<QuestionPaper> getQuestionPaper(@PathVariable Long id, HttpServletResponse response) {
		log.debug("REST request to get QuestionPaper : {}", id);
		Optional<QuestionPaper> questionPaper = questionPaperService.findOne(id);
		if (questionPaper.isPresent()) {
			// QuestionPaper qPaperFound = getQPaperAsPDF(questionPaper);
			QuestionPaper qPaperFound = getQPaperAsWord(questionPaper);
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_PDF);
//			// Here you have to set the actual filename of your pdf
//			String filename = "output.pdf";
//			headers.setContentDispositionFormData(filename, filename);
			// headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

			return ResponseEntity.ok().body(qPaperFound);
		}
		return ResponseUtil.wrapOrNotFound(questionPaper);
	}

	private QuestionPaper getQPaperAsPDF(Optional<QuestionPaper> questionPaper) {
		QuestionPaper qPaperFound = questionPaper.get();
		pdfService.writeToPdf(qPaperFound);
		String path = "/tmp/" + qPaperFound.getId() + "_" + qPaperFound.getQuestionPaperName() + ".pdf";
		File file = new File(path);
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
			// streamReport(response, fileContent, "my_report.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileContent != null) {
			qPaperFound.setQuestionPaperFile(fileContent);
			qPaperFound.setQuestionPaperFileContentType("application/pdf");
		}
		return qPaperFound;
	}

	private QuestionPaper getQPaperAsWord(Optional<QuestionPaper> questionPaper) {
		QuestionPaper qPaperFound = questionPaper.get();
		wordDocumentService.dumpQuestionPaperToWordDoc(qPaperFound);
		String path = "/tmp/simple1.docx";// + qPaperFound.getId() + "_" + qPaperFound.getQuestionPaperName() + ".pdf";
		File file = new File(path);
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
			// streamReport(response, fileContent, "my_report.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileContent != null) {
			qPaperFound.setQuestionPaperFile(fileContent);
			qPaperFound.setQuestionPaperFileContentType(
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		}
		return qPaperFound;
	}

//	 protected void streamReport(HttpServletResponse response, byte[] data, String name)
//	            throws IOException {
//
//	        response.setContentType("application/pdf");
//	        response.setHeader("Content-disposition", "attachment; filename=" + name);
//	        response.setContentLength(data.length);
//
//	        response.getOutputStream().write(data);
//	        response.getOutputStream().flush();
//	    }
	/**
	 * {@code DELETE  /question-papers/:id} : delete the "id" questionPaper.
	 *
	 * @param id the id of the questionPaper to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/question-papers/{id}")
	public ResponseEntity<Void> deleteQuestionPaper(@PathVariable Long id) {
		log.debug("REST request to delete QuestionPaper : {}", id);
		questionPaperService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
