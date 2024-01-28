package com.ssik.manageit.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.IncomeExpenses;
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.SchoolReport;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.repository.IncomeExpensesRepository;
import com.ssik.manageit.repository.STIncomeExpensesRepository;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.repository.SchoolReportRepository;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.criteria.SchoolReportCriteria;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.dto.SchoolReportDTO;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;
import com.ssik.manageit.service.mapper.SchoolReportMapper;
import com.ssik.manageit.util.CSVUtils;

import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;

/**
 * Service Implementation for managing {@link SchoolReport}.
 */
@Service
@Transactional
public class SchoolReportService {

	private final Logger log = LoggerFactory.getLogger(SchoolReportService.class);

	private final SchoolReportRepository schoolReportRepository;

	private final SchoolReportMapper schoolReportMapper;

	@Autowired
	private StudentPaymentsQueryService studentPaymentsQueryService;

	@Autowired
	private SchoolCommonService schoolCommonService;
	@Autowired
	private SchoolClassService schoolClassService;

	@Autowired
	private StudentChargesSummaryQueryService studentChargesSummaryQueryService;

	@Autowired
	private IncomeExpensesRepository incomeExpenseRepository;

	@Autowired
	private STIncomeExpensesRepository stIncomeExpenseRepository;
	@Autowired
	private StudentAdditionalChargesService studentAdditionalChargeService;
	@Autowired
	private ClassStudentMapper classStudentMapper;
	@Autowired
	private SchoolLedgerHeadRepository schoolLedgerHeadRepo;
	@Autowired
	private StudentAdditionalChargesRepository studentAdditionalChargesRepository;

	// public static final String DATE_FORMAT = "dd/MM/yyyy";
	// SimpleDateFormat date_formatter = new SimpleDateFormat(DATE_FORMAT);
	DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

	public SchoolReportService(SchoolReportRepository schoolReportRepository, SchoolReportMapper schoolReportMapper) {
		this.schoolReportRepository = schoolReportRepository;
		this.schoolReportMapper = schoolReportMapper;
	}

	/**
	 * Save a schoolReport.
	 *
	 * @param schoolReportDTO the entity to save.
	 * @return the persisted entity.
	 */
	public SchoolReportDTO save(SchoolReportDTO schoolReportDTO) {
		log.debug("Request to save SchoolReport : {}", schoolReportDTO);
		SchoolReport schoolReport = schoolReportMapper.toEntity(schoolReportDTO);
		schoolReport = schoolReportRepository.save(schoolReport);
		return schoolReportMapper.toDto(schoolReport);
	}

	/**
	 * Partially update a schoolReport.
	 *
	 * @param schoolReportDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<SchoolReportDTO> partialUpdate(SchoolReportDTO schoolReportDTO) {
		log.debug("Request to partially update SchoolReport : {}", schoolReportDTO);

		return schoolReportRepository.findById(schoolReportDTO.getId()).map(existingSchoolReport -> {
			schoolReportMapper.partialUpdate(existingSchoolReport, schoolReportDTO);
			return existingSchoolReport;
		}).map(schoolReportRepository::save).map(schoolReportMapper::toDto);
	}

	/**
	 * Get all the schoolReports.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<SchoolReportDTO> findAll(Pageable pageable) {
		log.debug("Request to get all SchoolReports");
		return schoolReportRepository.findAll(pageable).map(schoolReportMapper::toDto);
	}

	/**
	 * Get all the schoolReports with eager load of many-to-many relationships.
	 *
	 * @return the list of entities.
	 */
	public Page<SchoolReportDTO> findAllWithEagerRelationships(Pageable pageable) {
		return schoolReportRepository.findAllWithEagerRelationships(pageable).map(schoolReportMapper::toDto);
	}

	/**
	 * Get one schoolReport by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<SchoolReportDTO> findOne(Long id) {
		log.debug("Request to get SchoolReport : {}", id);
		return schoolReportRepository.findOneWithEagerRelationships(id).map(schoolReportMapper::toDto);
	}

	/**
	 * Delete the schoolReport by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete SchoolReport : {}", id);
		schoolReportRepository.deleteById(id);
	}

	/**
	 * Find daily Income and expense.
	 *
	 * @param id the id of the entity.
	 */
	public void dailyIncomeExpenseReport(SchoolReportCriteria criteria) {
		log.debug("Request to delete SchoolReport : {}");
		// LocalDateFilter startDateFilter= new Localda;
		StudentPaymentsCriteria studentPaymentCriteria = new StudentPaymentsCriteria();
		studentPaymentCriteria.setPaymentDate(criteria.getStartDate());
		studentPaymentsQueryService.findByCriteria(null);
	}

	/**
	 * Find Income and expense for a rane of date
	 *
	 * @param id the id of the entity.
	 */
	public void dateRangeIncomeExpenseReport(SchoolReportCriteria criteria) {
		log.debug("Request to delete SchoolReport : {}");
		// LocalDateFilter startDateFilter= new Localda;
		StudentPaymentsCriteria studentPaymentCriteria = new StudentPaymentsCriteria();
		studentPaymentCriteria.setPaymentDate(criteria.getStartDate());
		studentPaymentsQueryService.findByCriteria(null);
	}

	public byte[] getDueListInCsv(SchoolReportDTO schoolReportDTO, School school, boolean onlyClosingBalance) {

		List<ClassStudent> classStudents = schoolCommonService.getListOfStudentsForASchool(school);
		classStudents = classStudents.stream().sorted(Comparator.comparing(ClassStudent::getSchoolClassId))
				.collect(Collectors.toList());

		return getCsvForDueList(
				studentChargesSummaryQueryService.findDuesForStudents(classStudents, onlyClosingBalance));
	}

	public byte[] getSessionOpeningBalanceInCsv(SchoolReportDTO schoolReportDTO, School school,
			boolean onlyClosingBalance) {

		List<ClassStudent> classStudents = schoolCommonService.getListOfStudentsForASchool(school);
		classStudents = classStudents.stream().sorted(Comparator.comparing(ClassStudent::getSchoolClassId))
				.collect(Collectors.toList());
		List<StudentChargesSummaryDTO> studentChargesSummaries = studentChargesSummaryQueryService
				.findDuesForStudents(classStudents, onlyClosingBalance);

		// TODO correct this -but for now, add the march closing as new apr opening
		for (StudentChargesSummaryDTO studentChargesSummaryDTO : studentChargesSummaries) {
			studentChargesSummaryDTO.setAprSummary(studentChargesSummaryDTO.getMarSummary());
		}
		saveSessionOpeningBalance(studentChargesSummaries);
		return getCsvForDueList(studentChargesSummaries);
	}

	@Transactional
	private void saveSessionOpeningBalance(List<StudentChargesSummaryDTO> studentChargesSummaries) {
		List<StudentAdditionalCharges> studentSessionStartAdditionalCharges = new ArrayList<StudentAdditionalCharges>();
		// Add additional charge for the session opening
		SchoolLedgerHead sessionOpeningLedgerHead = schoolLedgerHeadRepo.findByLedgerHeadName("SessionOpeningBalance")
				.get(0);
		for (StudentChargesSummaryDTO studentChargesSummaryDTO : studentChargesSummaries) {
			StudentAdditionalCharges newSessionOpeningBalance = new StudentAdditionalCharges(FeeYear.YEAR_2023, 4);
			// Set March closing balance as session ooening balance
			newSessionOpeningBalance.setAprAddChrg(Double.parseDouble(studentChargesSummaryDTO.getMarSummary()));
			newSessionOpeningBalance.setSchoolLedgerHead(sessionOpeningLedgerHead);
			newSessionOpeningBalance
					.setClassStudent(classStudentMapper.toEntity(studentChargesSummaryDTO.getClassStudent()));
			studentSessionStartAdditionalCharges.add(newSessionOpeningBalance);
			newSessionOpeningBalance.setCreateDate(LocalDate.now());

		}
		studentAdditionalChargesRepository.saveAll(studentSessionStartAdditionalCharges);
	}

	public List<StudentChargesSummaryDTO> getDueList(SchoolReportDTO schoolReportDTO, School school,
			boolean onlyClosingBalance) {
		List<ClassStudent> classStudents = schoolCommonService.getListOfStudentsForASchool(school);
		classStudents = classStudents.stream().sorted(Comparator.comparing(ClassStudent::getSchoolClassId))
				.collect(Collectors.toList());

		return studentChargesSummaryQueryService.findDuesForStudents(classStudents, onlyClosingBalance);

	}

	public byte[] getStudentRecords(SchoolReportDTO schoolReportDTO, School school) {
		List<ClassStudent> students = schoolCommonService.getListOfStudentsForASchool(school);
		return getCSVForStudentDetails(students, school);
	}

	public byte[] getStudentFeePaymentDetails(SchoolReportDTO schoolReportDTO, School school) {
		StudentPaymentsCriteria studentPaymentCriteria = new StudentPaymentsCriteria();
		LocalDateFilter localDateFilter = new LocalDateFilter();
		localDateFilter.setGreaterThanOrEqual(schoolReportDTO.getStartDate());
		localDateFilter.setLessThan(schoolReportDTO.getEndDate());
		studentPaymentCriteria.setPaymentDate(localDateFilter);

		String startDateString = formatter.format(schoolReportDTO.getStartDate());
		String endDateString = formatter.format(schoolReportDTO.getEndDate());

		LongFilter studentIds = new LongFilter();
		studentIds.setIn(schoolCommonService.getListOfStudentIdsForASchool(school));
		studentPaymentCriteria.setClassStudentId(studentIds);

		if (school.getSchoolName().equalsIgnoreCase("Lords")) {
			return getCsvForCollectionsForLords(studentPaymentsQueryService.findByCriteria(studentPaymentCriteria),
					startDateString, endDateString, school);
		} else {
			return getCsvForCollectionsForSSIK(studentPaymentsQueryService.findByCriteria(studentPaymentCriteria),
					startDateString, endDateString, school);

		}
	}

	public byte[] getCsvForDueList(List<StudentChargesSummaryDTO> chargesSummaries) {
		StringBuilder headers = new StringBuilder();
		StringBuilder content = new StringBuilder();

		CSVUtils.append(headers, "StudentClass", "StudentId", "Name", "FatherName", "Apr", "May", "Jun", "Jul", "Aug",
				"Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar");
		CSVUtils.endRow(headers);
		if (chargesSummaries.size() > 0) {
			Double summary = 0.0;
			for (StudentChargesSummaryDTO chargeSummary : chargesSummaries) {
				summary = summary + Double.parseDouble(chargeSummary.getMarSummary());
				CSVUtils.add(content, chargeSummary.getClassStudent().getSchoolClass().getClassName(),
						chargeSummary.getClassStudent().getStudentId(),
						chargeSummary.getClassStudent().getFirstName() + " "
								+ chargeSummary.getClassStudent().getLastName(),
						chargeSummary.getClassStudent().getFatherName(), chargeSummary.getAprSummary(),
						chargeSummary.getMaySummary(), chargeSummary.getJunSummary(), chargeSummary.getJulSummary(),
						chargeSummary.getAugSummary(), chargeSummary.getSepSummary(), chargeSummary.getOctSummary(),
						chargeSummary.getNovSummary(), chargeSummary.getDecSummary(), chargeSummary.getJanSummary(),
						chargeSummary.getFebSummary(), chargeSummary.getMarSummary());
				CSVUtils.endRow(content);
			}
			CSVUtils.add(content, "------------TOTAL DUES ------------------", summary);
			CSVUtils.endRow(content);
		}
		byte[] csvData = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
			baos.write(content.toString().getBytes(CharEncoding.UTF_8));
			csvData = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Exception creating CSV data for due list...");
		}

		return csvData;
	}

	public byte[] getCsvForIncomeExpenses(SchoolReportDTO schoolReportDTO, School school) {

		List<IncomeExpenses> incomeExpenses = incomeExpenseRepository
				.findBySchoolAndPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(school,
						schoolReportDTO.getStartDate(), schoolReportDTO.getEndDate());
		StringBuilder headers = new StringBuilder();
		StringBuilder content = new StringBuilder();

		CSVUtils.append(headers, "Date", "LedgerHead", "Amount", "ReceiptId", "Mode", "Vendor", "Remarks",
				"TransactionType");
		CSVUtils.endRow(headers);
		if (incomeExpenses.size() > 0) {
			Double summary = 0.0;
			for (IncomeExpenses incomeExpense : incomeExpenses) {
				summary = summary + incomeExpense.getAmountPaid();
				CSVUtils.add(content, incomeExpense.getPaymentDate(), incomeExpense.getLedgerHead().getLedgerHeadName(),
						incomeExpense.getAmountPaid() + " ", incomeExpense.getReceiptId(), incomeExpense.getModeOfPay(),
						incomeExpense.getVendor().getVendorName(), incomeExpense.getRemarks(),
						incomeExpense.getTransactionType());
				CSVUtils.endRow(content);
			}

			IncomeExpenses incomeExpense = incomeExpenses.get(0);
			CSVUtils.add(content, incomeExpense.getPaymentDate(), "----------- TOTAL --------------------------",
					summary);
			CSVUtils.endRow(content);

		}
		byte[] csvData = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
			baos.write(content.toString().getBytes(CharEncoding.UTF_8));
			csvData = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Exception creating CSV data ...");
		}

		return csvData;
	}

	public byte[] getCsvForSchoolTransportIncomeExpenses(SchoolReportDTO schoolReportDTO) {

		List<STIncomeExpenses> incomeExpenses = stIncomeExpenseRepository
				.findByPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(schoolReportDTO.getStartDate(),
						schoolReportDTO.getEndDate());
		StringBuilder headers = new StringBuilder();
		StringBuilder content = new StringBuilder();

		CSVUtils.append(headers, "Date", "Student", "AmountPaid", "ReceiptId", "Mode", "RouteName", "RouteAddress",
				"RouteCharges", "Remarks");
		CSVUtils.endRow(headers);
		if (incomeExpenses.size() > 0) {
			Double summary = 0.0;
			for (STIncomeExpenses stIncomeExpense : incomeExpenses) {
				summary = summary + stIncomeExpense.getAmountPaid();
				CSVUtils.add(content, stIncomeExpense.getPaymentDate(),
						stIncomeExpense.getClassStudent().getStudentId() + "--"
								+ stIncomeExpense.getClassStudent().getFirstName() + " "
								+ stIncomeExpense.getClassStudent().getLastName(),
						stIncomeExpense.getAmountPaid() + " ", stIncomeExpense.getReceiptId(),
						stIncomeExpense.getModeOfPay(), stIncomeExpense.getStRoute().getTransportRouteName(),
						stIncomeExpense.getStRoute().getTransportRouteAddress(),
						stIncomeExpense.getStRoute().getRouteCharge(), stIncomeExpense.getRemarks());
				CSVUtils.endRow(content);
			}
			// add summary row
			STIncomeExpenses stIncomeExpense = incomeExpenses.get(0);

			CSVUtils.add(content, stIncomeExpense.getPaymentDate(), "----------- TOTAL --------------------------",
					summary);
			CSVUtils.endRow(content);
		}
		byte[] csvData = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
			baos.write(content.toString().getBytes(CharEncoding.UTF_8));
			csvData = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Exception creating CSV data for transport...");
		}

		return csvData;
	}
//	public byte[] getCSVForTransportCharges(SchoolReportDTO schoolReportDTO) {
//		List<STIncomeExpenses> stIncomeExpenses = stIncomeExpenseRepository
//				.findByPaymentDateBetweenAndCancelDateIsNullOrderByPaymentDateAsc(schoolReportDTO.getStartDate(),
//						schoolReportDTO.getEndDate());
//		List<String> headers = new ArrayList<String>();
//		List<List<String>> rows = new ArrayList<List<String>>();
//		for(STIncomeExpenses stIncomeExpense: stIncomeExpenses) {
//			List<String> row=new ArrayList<String>();
//			row.add(""+stIncomeExpense.getPaymentDate());
//			row.add(stIncomeExpense.getClassStudent().getStudentId());
//		}
//
//		return getCsv(headers, rows);
//
//	}

//	private byte[] getCsv(List<String> headerRow, List<List<String>> rows) {
//
//		StringBuilder headers = new StringBuilder();
//		StringBuilder content = new StringBuilder();
//
//		for (String header : headerRow) {
//			CSVUtils.append(headers, header);
//		}
//		CSVUtils.endRow(headers);
//
//		for (List<String> cells : rows) {
//			for (String cell : cells)
//				CSVUtils.add(content, cell);
//			CSVUtils.endRow(content);
//		}
//		byte[] csvData = null;
//		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//			baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
//			baos.write(content.toString().getBytes(CharEncoding.UTF_8));
//			csvData = baos.toByteArray();
//		} catch (IOException e) {
//			throw new RuntimeException("Exception creating CSV data ...");
//		}
//
//		return csvData;
//	}

	private byte[] getCsvForCollectionsForLords(List<StudentPaymentsDTO> studentCredits, String startDate,
			String endDate, School school) {
		if (school != null && studentCredits != null) {
			log.info("Total records passed in for the School is :: " + school.getSchoolName() + " is "
					+ studentCredits.size());
		} else {
			log.info("Either school info or the student credit information is not available ");
		}
		byte[] csvData = null;
		if (studentCredits != null && studentCredits.size() > 0) {
			Map<String, List<SchoolClassDTO>> billGroups = new HashMap<>();
			List<SchoolClassDTO> bloomingBudsClasses = new ArrayList<>();
			bloomingBudsClasses.add(schoolClassService.findDto(1L));
			bloomingBudsClasses.add(schoolClassService.findDto(2L));
			bloomingBudsClasses.add(schoolClassService.findDto(3L));
			bloomingBudsClasses.add(schoolClassService.findDto(4L));
			bloomingBudsClasses.add(schoolClassService.findDto(5L));
			List<SchoolClassDTO> lordsInternational = new ArrayList<>();
			lordsInternational.add(schoolClassService.findDto(6L));
			lordsInternational.add(schoolClassService.findDto(7L));
			lordsInternational.add(schoolClassService.findDto(8L));
			lordsInternational.add(schoolClassService.findDto(9L));
			lordsInternational.add(schoolClassService.findDto(10L));
			lordsInternational.add(schoolClassService.findDto(11L));
			List<SchoolClassDTO> lords9And10 = new ArrayList<>();
			lords9And10.add(schoolClassService.findDto(12L));
			lords9And10.add(schoolClassService.findDto(13L));

			billGroups.put("Blooming Buds", bloomingBudsClasses);
			billGroups.put("Lords International", lordsInternational);
			billGroups.put("Lords 9 And 10", lords9And10);

			// List<ReceiptSummary> receiptSummaries =
			// addCreditsByReceiptId(studentCredits);
			Map<String, List<StudentPaymentsDTO>> classWiseReceipts = groupReceiptByClass(studentCredits);
			StringBuilder content = new StringBuilder();
			StringBuilder headers = new StringBuilder();
			Iterator<String> billGroupItr = billGroups.keySet().iterator();
			Double totalAmountAcrossGroup = 0.0;

			CSVUtils.append(headers, "Date", "StudentClass", "StudentId", "Name", "FatherName", "Amount", "ReceiptId");
			CSVUtils.endRow(headers);
			while (billGroupItr.hasNext()) {
				String billGroup = billGroupItr.next();
				CSVUtils.append(content, "", "", "", "Summary for " + billGroup, "", "", "");
				CSVUtils.endRow(content);
				List<SchoolClassDTO> studentClassesForAGroup = billGroups.get(billGroup);
				Double totalAmountForGroup = 0.0;
				for (SchoolClassDTO studentClass : studentClassesForAGroup) {
					List<StudentPaymentsDTO> receiptsForAclass = classWiseReceipts.get(studentClass.getClassName());
					if (receiptsForAclass != null) {
						for (StudentPaymentsDTO receiptSummary : receiptsForAclass) {
							CSVUtils.add(content, formatter.format(receiptSummary.getPaymentDate()),
									receiptSummary.getClassStudent().getSchoolClass().getClassLongName(),
									receiptSummary.getClassStudent().getStudentId(),
									receiptSummary.getClassStudent().getFirstName() + " "
											+ receiptSummary.getClassStudent().getLastName(),
									receiptSummary.getClassStudent().getFatherName(), receiptSummary.getAmountPaid(),
									receiptSummary.getReceiptId());
							totalAmountForGroup = totalAmountForGroup + receiptSummary.getAmountPaid();
							totalAmountAcrossGroup = totalAmountAcrossGroup + receiptSummary.getAmountPaid();
							CSVUtils.endRow(content);
						}
					}
				}
				// Add a summary row
				if (totalAmountForGroup > 0) {
					CSVUtils.append(content, "", "", "", "", "", "", "");
					CSVUtils.endRow(content);
					CSVUtils.add(content, "", "", "", " ", "",
							"Collections for " + billGroup + " from Date " + startDate + " to " + endDate + "  = ",
							totalAmountForGroup);
					CSVUtils.endRow(content);
					CSVUtils.append(content, "", "", "", "", "", "", "");
					CSVUtils.endRow(content);
					CSVUtils.append(content, "", "", "", "", "", "", "");
					CSVUtils.endRow(content);
				}
				// else {
				// CSVUtils.append(content, "1", "2", "3", "No collection for " + billGroup,
				// "5", "6", "7");
				// CSVUtils.endRow(content);
				// }

			}
			// Add a grand summary row
			CSVUtils.append(content, "", "", "", "", "", "", "");
			CSVUtils.endRow(content);
			CSVUtils.append(content, "", "", "", "", "", "", "");
			CSVUtils.endRow(content);
			CSVUtils.add(content, "", "", "", " ", "",
					"Total Collections from Date " + startDate + " to " + endDate + "  = ", totalAmountAcrossGroup);
			CSVUtils.endRow(content);

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
				baos.write(content.toString().getBytes(CharEncoding.UTF_8));
				csvData = baos.toByteArray();
			} catch (IOException e) {
				// LOG.error("Exception while generating csv byte array data.", e);
				// throw new ErrorCodeException(ErrorCodes.CSV_GENERATE_FAILED, e);
				throw new RuntimeException("Error generating daily report");
			}
			// LOG.debug("csv byte array was created ok.");
		}
		return csvData;
	}

	private byte[] getCSVForStudentDetails(List<ClassStudent> students, School school) {
		if (students != null) {
			log.info("Total records passed in for the School is :: " + school.getSchoolName() + " is "
					+ students.size());
		} else {
			log.info("Either school info or the student details is not available ");
			return "".getBytes();
		}
		byte[] csvData = null;
		if (students != null && students.size() > 0) {
			StringBuilder content = new StringBuilder();
			StringBuilder headers = new StringBuilder();

			CSVUtils.append(headers, "Date", "StudentClass", "StudentId", "Name", "DOB", "FatherName", "Phone",
					"Bloodgroup", "Address", "AddmissionDate", "CreateDate", "CancelDate");
			CSVUtils.endRow(headers);
			for (ClassStudent student : students) {
				CSVUtils.add(content, formatter.format(LocalDate.now()), student.getSchoolClass().getClassLongName(),
						student.getStudentId(), student.getFirstName() + " " + student.getLastName(),
						student.getDateOfBirth() == null ? "NA"
								: date_formatter.format(
										LocalDate.ofInstant(student.getDateOfBirth(), ZoneId.of("Asia/Kolkata"))),
						student.getFatherName(), student.getPhoneNumber(), student.getBloodGroup(),
						student.getAddressLine1() == null ? ""
								: student.getAddressLine1() + student.getAddressLine2() == null ? ""
										: student.getAddressLine2(),
						student.getAdmissionDate() == null ? "NA" : date_formatter.format(student.getAdmissionDate()),
						student.getCreateDate() == null ? "NA" : date_formatter.format(student.getCreateDate()),
						student.getCancelDate() == null ? "NA" : date_formatter.format(student.getCancelDate()));
				CSVUtils.endRow(content);
			}

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
				baos.write(content.toString().getBytes(CharEncoding.UTF_8));
				csvData = baos.toByteArray();
			} catch (IOException e) {
				// LOG.error("Exception while generating csv byte array data.", e);
				// throw new ErrorCodeException(ErrorCodes.CSV_GENERATE_FAILED, e);
				throw new RuntimeException("Error generating student details report");
			}
			// LOG.debug("csv byte array was created ok.");
		}
		return csvData;
	}

	private byte[] getCsvForCollectionsForSSIK(List<StudentPaymentsDTO> studentCredits, String startDate,
			String endDate, School school) {
		if (school != null && studentCredits != null) {
			log.info("Total records passed in for the School is :: " + school.getSchoolName() + " is "
					+ studentCredits.size());
		} else {
			log.info("Either school info or the student credit information is not available ");
		}
		byte[] csvData = null;
		if (studentCredits != null && studentCredits.size() > 0) {
			StringBuilder content = new StringBuilder();
			StringBuilder headers = new StringBuilder();
			Double totalAmount = 0.0;

			CSVUtils.append(headers, "Date", "StudentClass", "StudentId", "Name", "FatherName", "Amount", "ReceiptId");
			CSVUtils.endRow(headers);
			for (StudentPaymentsDTO receiptSummary : studentCredits) {
				CSVUtils.add(content, formatter.format(receiptSummary.getPaymentDate()),
						receiptSummary.getClassStudent().getSchoolClass().getClassLongName(),
						receiptSummary.getClassStudent().getStudentId(),
						receiptSummary.getClassStudent().getFirstName() + " "
								+ receiptSummary.getClassStudent().getLastName(),
						receiptSummary.getClassStudent().getFatherName(), receiptSummary.getAmountPaid(),
						receiptSummary.getReceiptId());
				totalAmount = totalAmount + receiptSummary.getAmountPaid();
				CSVUtils.endRow(content);
			}
			// Add a grand summary row
			CSVUtils.append(content, "", "", "", "", "", "", "");
			CSVUtils.endRow(content);
			CSVUtils.add(content, "", "", "", " ", "",
					"Total Collections from Date " + startDate + " to " + endDate + "  = ", totalAmount);
			CSVUtils.endRow(content);

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				baos.write(headers.toString().getBytes(CharEncoding.UTF_8));
				baos.write(content.toString().getBytes(CharEncoding.UTF_8));
				csvData = baos.toByteArray();
			} catch (IOException e) {
				// LOG.error("Exception while generating csv byte array data.", e);
				// throw new ErrorCodeException(ErrorCodes.CSV_GENERATE_FAILED, e);
				throw new RuntimeException("Error generating daily report");
			}
			// LOG.debug("csv byte array was created ok.");
		}
		return csvData;
	}

	private Map<String, List<StudentPaymentsDTO>> groupReceiptByClass(List<StudentPaymentsDTO> receipts) {
		Map<String, List<StudentPaymentsDTO>> classWiseReceipts = new HashMap<>();
		if (receipts != null) {
			for (StudentPaymentsDTO receiptSummary : receipts) {
				List<StudentPaymentsDTO> receiptsForAClass = classWiseReceipts
						.get(receiptSummary.getClassStudent().getSchoolClass().getClassName());
				if (receiptsForAClass == null) {
					receiptsForAClass = new ArrayList<>();
				}
				receiptsForAClass.add(receiptSummary);
				classWiseReceipts.put(receiptSummary.getClassStudent().getSchoolClass().getClassName(),
						receiptsForAClass);
			}
		}
		return classWiseReceipts;
	}
}
