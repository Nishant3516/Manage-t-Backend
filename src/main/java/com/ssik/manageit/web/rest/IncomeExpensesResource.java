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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.IncomeExpenses;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.repository.AuditLogRepository;
import com.ssik.manageit.repository.IncomeExpensesRepository;
import com.ssik.manageit.service.IncomeExpensesService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.util.IdStoreUtil;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.IncomeExpenses}.
 */
@RestController
@RequestMapping("/api")
public class IncomeExpensesResource {

	private final Logger log = LoggerFactory.getLogger(IncomeExpensesResource.class);

	private static final String ENTITY_NAME = "incomeExpenses";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	IdStoreUtil idStoreUtil;
	private final IncomeExpensesService incomeExpensesService;

	@Autowired
	private SchoolCommonService schoolCommonService;

	private final IncomeExpensesRepository incomeExpensesRepository;

	@Autowired
	private AuditLogRepository auditLogRepository;

	public IncomeExpensesResource(IncomeExpensesService incomeExpensesService,
			IncomeExpensesRepository incomeExpensesRepository) {
		this.incomeExpensesService = incomeExpensesService;
		this.incomeExpensesRepository = incomeExpensesRepository;
	}

	/**
	 * {@code POST  /income-expenses} : Create a new incomeExpenses.
	 *
	 * @param incomeExpenses the incomeExpenses to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new incomeExpenses, or with status {@code 400 (Bad Request)}
	 *         if the incomeExpenses has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/income-expenses")
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	public ResponseEntity<IncomeExpenses> createIncomeExpenses(@Valid @RequestBody IncomeExpenses incomeExpenses)
			throws URISyntaxException {
		log.debug("REST request to save IncomeExpenses : {}", incomeExpenses);
		if (incomeExpenses.getId() != null) {
			throw new BadRequestAlertException("A new incomeExpenses cannot already have an ID", ENTITY_NAME,
					"idexists");
		}

		if (incomeExpenses.getModeOfPay() == null) {
			incomeExpenses.setModeOfPay(ModeOfPayment.CASH);
		}
		if (incomeExpenses.getPaymentDate() == null) {
			incomeExpenses.setPaymentDate(LocalDate.now());
		}
		School school = schoolCommonService.getUserSchool();
		incomeExpenses.setCreateDate(LocalDate.now());
		if (isPaymentDuplicate(incomeExpenses, school)) {
			throw new BadRequestAlertException("This Income Expense already there", ENTITY_NAME,
					"IncomeExpense with same amount on same date for same Vendor already exists");
		}
		incomeExpenses.setReceiptId("" + idStoreUtil.getAndSaveNextId(IdType.RECEIPT));
		incomeExpenses.setSchool(school);
		incomeExpenses.setTenant(school.getTenant());
		IncomeExpenses result = incomeExpensesService.save(incomeExpenses);

		// Add a audit log
		AuditLog auditLog = new AuditLog("ADD", LocalDate.now(), schoolCommonService.getLoggedinUser(),
				schoolCommonService.getUserSchool(), incomeExpenses.toString(), null, ENTITY_NAME);
		auditLog.setCreateDate(LocalDate.now());
		auditLogRepository.save(auditLog);

		return ResponseEntity
				.created(new URI("/api/income-expenses/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	private Boolean isPaymentDuplicate(IncomeExpenses incomeExpenses, School school) {
		List<IncomeExpenses> incomeExpensesFound = incomeExpensesRepository
				.findBySchoolAndPaymentDateAndModeOfPayAndAmountPaidAndVendorAndCancelDateIsNull(school,
						incomeExpenses.getPaymentDate(), incomeExpenses.getModeOfPay(), incomeExpenses.getAmountPaid(),
						incomeExpenses.getVendor());

		if (incomeExpensesFound == null || incomeExpensesFound.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * {@code PUT  /income-expenses/:id} : Updates an existing incomeExpenses.
	 *
	 * @param id             the id of the incomeExpenses to save.
	 * @param incomeExpenses the incomeExpenses to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated incomeExpenses, or with status {@code 400 (Bad Request)}
	 *         if the incomeExpenses is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the incomeExpenses couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@PutMapping("/income-expenses/{id}")
//	public ResponseEntity<IncomeExpenses> updateIncomeExpenses(
//			@PathVariable(value = "id", required = false) final Long id,
//			@Valid @RequestBody IncomeExpenses incomeExpenses) throws URISyntaxException {
//		log.debug("REST request to update IncomeExpenses : {}, {}", id, incomeExpenses);
//		if (incomeExpenses.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		} else {
//
//		}
//		if (!Objects.equals(id, incomeExpenses.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!incomeExpensesRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//		incomeExpenses.setLastModified(LocalDate.now());
//		IncomeExpenses result = incomeExpensesService.save(incomeExpenses);
//		// Add a audit log
//		Optional<IncomeExpenses> oldIncomeExpenses = incomeExpensesRepository.findById(id);
//		AuditLog auditLog = new AuditLog("PUT FEE", LocalDate.now(), schoolCommonService.getLoggedinUser(),
//				schoolCommonService.getUserSchool(), incomeExpenses.toString(), oldIncomeExpenses.get().toString(),
//				ENTITY_NAME);
//		auditLog.setCreateDate(LocalDate.now());
//		auditLogRepository.save(auditLog);
//		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
//				incomeExpenses.getId().toString())).body(result);
//	}

	/**
	 * {@code PATCH  /income-expenses/:id} : Partial updates given fields of an
	 * existing incomeExpenses, field will ignore if it is null
	 *
	 * @param id             the id of the incomeExpenses to save.
	 * @param incomeExpenses the incomeExpenses to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated incomeExpenses, or with status {@code 400 (Bad Request)}
	 *         if the incomeExpenses is not valid, or with status
	 *         {@code 404 (Not Found)} if the incomeExpenses is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the incomeExpenses
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
//	@PatchMapping(value = "/income-expenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
//	public ResponseEntity<IncomeExpenses> partialUpdateIncomeExpenses(
//			@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody IncomeExpenses incomeExpenses) throws URISyntaxException {
//		log.debug("REST request to partial update IncomeExpenses partially : {}, {}", id, incomeExpenses);
//		if (incomeExpenses.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, incomeExpenses.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!incomeExpensesRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//		incomeExpenses.setLastModified(LocalDate.now());
//		Optional<IncomeExpenses> result = incomeExpensesService.partialUpdate(incomeExpenses);
//		Optional<IncomeExpenses> oldIncomeExpenses = incomeExpensesRepository.findById(id);
//		AuditLog auditLog = new AuditLog("PATCH FEE", LocalDate.now(), schoolCommonService.getLoggedinUser(),
//				schoolCommonService.getUserSchool(), incomeExpenses.toString(), oldIncomeExpenses.get().toString(),
//				ENTITY_NAME);
//		auditLog.setCreateDate(LocalDate.now());
//		auditLogRepository.save(auditLog);
//
//		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
//				ENTITY_NAME, incomeExpenses.getId().toString()));
//	}

	/**
	 * {@code GET  /income-expenses} : get all the incomeExpenses.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of incomeExpenses in body.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/income-expenses")
	public List<IncomeExpenses> getAllIncomeExpenses(@RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		if (!schoolCommonService.isReportForMoreThanAdayAllowed(startDate, endDate)) {
			throw new BadRequestAlertException("Invalid Date Range", ENTITY_NAME,
					"Report generation for more than a day not allowed");
		}
		log.debug("REST request to get all IncomeExpenses");
		School school = schoolCommonService.getUserSchool();

		return incomeExpensesRepository
				.findBySchoolAndPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(school, startDate, endDate);
	}

	/**
	 * {@code GET  /income-expenses/:id} : get the "id" incomeExpenses.
	 *
	 * @param id the id of the incomeExpenses to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the incomeExpenses, or with status {@code 404 (Not Found)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/income-expenses/{id}")
	public ResponseEntity<IncomeExpenses> getIncomeExpenses(@PathVariable Long id) {
		log.debug("REST request to get IncomeExpenses : {}", id);
		Optional<IncomeExpenses> incomeExpenses = incomeExpensesService.findOne(id);
		return ResponseUtil.wrapOrNotFound(incomeExpenses);
	}

	/**
	 * {@code DELETE  /income-expenses/:id} : delete the "id" incomeExpenses.
	 *
	 * @param id the id of the incomeExpenses to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@DeleteMapping("/income-expenses/{id}")
	public ResponseEntity<Void> deleteIncomeExpenses(@PathVariable Long id) {
		log.debug("REST request to delete IncomeExpenses : {}", id);
		// incomeExpensesService.delete(id);

		String receiptId = "";
		Optional<IncomeExpenses> incomeExpenseOpt = incomeExpensesService.findOne(id);
		if (incomeExpenseOpt.isPresent()) {
			IncomeExpenses incomeExpense = incomeExpenseOpt.get();
			receiptId = "" + incomeExpense.getReceiptId();
			incomeExpense.setCancelDate(LocalDate.now());
			incomeExpensesService.save(incomeExpense);
			// Add a audit log
			AuditLog auditLog = new AuditLog("DELETE", LocalDate.now(), schoolCommonService.getLoggedinUser(),
					schoolCommonService.getUserSchool(), incomeExpense.toString(), null, ENTITY_NAME);
			auditLog.setCreateDate(LocalDate.now());
			auditLogRepository.save(auditLog);

		}

		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, receiptId)).build();
	}
}
