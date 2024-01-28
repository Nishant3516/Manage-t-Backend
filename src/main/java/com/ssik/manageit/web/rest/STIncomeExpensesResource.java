package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.repository.AuditLogRepository;
import com.ssik.manageit.repository.STIncomeExpensesRepository;
import com.ssik.manageit.service.STIncomeExpensesQueryService;
import com.ssik.manageit.service.STIncomeExpensesService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.criteria.STIncomeExpensesCriteria;
import com.ssik.manageit.util.IdStoreUtil;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.STIncomeExpenses}.
 */
@RestController
@RequestMapping("/api")
public class STIncomeExpensesResource {

	private final Logger log = LoggerFactory.getLogger(STIncomeExpensesResource.class);

	private static final String ENTITY_NAME = "sTIncomeExpenses";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final STIncomeExpensesService sTIncomeExpensesService;

	private final STIncomeExpensesRepository sTIncomeExpensesRepository;
	@Autowired
	IdStoreUtil idStoreUtil;
	@Autowired
	private SchoolCommonService schoolCommonService;
	@Autowired
	private AuditLogRepository auditLogRepository;

	private final STIncomeExpensesQueryService sTIncomeExpensesQueryService;

	public STIncomeExpensesResource(STIncomeExpensesService sTIncomeExpensesService,
			STIncomeExpensesRepository sTIncomeExpensesRepository,
			STIncomeExpensesQueryService sTIncomeExpensesQueryService) {
		this.sTIncomeExpensesService = sTIncomeExpensesService;
		this.sTIncomeExpensesRepository = sTIncomeExpensesRepository;
		this.sTIncomeExpensesQueryService = sTIncomeExpensesQueryService;
	}

	/**
	 * {@code POST  /st-income-expenses} : Create a new sTIncomeExpenses.
	 *
	 * @param sTIncomeExpenses the sTIncomeExpenses to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new sTIncomeExpenses, or with status
	 *         {@code 400 (Bad Request)} if the sTIncomeExpenses has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping("/st-income-expenses")
	public ResponseEntity<STIncomeExpenses> createSTIncomeExpenses(
			@Valid @RequestBody STIncomeExpenses sTIncomeExpenses) throws URISyntaxException {
		log.debug("REST request to save STIncomeExpenses : {}", sTIncomeExpenses);
		if (sTIncomeExpenses.getId() != null) {
			throw new BadRequestAlertException("A new sTIncomeExpenses cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		sTIncomeExpenses.setPaymentDate(LocalDate.now());
		if (sTIncomeExpenses.getModeOfPay() == null) {
			sTIncomeExpenses.setModeOfPay(ModeOfPayment.CASH);
		}
		sTIncomeExpenses.setCreateDate(LocalDate.now());
		if (isPaymentDuplicate(sTIncomeExpenses)) {
			throw new BadRequestAlertException("This Transport Charge is already there", ENTITY_NAME,
					"Transport Charge with same amount on same date for same Student And Route already exists");
		}
		sTIncomeExpenses.setReceiptId("" + idStoreUtil.getAndSaveNextId(IdType.RECEIPT));
		School school = schoolCommonService.getUserSchool();
		sTIncomeExpenses.setSchool(school);
		sTIncomeExpenses.setTenant(school.getTenant());
		STIncomeExpenses result = sTIncomeExpensesService.save(sTIncomeExpenses);
		// Add a audit log
		AuditLog auditLog = new AuditLog("ADD TRANSPORT FEE", LocalDate.now(), schoolCommonService.getLoggedinUser(),
				schoolCommonService.getUserSchool(), sTIncomeExpenses.toString(), null, ENTITY_NAME);
		auditLog.setCreateDate(LocalDate.now());
		auditLogRepository.save(auditLog);

		return ResponseEntity
				.created(new URI("/api/st-income-expenses/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	private Boolean isPaymentDuplicate(STIncomeExpenses stIncomeExpenses) {
		List<STIncomeExpenses> stIncomeExpensesFound = sTIncomeExpensesRepository
				.findByPaymentDateAndModeOfPayAndAmountPaidAndClassStudentAndStRouteAndCancelDateIsNull(
						stIncomeExpenses.getPaymentDate(), stIncomeExpenses.getModeOfPay(),
						stIncomeExpenses.getAmountPaid(), stIncomeExpenses.getClassStudent(),
						stIncomeExpenses.getStRoute());

		if (stIncomeExpensesFound == null || stIncomeExpensesFound.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * {@code PUT  /st-income-expenses/:id} : Updates an existing sTIncomeExpenses.
	 *
	 * @param id               the id of the sTIncomeExpenses to save.
	 * @param sTIncomeExpenses the sTIncomeExpenses to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated sTIncomeExpenses, or with status
	 *         {@code 400 (Bad Request)} if the sTIncomeExpenses is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         sTIncomeExpenses couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PutMapping("/st-income-expenses/{id}")
//	public ResponseEntity<STIncomeExpenses> updateSTIncomeExpenses(
//			@PathVariable(value = "id", required = false) final Long id,
//			@Valid @RequestBody STIncomeExpenses sTIncomeExpenses) throws URISyntaxException {
//		log.debug("REST request to update STIncomeExpenses : {}, {}", id, sTIncomeExpenses);
//		if (sTIncomeExpenses.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, sTIncomeExpenses.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!sTIncomeExpensesRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		STIncomeExpenses result = sTIncomeExpensesService.save(sTIncomeExpenses);
//		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
//				sTIncomeExpenses.getId().toString())).body(result);
//	}

	/**
	 * {@code PATCH  /st-income-expenses/:id} : Partial updates given fields of an
	 * existing sTIncomeExpenses, field will ignore if it is null
	 *
	 * @param id               the id of the sTIncomeExpenses to save.
	 * @param sTIncomeExpenses the sTIncomeExpenses to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated sTIncomeExpenses, or with status
	 *         {@code 400 (Bad Request)} if the sTIncomeExpenses is not valid, or
	 *         with status {@code 404 (Not Found)} if the sTIncomeExpenses is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         sTIncomeExpenses couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/st-income-expenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
//	public ResponseEntity<STIncomeExpenses> partialUpdateSTIncomeExpenses(
//			@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody STIncomeExpenses sTIncomeExpenses) throws URISyntaxException {
//		log.debug("REST request to partial update STIncomeExpenses partially : {}, {}", id, sTIncomeExpenses);
//		if (sTIncomeExpenses.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, sTIncomeExpenses.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!sTIncomeExpensesRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<STIncomeExpenses> result = sTIncomeExpensesService.partialUpdate(sTIncomeExpenses);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, sTIncomeExpenses.getId().toString()));
//	}

	/**
	 * {@code GET  /st-income-expenses} : get all the sTIncomeExpenses.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of sTIncomeExpenses in body.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/st-income-expenses")
	public ResponseEntity<List<STIncomeExpenses>> getAllSTIncomeExpenses(STIncomeExpensesCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get STIncomeExpenses by criteria: {}", criteria);
		Page<STIncomeExpenses> page = sTIncomeExpensesQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /st-income-expenses/count} : count all the sTIncomeExpenses.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/st-income-expenses/count")
	public ResponseEntity<Long> countSTIncomeExpenses(STIncomeExpensesCriteria criteria) {
		log.debug("REST request to count STIncomeExpenses by criteria: {}", criteria);
		return ResponseEntity.ok().body(sTIncomeExpensesQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /st-income-expenses/:id} : get the "id" sTIncomeExpenses.
	 *
	 * @param id the id of the sTIncomeExpenses to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the sTIncomeExpenses, or with status {@code 404 (Not Found)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/st-income-expenses/{id}")
	public ResponseEntity<STIncomeExpenses> getSTIncomeExpenses(@PathVariable Long id) {
		log.debug("REST request to get STIncomeExpenses : {}", id);
		Optional<STIncomeExpenses> sTIncomeExpenses = sTIncomeExpensesService.findOne(id);
		return ResponseUtil.wrapOrNotFound(sTIncomeExpenses);
	}

	/**
	 * {@code DELETE  /st-income-expenses/:id} : delete the "id" sTIncomeExpenses.
	 *
	 * @param id the id of the sTIncomeExpenses to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@DeleteMapping("/st-income-expenses/{id}")
	public ResponseEntity<Void> deleteSTIncomeExpenses(@PathVariable Long id) {
		log.debug("REST request to delete STIncomeExpenses : {}", id);
		// sTIncomeExpensesService.delete(id);
		String receiptId = "";
		Optional<STIncomeExpenses> stIncomeExpenseOpt = sTIncomeExpensesRepository.findById(id);
		if (stIncomeExpenseOpt.isPresent()) {
			STIncomeExpenses stIncomeExpense = stIncomeExpenseOpt.get();
			if (stIncomeExpense.getCancelDate() == null) {
				stIncomeExpense.setCancelDate(LocalDate.now());
				receiptId = stIncomeExpense.getReceiptId();
			}
			sTIncomeExpensesRepository.save(stIncomeExpense);
			AuditLog auditLog = new AuditLog("MARK DELETE TRANSPORT FEE", LocalDate.now(),
					schoolCommonService.getLoggedinUser(), schoolCommonService.getUserSchool(),
					stIncomeExpense.toString(), null, ENTITY_NAME);
			auditLog.setCreateDate(LocalDate.now());
			auditLogRepository.save(auditLog);

		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, receiptId)).build();
	}
}
