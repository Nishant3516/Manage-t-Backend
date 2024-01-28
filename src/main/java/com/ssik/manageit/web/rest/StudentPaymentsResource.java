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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.StudentPayments;
import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.repository.AuditLogRepository;
import com.ssik.manageit.repository.StudentPaymentsRepository;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.StudentPaymentsQueryService;
import com.ssik.manageit.service.StudentPaymentsService;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria.ModeOfPaymentFilter;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.util.IdStoreUtil;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.StudentPayments}.
 */
@RestController
@RequestMapping("/api")
public class StudentPaymentsResource {

	private final Logger log = LoggerFactory.getLogger(StudentPaymentsResource.class);

	private static final String ENTITY_NAME = "studentPayments";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final StudentPaymentsService studentPaymentsService;

	private final StudentPaymentsRepository studentPaymentsRepository;

	private final StudentPaymentsQueryService studentPaymentsQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;

	@Autowired
	IdStoreUtil idStoreUtil;
	@Autowired
	private AuditLogRepository auditLogRepository;

	public StudentPaymentsResource(StudentPaymentsService studentPaymentsService,
			StudentPaymentsRepository studentPaymentsRepository,
			StudentPaymentsQueryService studentPaymentsQueryService) {
		this.studentPaymentsService = studentPaymentsService;
		this.studentPaymentsRepository = studentPaymentsRepository;
		this.studentPaymentsQueryService = studentPaymentsQueryService;
	}

	/**
	 * {@code POST  /student-payments} : Create a new studentPayments.
	 *
	 * @param studentPayments the studentPaymentsDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new studentPaymentsDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentPayments has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping("/student-payments")
	public ResponseEntity<StudentPayments> createStudentPayments(@Valid @RequestBody StudentPayments studentPayments)
			throws URISyntaxException {
		log.debug("REST request to save StudentPayments : {}", studentPayments);
		if (studentPayments.getId() != null) {
			throw new BadRequestAlertException("A new studentPayments cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (studentPayments.getModeOfPay() == null) {
			studentPayments.setModeOfPay(ModeOfPayment.CASH);
		}
		if (studentPayments.getPaymentDate() == null) {
			studentPayments.setPaymentDate(LocalDate.now());
		}
		studentPayments.setCreateDate(LocalDate.now());
		if (isPaymentDuplicate(studentPayments)) {
			throw new BadRequestAlertException("This studentPayments already there", ENTITY_NAME,
					"Payment with same amount on same date for same student already exists");
		}

		studentPayments.setReceiptId("" + idStoreUtil.getAndSaveNextId(IdType.RECEIPT));
		School school = schoolCommonService.getUserSchool();
		studentPayments.setSchool(school);
		studentPayments.setTenant(school.getTenant());

		StudentPayments result = studentPaymentsRepository.save(studentPayments);
		// Add a audit log
		AuditLog auditLog = new AuditLog("ADD", LocalDate.now(), schoolCommonService.getLoggedinUser(),
				schoolCommonService.getUserSchool(), studentPayments.toString(), null, ENTITY_NAME);
		auditLog.setCreateDate(LocalDate.now());
		auditLogRepository.save(auditLog);

		return ResponseEntity
				.created(new URI("/api/student-payments/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	private Boolean isPaymentDuplicate(StudentPayments studentPayments) {
		StudentPaymentsCriteria criteria = new StudentPaymentsCriteria();
		DoubleFilter paymentAmount = new DoubleFilter();
		paymentAmount.setEquals(studentPayments.getAmountPaid());

		LocalDateFilter payDate = new LocalDateFilter();
		payDate.setEquals(studentPayments.getPaymentDate());

		LongFilter studentId = new LongFilter();
		studentId.setEquals(studentPayments.getClassStudent().getId());

		ModeOfPaymentFilter modeOfPay = new ModeOfPaymentFilter();
		modeOfPay.setEquals(studentPayments.getModeOfPay());

		criteria.setAmountPaid(paymentAmount);
		criteria.setPaymentDate(payDate);
		criteria.setClassStudentId(studentId);
		criteria.setModeOfPay(modeOfPay);

		List<StudentPaymentsDTO> payments = studentPaymentsQueryService.findByCriteria(criteria);
		if (payments == null || payments.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * {@code PUT  /student-payments/:id} : Updates an existing studentPayments.
	 *
	 * @param id                 the id of the studentPaymentsDTO to save.
	 * @param studentPaymentsDTO the studentPaymentsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentPaymentsDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentPaymentsDTO is not valid, or
	 *         with status {@code 500 (Internal Server Error)} if the
	 *         studentPaymentsDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//    @PutMapping("/student-payments/{id}")
//    public ResponseEntity<StudentPaymentsDTO> updateStudentPayments(
//        @PathVariable(value = "id", required = false) final Long id,
//        @Valid @RequestBody StudentPaymentsDTO studentPaymentsDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to update StudentPayments : {}, {}", id, studentPaymentsDTO);
//        if (studentPaymentsDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, studentPaymentsDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!studentPaymentsRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        StudentPaymentsDTO result = studentPaymentsService.save(studentPaymentsDTO);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentPaymentsDTO.getId().toString()))
//            .body(result);
//    }

	/**
	 * {@code PATCH  /student-payments/:id} : Partial updates given fields of an
	 * existing studentPayments, field will ignore if it is null
	 *
	 * @param id                 the id of the studentPaymentsDTO to save.
	 * @param studentPaymentsDTO the studentPaymentsDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated studentPaymentsDTO, or with status
	 *         {@code 400 (Bad Request)} if the studentPaymentsDTO is not valid, or
	 *         with status {@code 404 (Not Found)} if the studentPaymentsDTO is not
	 *         found, or with status {@code 500 (Internal Server Error)} if the
	 *         studentPaymentsDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//    @PatchMapping(value = "/student-payments/{id}", consumes = "application/merge-patch+json")
//    public ResponseEntity<StudentPaymentsDTO> partialUpdateStudentPayments(
//        @PathVariable(value = "id", required = false) final Long id,
//        @NotNull @RequestBody StudentPaymentsDTO studentPaymentsDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update StudentPayments partially : {}, {}", id, studentPaymentsDTO);
//        if (studentPaymentsDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, studentPaymentsDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!studentPaymentsRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<StudentPaymentsDTO> result = studentPaymentsService.partialUpdate(studentPaymentsDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentPaymentsDTO.getId().toString())
//        );
//    }

	/**
	 * {@code GET  /student-payments} : get all the studentPayments.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of studentPayments in body.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/student-payments")
	public ResponseEntity<List<StudentPaymentsDTO>> getAllStudentPayments(StudentPaymentsCriteria criteria) {
		log.debug("REST request to get StudentPayments by criteria: {}", criteria);
		List<StudentPaymentsDTO> entityList = studentPaymentsQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	/**
	 * {@code GET  /student-payments/count} : count all the studentPayments.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/student-payments/count")
	public ResponseEntity<Long> countStudentPayments(StudentPaymentsCriteria criteria) {
		log.debug("REST request to count StudentPayments by criteria: {}", criteria);
		return ResponseEntity.ok().body(studentPaymentsQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /student-payments/:id} : get the "id" studentPayments.
	 *
	 * @param id the id of the studentPaymentsDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the studentPaymentsDTO, or with status {@code 404 (Not Found)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/student-payments/{id}")
	public ResponseEntity<StudentPaymentsDTO> getStudentPayments(@PathVariable Long id) {
		log.debug("REST request to get StudentPayments : {}", id);
		Optional<StudentPaymentsDTO> studentPaymentsDTO = studentPaymentsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(studentPaymentsDTO);
	}

	/**
	 * {@code DELETE  /student-payments/:id} : delete the "id" studentPayments.
	 *
	 * @param id the id of the studentPaymentsDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@DeleteMapping("/student-payments/{id}")
	public ResponseEntity<Void> deleteStudentPayments(@PathVariable Long id) {
		log.debug("REST request to delete StudentPayments : {}", id);
		// studentPaymentsService.delete(id);
		String receiptId = "";
		Optional<StudentPaymentsDTO> studentsPaymentOpt = studentPaymentsService.findOne(id);
		if (studentsPaymentOpt.isPresent()) {
			StudentPaymentsDTO studentsPayment = studentsPaymentOpt.get();
			receiptId = "" + studentsPayment.getReceiptId();
			studentsPayment.setCancelDate(LocalDate.now());
			studentPaymentsService.save(studentsPayment);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, receiptId)).build();
	}
}
