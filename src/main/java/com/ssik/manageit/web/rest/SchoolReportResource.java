package com.ssik.manageit.web.rest;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.domain.enumeration.ReportType;
import com.ssik.manageit.repository.SchoolReportRepository;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.SchoolLedgerHeadQueryService;
import com.ssik.manageit.service.SchoolReportQueryService;
import com.ssik.manageit.service.SchoolReportService;
import com.ssik.manageit.service.criteria.SchoolLedgerHeadCriteria;
import com.ssik.manageit.service.criteria.SchoolReportCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.dto.SchoolReportDTO;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.service.mapper.SchoolLedgerHeadMapper;
import com.ssik.manageit.service.mapper.StudentAdditionalChargesMapper;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.SchoolReport}.
 */
@RestController
@RequestMapping("/api")
public class SchoolReportResource {

	private final Logger log = LoggerFactory.getLogger(SchoolReportResource.class);

	private static final String ENTITY_NAME = "schoolReport";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolReportService schoolReportService;

	private final SchoolReportRepository schoolReportRepository;

	private final SchoolReportQueryService schoolReportQueryService;
	@Autowired
	private SchoolCommonService schoolCommonService;
	@Autowired
	private SchoolLedgerHeadQueryService ledgerHeadQueryService;
	@Autowired
	private SchoolLedgerHeadMapper schoolLedgerHeadMapper;
	@Autowired
	private StudentAdditionalChargesMapper studentAdditionalChargesMapper;
	@Autowired
	private StudentAdditionalChargesRepository studentAdditionalChargesRepository;

	public SchoolReportResource(SchoolReportService schoolReportService, SchoolReportRepository schoolReportRepository,
			SchoolReportQueryService schoolReportQueryService) {
		this.schoolReportService = schoolReportService;
		this.schoolReportRepository = schoolReportRepository;
		this.schoolReportQueryService = schoolReportQueryService;
	}

	/**
	 * {@code POST  /school-reports} : Create a new schoolReport.
	 *
	 * @param schoolReportDTO the schoolReportDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new schoolReportDTO, or with status
	 *         {@code 400 (Bad Request)} if the schoolReport has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	// @PostMapping("/school-reports",produces = "text/csv; charset=utf-8")
	@RequestMapping(value = "/school-reports", method = RequestMethod.POST)
	public ResponseEntity<SchoolReportDTO> createSchoolReport(@Valid @RequestBody SchoolReportDTO schoolReportDTO)
			throws URISyntaxException {
		log.debug("REST request to save SchoolReport : {}", schoolReportDTO);
		if (!schoolCommonService.isReportForMoreThanAdayAllowed(schoolReportDTO.getStartDate(),
				schoolReportDTO.getEndDate())) {
			throw new BadRequestAlertException("Invalid Date Range", ENTITY_NAME,
					"Report generation for more than a day not allowed");
		}
		if (schoolReportDTO.getId() != null) {
			throw new BadRequestAlertException("A new schoolReport cannot already have an ID", ENTITY_NAME, "idexists");
		}
		School school = schoolCommonService.getUserSchool();
		if (schoolReportDTO.getReportType().equals(ReportType.STUDENT_DETAILS)) {
			schoolReportDTO.setReportFile(schoolReportService.getStudentRecords(schoolReportDTO, school));
		} else if (schoolReportDTO.getReportType().equals(ReportType.FEE_COLLECTION)) {
			schoolReportDTO.setReportFile(schoolReportService.getStudentFeePaymentDetails(schoolReportDTO, school));
		} else if (schoolReportDTO.getReportType().equals(ReportType.FEE_DUE)) {
			schoolReportDTO.setReportFile(schoolReportService.getDueListInCsv(schoolReportDTO, school, true));
		} else if (schoolReportDTO.getReportType().equals(ReportType.SESSION_TRANSITION)) {
//			schoolReportDTO
//					.setReportFile(schoolReportService.getSessionOpeningBalanceInCsv(schoolReportDTO, school, true));
			// TODO error, so not supported
		} else if (schoolReportDTO.getReportType().equals(ReportType.INCOME_EXPENSE)) {
			schoolReportDTO.setReportFile(schoolReportService.getCsvForIncomeExpenses(schoolReportDTO, school));
		} else if (schoolReportDTO.getReportType().equals(ReportType.TRANSPORT)) {
			schoolReportDTO.setReportFile(schoolReportService.getCsvForSchoolTransportIncomeExpenses(schoolReportDTO));
		} else if (schoolReportDTO.getReportType().equals(ReportType.APPLY_FEE_DUE)) {
			List<StudentChargesSummaryDTO> dueList = schoolReportService.getDueList(schoolReportDTO, school, true);
			log.info("Total students having dues is " + dueList.size());
			SchoolLedgerHeadCriteria schoolLedgerHeadCriteria = new SchoolLedgerHeadCriteria();
			StringFilter ledgerHeadName = new StringFilter();
			ledgerHeadName.setEquals("Late Fine");
			schoolLedgerHeadCriteria.setLedgerHeadName(ledgerHeadName);

			List<SchoolLedgerHead> schoolLegderHeads = ledgerHeadQueryService.findByCriteria(schoolLedgerHeadCriteria);
			SchoolLedgerHeadDTO schoolLegderHeadDto = null;
			if (!schoolLegderHeads.isEmpty() && schoolLegderHeads.size() == 1) {
				schoolLegderHeadDto = schoolLedgerHeadMapper.toDto(schoolLegderHeads.get(0));
			} else {
				throw new RuntimeException("Ledger Head error ... ");
			}
			Double lateFine = 50.0;
			Double maxDueAllowed = 500.0;
			if (school.getSchoolName().equalsIgnoreCase("ssik")) {
				lateFine = 100.0;
				maxDueAllowed = 500.0;
			}
			LocalDate feeAsOnDate = schoolReportDTO.getStartDate();
			int dueMonth = feeAsOnDate.getMonthValue();

			List<StudentAdditionalChargesDTO> studentsDue = new ArrayList<StudentAdditionalChargesDTO>();
			for (StudentChargesSummaryDTO studentChargesSummary : dueList) {
				StudentAdditionalChargesDTO studentDue = addStudentDueCharges(dueMonth, maxDueAllowed, lateFine,
						studentChargesSummary, schoolLegderHeadDto);
				if (studentDue != null) {
					// it will be null if the due amount for a month is less than the permissible
					// limit
					studentsDue.add(studentDue);
				}
			}
			studentAdditionalChargesRepository.saveAll(studentAdditionalChargesMapper.toEntity(studentsDue));

			// refetch the due list --and this time there should be additonal charge
			schoolReportDTO.setReportFile(schoolReportService.getDueListInCsv(schoolReportDTO, school, true));

			// schoolReportDTO.setReportFile("File Generated".getBytes());
		} else {
			throw new RuntimeException("Operation Not Supported ..");
		}
		// SchoolReportDTO result = schoolReportService.save(schoolReportDTO);
		// return ResponseEntity
		// .created(new URI("/api/school-reports/" + result.getId()))
		// .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
		// ENTITY_NAME, result.getId().toString()))
		// .body(result);
		schoolReportDTO.setReportFileContentType("text/csv; charset=utf-8");
		return ResponseEntity.ok().body(schoolReportDTO);
	}

	private StudentAdditionalChargesDTO addStudentDueCharges(int dueMonth, Double maxDueAllowed, Double lateFine,
			StudentChargesSummaryDTO studentCharges, SchoolLedgerHeadDTO schoolLegderHeadDto) {

		// Add discount if the start date is later APR
		ClassStudentDTO classStudentDTO = studentCharges.getClassStudent();

		StudentAdditionalChargesDTO studentAdditionalChargesDto = null;

		Double dueAmount = 0.0;

		switch (dueMonth) {
		case 4:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getAprSummary());
			break;
		case 5:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getMaySummary());
			break;
		case 6:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getJunSummary());
			break;
		case 7:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getJulSummary());
			break;
		case 8:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getAugSummary());
			break;
		case 9:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getSepSummary());
			break;
		case 10:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getOctSummary());
			break;
		case 11:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getNovSummary());
			break;
		case 12:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getDecSummary());
			break;
		case 1:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getJanSummary());
			break;
		case 2:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine, 0.0);
			dueAmount = Double.parseDouble(studentCharges.getFebSummary());
			break;
		case 3:
			studentAdditionalChargesDto = new StudentAdditionalChargesDTO(schoolLegderHeadDto, classStudentDTO,
					FeeYear.YEAR_2023, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, lateFine);
			dueAmount = Double.parseDouble(studentCharges.getMarSummary());
			break;
		default:
			break;
		}
		if (dueAmount <= maxDueAllowed) {
			// we do not want to additional charge is due is less than permissible limit
			// -this is more to simplify the stuff
			return null;
		}
		return studentAdditionalChargesDto;
	}

	/**
	 * {@code PUT  /school-reports/:id} : Updates an existing schoolReport.
	 *
	 * @param id              the id of the schoolReportDTO to save.
	 * @param schoolReportDTO the schoolReportDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolReportDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolReportDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the schoolReportDTO couldn't
	 *         be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/school-reports/{id}")
	public ResponseEntity<SchoolReportDTO> updateSchoolReport(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody SchoolReportDTO schoolReportDTO) throws URISyntaxException {
		log.debug("REST request to update SchoolReport : {}, {}", id, schoolReportDTO);
		if (schoolReportDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolReportDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolReportRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		SchoolReportDTO result = schoolReportService.save(schoolReportDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				schoolReportDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /school-reports/:id} : Partial updates given fields of an
	 * existing schoolReport, field will ignore if it is null
	 *
	 * @param id              the id of the schoolReportDTO to save.
	 * @param schoolReportDTO the schoolReportDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated schoolReportDTO, or with status {@code 400 (Bad Request)}
	 *         if the schoolReportDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the schoolReportDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the schoolReportDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/school-reports/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<SchoolReportDTO> partialUpdateSchoolReport(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody SchoolReportDTO schoolReportDTO) throws URISyntaxException {
		log.debug("REST request to partial update SchoolReport partially : {}, {}", id, schoolReportDTO);
		if (schoolReportDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, schoolReportDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!schoolReportRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<SchoolReportDTO> result = schoolReportService.partialUpdate(schoolReportDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, schoolReportDTO.getId().toString()));
	}

	/**
	 * {@code GET  /school-reports} : get all the schoolReports.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolReports in body.
	 */
	@GetMapping("/school-reports")
	public ResponseEntity<List<SchoolReportDTO>> getAllSchoolReports(LocalDate startDate, LocalDate endDate) {
		// log.debug("REST request to get SchoolReports by criteria: {}", criteria);
		// Page<SchoolReportDTO> page =
		// schoolReportQueryService.findByCriteria(criteria, pageable);
		// HttpHeaders headers =
		// PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
		// page);
		List<SchoolReportDTO> reports = new ArrayList<>();
		return ResponseEntity.ok().body(reports);
		// return "success";
	}

	/**
	 * {@code GET  /school-reports/count} : count all the schoolReports.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-reports/count")
	public ResponseEntity<Long> countSchoolReports(SchoolReportCriteria criteria) {
		log.debug("REST request to count SchoolReports by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolReportQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-reports/:id} : get the "id" schoolReport.
	 *
	 * @param id the id of the schoolReportDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolReportDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-reports/{id}")
	public ResponseEntity<SchoolReportDTO> getSchoolReport(@PathVariable Long id) {
		log.debug("REST request to get SchoolReport : {}", id);
		Optional<SchoolReportDTO> schoolReportDTO = schoolReportService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolReportDTO);
	}

	/**
	 * {@code DELETE  /school-reports/:id} : delete the "id" schoolReport.
	 *
	 * @param id the id of the schoolReportDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/school-reports/{id}")
	public ResponseEntity<Void> deleteSchoolReport(@PathVariable Long id) {
		log.debug("REST request to delete SchoolReport : {}", id);
		schoolReportService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
