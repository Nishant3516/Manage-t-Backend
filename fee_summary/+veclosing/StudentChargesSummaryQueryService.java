package com.ssik.manageit.service;

import com.ssik.manageit.domain.*;
// for static metamodels
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.domain.StudentChargesSummary;
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.repository.IdStoreRepository;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.repository.StudentChargesSummaryRepository;
import com.ssik.manageit.service.criteria.ClassFeeCriteria;
import com.ssik.manageit.service.criteria.StudentAdditionalChargesCriteria;
import com.ssik.manageit.service.criteria.StudentChargesSummaryCriteria;
import com.ssik.manageit.service.criteria.StudentDiscountCriteria;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;
import com.ssik.manageit.service.mapper.StudentChargesSummaryMapper;
import com.ssik.manageit.service.mapper.StudentPaymentsMapper;
import com.ssik.manageit.web.rest.vm.MonthWiseDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;

/**
 * Service for executing complex queries for {@link StudentChargesSummary} entities in the database.
 * The main input is a {@link StudentChargesSummaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentChargesSummaryDTO} or a {@link Page} of {@link StudentChargesSummaryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentChargesSummaryQueryService extends QueryService<StudentChargesSummary> {

    private final Logger log = LoggerFactory.getLogger(StudentChargesSummaryQueryService.class);

    private final StudentChargesSummaryRepository studentChargesSummaryRepository;

    private final StudentChargesSummaryMapper studentChargesSummaryMapper;
    private final ClassStudentRepository classStudentRepository;
    private final ClassFeeQueryService classFeeQueryService;
    private final StudentDiscountQueryService studentDiscountQueryService;
    private final StudentAdditionalChargesQueryService studentAdditionalChargesQueryService;
    private final StudentPaymentsQueryService studentPaymentsQueryService;
    private final SchoolLedgerHeadRepository schoolLedgerHeadRepo;

    @Autowired
    private ClassStudentService classStudentService;

    public StudentChargesSummaryQueryService(
        StudentChargesSummaryRepository studentChargesSummaryRepository,
        StudentChargesSummaryMapper studentChargesSummaryMapper,
        ClassStudentRepository classStudentRepository,
        ClassFeeQueryService classFeeQueryServide,
        StudentDiscountQueryService studentDiscountQueryService,
        StudentAdditionalChargesQueryService studentAdditionalChargesQueryService,
        StudentPaymentsQueryService studentPaymentsQueryService,
        SchoolLedgerHeadRepository schoolLedgerHeadRepo
    ) {
        this.studentChargesSummaryRepository = studentChargesSummaryRepository;
        this.studentChargesSummaryMapper = studentChargesSummaryMapper;
        this.classStudentRepository = classStudentRepository;
        this.classFeeQueryService = classFeeQueryServide;
        this.studentDiscountQueryService = studentDiscountQueryService;
        this.studentAdditionalChargesQueryService = studentAdditionalChargesQueryService;
        this.studentPaymentsQueryService = studentPaymentsQueryService;
        this.schoolLedgerHeadRepo = schoolLedgerHeadRepo;
    }

    /**
     * Return a {@link List} of {@link StudentChargesSummaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentChargesSummaryDTO> findByCriteria(StudentChargesSummaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentChargesSummary> specification = createSpecification(criteria);
        return studentChargesSummaryMapper.toDto(studentChargesSummaryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentChargesSummaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentChargesSummaryDTO> findByCriteria(StudentChargesSummaryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentChargesSummary> specification = createSpecification(criteria);
        return studentChargesSummaryRepository.findAll(specification, page).map(studentChargesSummaryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<StudentChargesSummaryDTO> findByStudentId(List<String> studentIds, boolean onlyClosingBalance) {
        List<StudentChargesSummaryDTO> studentChargesSummaries = new ArrayList<>();
        List<StudentChargesSummaryDTO> onlyClosingSummaries = new ArrayList<>();

        //get details of student
        //fet details of class fee
        //get details of add charges
        //get details of discount
        //    	ClassStudentCriteria classStudentCriteria=new ClassStudentCriteria();
        //    	StringFilter studentIdFilter=new StringFilter();
        //    	studentIdFilter.setEquals(studentId);
        //    	classStudentCriteria.setStudentId(studentIdFilter);
        //        final Specification<ClassStudent> specification =  classStudentQueryService.createSpecification(classStudentCriteria);
        for (String studentId : studentIds) {
            log.debug("find by StudentId : {}", studentId);
            ClassStudent classStudentFound = classStudentRepository.findByStudentId(studentId);
            ClassStudentDTO classStudentDto = classStudentService.getStudentWithClassDetails(classStudentFound.getId());

            if (classStudentDto != null) {
                ClassFeeCriteria classFeeCriteria = new ClassFeeCriteria();
                LongFilter classIdFilter = new LongFilter();
                classIdFilter.setEquals(classStudentDto.getSchoolClass().getId());
                classFeeCriteria.setSchoolClassId(classIdFilter);

                List<ClassFeeDTO> classFees = classFeeQueryService.findByCriteria(classFeeCriteria);

                LongFilter studentIdFilter = new LongFilter();
                studentIdFilter.setEquals(classStudentDto.getId());

                StudentAdditionalChargesCriteria studentAddChgCriteria = new StudentAdditionalChargesCriteria();
                studentAddChgCriteria.setClassStudentId(studentIdFilter);
                List<StudentAdditionalChargesDTO> studentAddCharges = studentAdditionalChargesQueryService.findByCriteria(
                    studentAddChgCriteria
                );

                StudentDiscountCriteria studentDiscCriteria = new StudentDiscountCriteria();
                studentDiscCriteria.setClassStudentId(studentIdFilter);
                List<StudentDiscountDTO> studentDiscounts = studentDiscountQueryService.findByCriteria(studentDiscCriteria);

                StudentPaymentsCriteria studentPaymentCriteria = new StudentPaymentsCriteria();
                studentPaymentCriteria.setClassStudentId(studentIdFilter);
                List<StudentPaymentsDTO> studentPayments = studentPaymentsQueryService.findByCriteria(studentPaymentCriteria);

                MonthWiseDetails totalDebits = new MonthWiseDetails();

                addClassFees(studentChargesSummaries, classStudentDto, classFees, totalDebits);
                addAdditionalCharges(studentChargesSummaries, studentAddCharges, totalDebits);
                addDiscountInformation(studentChargesSummaries, classStudentDto, studentDiscounts, totalDebits);

                MonthWiseDetails monthWisePayments = new MonthWiseDetails();
                Double totalPayments = 0.0;
                List<StudentChargesSummaryDTO> studentPaymentDetails = new ArrayList<>();
                totalPayments = createMonthWisePaymentSummary(studentPayments, monthWisePayments, totalPayments, studentPaymentDetails);
                studentChargesSummaries.add(monthWisePayments.toStudentChargesSummaryDTO("Monthly Payments", classStudentDto));

                MonthWiseDetails closingBalance = createClosingBalanceWithTotalDebits(totalDebits);

                Double totalPaymentsOrig = totalPayments;

                adjustPaymentsAgainstTotalDebits(totalDebits, totalPayments, closingBalance);

                adjustClosingBalancesAfterPaymentAdjustment(closingBalance, totalPaymentsOrig);

                studentChargesSummaries.add(closingBalance.toStudentChargesSummaryDTO("Closing", classStudentDto));
                studentChargesSummaries.addAll(studentPaymentDetails);
                onlyClosingSummaries.add(closingBalance.toStudentChargesSummaryDTO("Closing", classStudentDto));

                createOpeningBalance(studentChargesSummaries, classStudentDto, closingBalance);
            }
        }
        if (onlyClosingBalance) {
            return onlyClosingSummaries;
        } else {
            return studentChargesSummaries;
        }
    }

    private void createOpeningBalance(
        List<StudentChargesSummaryDTO> studentChargesSummaries,
        ClassStudentDTO classStudentDto,
        MonthWiseDetails closingBalance
    ) {
        MonthWiseDetails openingBalance = new MonthWiseDetails();
        openingBalance.setAprMonthTotal(0.0);
        openingBalance.setMayMonthTotal(closingBalance.getAprMonthTotal());
        openingBalance.setJunMonthTotal(closingBalance.getMayMonthTotal());
        openingBalance.setJulMonthTotal(closingBalance.getJunMonthTotal());
        openingBalance.setAugMonthTotal(closingBalance.getJulMonthTotal());
        openingBalance.setSepMonthTotal(closingBalance.getAugMonthTotal());
        openingBalance.setOctMonthTotal(closingBalance.getSepMonthTotal());
        openingBalance.setNovMonthTotal(closingBalance.getOctMonthTotal());
        openingBalance.setDecMonthTotal(closingBalance.getNovMonthTotal());
        openingBalance.setJanMonthTotal(closingBalance.getDecMonthTotal());
        openingBalance.setFebMonthTotal(closingBalance.getJanMonthTotal());
        openingBalance.setMarMonthTotal(closingBalance.getFebMonthTotal());
        studentChargesSummaries.add(0, openingBalance.toStudentChargesSummaryDTO("Opening", classStudentDto));
    }

    private Double createMonthWisePaymentSummary(
        List<StudentPaymentsDTO> studentPayments,
        MonthWiseDetails monthWisePayments,
        Double totalPayments,
        List<StudentChargesSummaryDTO> studentPaymentDetails
    ) {
        for (StudentPaymentsDTO studentPayment : studentPayments) {
            if (studentPayment.getPaymentDate() != null && studentPayment.getCancelDate() == null) {
                totalPayments += studentPayment.getAmountPaid();
                if (studentPayment.getPaymentDate().getMonthValue() == 4) {
                    monthWisePayments.setAprMonthTotal(monthWisePayments.getAprMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 5) {
                    monthWisePayments.setMayMonthTotal(monthWisePayments.getMayMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 6) {
                    monthWisePayments.setJunMonthTotal(monthWisePayments.getJunMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 7) {
                    monthWisePayments.setJulMonthTotal(monthWisePayments.getJulMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 8) {
                    monthWisePayments.setAugMonthTotal(monthWisePayments.getAugMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 9) {
                    monthWisePayments.setSepMonthTotal(monthWisePayments.getSepMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 10) {
                    monthWisePayments.setOctMonthTotal(monthWisePayments.getOctMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 11) {
                    monthWisePayments.setNovMonthTotal(monthWisePayments.getNovMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 12) {
                    monthWisePayments.setDecMonthTotal(monthWisePayments.getDecMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 1) {
                    monthWisePayments.setJanMonthTotal(monthWisePayments.getJanMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 2) {
                    monthWisePayments.setFebMonthTotal(monthWisePayments.getFebMonthTotal() + studentPayment.getAmountPaid());
                }
                if (studentPayment.getPaymentDate().getMonthValue() == 3) {
                    monthWisePayments.setMarMonthTotal(monthWisePayments.getMarMonthTotal() + studentPayment.getAmountPaid());
                }
                studentPaymentDetails.add(studentPayment.toStudentChargesSummaryDTO("Payments"));
            }
        }
        return totalPayments;
    }

    private void addClassFees(
        List<StudentChargesSummaryDTO> studentChargesSummaries,
        ClassStudentDTO classStudentDto,
        List<ClassFeeDTO> classFees,
        MonthWiseDetails totalDebits
    ) {
        for (ClassFeeDTO classFee : classFees) {
            totalDebits.setAprMonthTotal(totalDebits.getAprMonthTotal() + (classFee.getAprFee() == null ? 0.0 : classFee.getAprFee()));
            totalDebits.setMayMonthTotal(totalDebits.getMayMonthTotal() + (classFee.getMayFee() == null ? 0.0 : classFee.getMayFee()));
            totalDebits.setJunMonthTotal(totalDebits.getJunMonthTotal() + (classFee.getJunFee() == null ? 0.0 : classFee.getJunFee()));
            totalDebits.setJulMonthTotal(totalDebits.getJulMonthTotal() + (classFee.getJulFee() == null ? 0.0 : classFee.getJulFee()));
            totalDebits.setAugMonthTotal(totalDebits.getAugMonthTotal() + (classFee.getAugFee() == null ? 0.0 : classFee.getAugFee()));
            totalDebits.setSepMonthTotal(totalDebits.getSepMonthTotal() + (classFee.getSepFee() == null ? 0.0 : classFee.getSepFee()));
            totalDebits.setOctMonthTotal(totalDebits.getOctMonthTotal() + (classFee.getOctFee() == null ? 0.0 : classFee.getOctFee()));
            totalDebits.setNovMonthTotal(totalDebits.getNovMonthTotal() + (classFee.getNovFee() == null ? 0.0 : classFee.getNovFee()));
            totalDebits.setDecMonthTotal(totalDebits.getDecMonthTotal() + (classFee.getDecFee() == null ? 0.0 : classFee.getDecFee()));
            totalDebits.setJanMonthTotal(totalDebits.getJanMonthTotal() + (classFee.getJanFee() == null ? 0.0 : classFee.getJanFee()));
            totalDebits.setFebMonthTotal(totalDebits.getFebMonthTotal() + (classFee.getFebFee() == null ? 0.0 : classFee.getFebFee()));
            totalDebits.setMarMonthTotal(totalDebits.getMarMonthTotal() + (classFee.getMarFee() == null ? 0.0 : classFee.getMarFee()));

            StudentChargesSummaryDTO studentChargesSummaryDto = classFee.toStudentChargesSummaryDTO(
                getLedgerHeadName(classFee.getSchoolLedgerHead().getId())
            );
            //Set the student detail in case of the class fee which does not have a student detail
            studentChargesSummaryDto.setClassStudent(classStudentDto);
            studentChargesSummaries.add(studentChargesSummaryDto);
        }
    }

    private void addAdditionalCharges(
        List<StudentChargesSummaryDTO> studentChargesSummaries,
        List<StudentAdditionalChargesDTO> studentAddCharges,
        MonthWiseDetails totalDebits
    ) {
        for (StudentAdditionalChargesDTO studentAddCharge : studentAddCharges) {
            totalDebits.setAprMonthTotal(
                totalDebits.getAprMonthTotal() + (studentAddCharge.getAprAddChrg() == null ? 0.0 : studentAddCharge.getAprAddChrg())
            );
            totalDebits.setMayMonthTotal(
                totalDebits.getMayMonthTotal() + (studentAddCharge.getAprAddChrg() == null ? 0.0 : studentAddCharge.getAprAddChrg())
            );
            totalDebits.setJunMonthTotal(
                totalDebits.getJunMonthTotal() + (studentAddCharge.getMayAddChrg() == null ? 0.0 : studentAddCharge.getMayAddChrg())
            );
            totalDebits.setJulMonthTotal(
                totalDebits.getJulMonthTotal() + (studentAddCharge.getJunAddChrg() == null ? 0.0 : studentAddCharge.getJunAddChrg())
            );
            totalDebits.setAugMonthTotal(
                totalDebits.getAugMonthTotal() + (studentAddCharge.getJulAddChrg() == null ? 0.0 : studentAddCharge.getJulAddChrg())
            );
            totalDebits.setSepMonthTotal(
                totalDebits.getSepMonthTotal() + (studentAddCharge.getAugAddChrg() == null ? 0.0 : studentAddCharge.getAugAddChrg())
            );
            totalDebits.setOctMonthTotal(
                totalDebits.getOctMonthTotal() + (studentAddCharge.getSepAddChrg() == null ? 0.0 : studentAddCharge.getSepAddChrg())
            );
            totalDebits.setNovMonthTotal(
                totalDebits.getNovMonthTotal() + (studentAddCharge.getOctAddChrg() == null ? 0.0 : studentAddCharge.getOctAddChrg())
            );
            totalDebits.setDecMonthTotal(
                totalDebits.getDecMonthTotal() + (studentAddCharge.getNovAddChrg() == null ? 0.0 : studentAddCharge.getNovAddChrg())
            );
            totalDebits.setJanMonthTotal(
                totalDebits.getJanMonthTotal() + (studentAddCharge.getDecAddChrg() == null ? 0.0 : studentAddCharge.getDecAddChrg())
            );
            totalDebits.setFebMonthTotal(
                totalDebits.getFebMonthTotal() + (studentAddCharge.getJanAddChrg() == null ? 0.0 : studentAddCharge.getJanAddChrg())
            );
            totalDebits.setMarMonthTotal(
                totalDebits.getMarMonthTotal() + (studentAddCharge.getFebAddChrgc() == null ? 0.0 : studentAddCharge.getFebAddChrgc())
            );
            studentChargesSummaries.add(
                studentAddCharge.toStudentChargesSummaryDTO(getLedgerHeadName(studentAddCharge.getSchoolLedgerHead().getId()))
            );
        }
    }

    private void addDiscountInformation(
        List<StudentChargesSummaryDTO> studentChargesSummaries,
        ClassStudentDTO classStudentDto,
        List<StudentDiscountDTO> studentDiscounts,
        MonthWiseDetails totalDebits
    ) {
        for (StudentDiscountDTO studentDiscount : studentDiscounts) {
            totalDebits.setAprMonthTotal(
                totalDebits.getAprMonthTotal() - (studentDiscount.getAprFeeDisc() == null ? 0.0 : studentDiscount.getAprFeeDisc())
            );
            totalDebits.setMayMonthTotal(
                totalDebits.getMayMonthTotal() - (studentDiscount.getMayFeeDisc() == null ? 0.0 : studentDiscount.getMayFeeDisc())
            );
            totalDebits.setJunMonthTotal(
                totalDebits.getJunMonthTotal() - (studentDiscount.getJunFeeDisc() == null ? 0.0 : studentDiscount.getJunFeeDisc())
            );
            totalDebits.setJulMonthTotal(
                totalDebits.getJulMonthTotal() - (studentDiscount.getJulFeeDisc() == null ? 0.0 : studentDiscount.getJulFeeDisc())
            );
            totalDebits.setAugMonthTotal(
                totalDebits.getAugMonthTotal() - (studentDiscount.getAugFeeDisc() == null ? 0.0 : studentDiscount.getAugFeeDisc())
            );
            totalDebits.setSepMonthTotal(
                totalDebits.getSepMonthTotal() - (studentDiscount.getSepFeeDisc() == null ? 0.0 : studentDiscount.getSepFeeDisc())
            );
            totalDebits.setOctMonthTotal(
                totalDebits.getOctMonthTotal() - (studentDiscount.getOctFeeDisc() == null ? 0.0 : studentDiscount.getOctFeeDisc())
            );
            totalDebits.setNovMonthTotal(
                totalDebits.getNovMonthTotal() - (studentDiscount.getNovFeeDisc() == null ? 0.0 : studentDiscount.getNovFeeDisc())
            );
            totalDebits.setDecMonthTotal(
                totalDebits.getDecMonthTotal() - (studentDiscount.getDecFeeDisc() == null ? 0.0 : studentDiscount.getDecFeeDisc())
            );
            totalDebits.setJanMonthTotal(
                totalDebits.getJanMonthTotal() - (studentDiscount.getJanFeeDisc() == null ? 0.0 : studentDiscount.getJanFeeDisc())
            );
            totalDebits.setFebMonthTotal(
                totalDebits.getFebMonthTotal() - (studentDiscount.getFebFeeDisc() == null ? 0.0 : studentDiscount.getFebFeeDisc())
            );
            totalDebits.setMarMonthTotal(
                totalDebits.getMarMonthTotal() - (studentDiscount.getMarFeeDisc() == null ? 0.0 : studentDiscount.getMarFeeDisc())
            );
            studentChargesSummaries.add(
                studentDiscount.toStudentChargesSummaryDTO(getLedgerHeadName(studentDiscount.getSchoolLedgerHead().getId()))
            );
        }
        studentChargesSummaries.add(totalDebits.toStudentChargesSummaryDTO("Monthly Total Debit", classStudentDto));
    }

    private MonthWiseDetails createClosingBalanceWithTotalDebits(MonthWiseDetails totalDebits) {
        MonthWiseDetails closingBalance = new MonthWiseDetails();
        closingBalance.setAprMonthTotal(totalDebits.getAprMonthTotal());
        closingBalance.setMayMonthTotal(closingBalance.getAprMonthTotal() + totalDebits.getMayMonthTotal());
        closingBalance.setJunMonthTotal(closingBalance.getMayMonthTotal() + totalDebits.getJunMonthTotal());
        closingBalance.setJulMonthTotal(closingBalance.getJunMonthTotal() + totalDebits.getJulMonthTotal());
        closingBalance.setAugMonthTotal(closingBalance.getJulMonthTotal() + totalDebits.getAugMonthTotal());
        closingBalance.setSepMonthTotal(closingBalance.getAugMonthTotal() + totalDebits.getSepMonthTotal());
        closingBalance.setOctMonthTotal(closingBalance.getSepMonthTotal() + totalDebits.getOctMonthTotal());
        closingBalance.setNovMonthTotal(closingBalance.getOctMonthTotal() + totalDebits.getNovMonthTotal());
        closingBalance.setDecMonthTotal(closingBalance.getNovMonthTotal() + totalDebits.getDecMonthTotal());
        closingBalance.setJanMonthTotal(closingBalance.getDecMonthTotal() + totalDebits.getJanMonthTotal());
        closingBalance.setFebMonthTotal(closingBalance.getJanMonthTotal() + totalDebits.getFebMonthTotal());
        closingBalance.setMarMonthTotal(closingBalance.getFebMonthTotal() + totalDebits.getMarMonthTotal());
        return closingBalance;
    }

    private void adjustClosingBalancesAfterPaymentAdjustment(MonthWiseDetails closingBalance, Double totalPaymentsOrig) {
        closingBalance.setAprMonthTotal(
            closingBalance.getAprMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getAprMonthTotal() - totalPaymentsOrig
                : closingBalance.getAprMonthTotal()
        );
        closingBalance.setMayMonthTotal(
            closingBalance.getMayMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getMayMonthTotal() - totalPaymentsOrig
                : closingBalance.getMayMonthTotal()
        );
        closingBalance.setJunMonthTotal(
            closingBalance.getJunMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getJulMonthTotal() - totalPaymentsOrig
                : closingBalance.getJunMonthTotal()
        );
        closingBalance.setJulMonthTotal(
            closingBalance.getJulMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getJulMonthTotal() - totalPaymentsOrig
                : closingBalance.getJulMonthTotal()
        );
        closingBalance.setAugMonthTotal(
            closingBalance.getAugMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getAugMonthTotal() - totalPaymentsOrig
                : closingBalance.getAugMonthTotal()
        );
        closingBalance.setSepMonthTotal(
            closingBalance.getSepMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getSepMonthTotal() - totalPaymentsOrig
                : closingBalance.getSepMonthTotal()
        );
        closingBalance.setOctMonthTotal(
            closingBalance.getOctMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getOctMonthTotal() - totalPaymentsOrig
                : closingBalance.getOctMonthTotal()
        );
        closingBalance.setNovMonthTotal(
            closingBalance.getNovMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getNovMonthTotal() - totalPaymentsOrig
                : closingBalance.getNovMonthTotal()
        );
        closingBalance.setDecMonthTotal(
            closingBalance.getDecMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getDecMonthTotal() - totalPaymentsOrig
                : closingBalance.getDecMonthTotal()
        );
        closingBalance.setJanMonthTotal(
            closingBalance.getJanMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getJanMonthTotal() - totalPaymentsOrig
                : closingBalance.getJanMonthTotal()
        );
        closingBalance.setFebMonthTotal(
            closingBalance.getFebMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getFebMonthTotal() - totalPaymentsOrig
                : closingBalance.getFebMonthTotal()
        );
        closingBalance.setMarMonthTotal(
            closingBalance.getMarMonthTotal() >= totalPaymentsOrig
                ? closingBalance.getMarMonthTotal() - totalPaymentsOrig
                : closingBalance.getMarMonthTotal()
        );
    }

    private void adjustPaymentsAgainstTotalDebits(MonthWiseDetails totalDebits, Double totalPayments, MonthWiseDetails closingBalance) {
        if (totalPayments > 0) {
            if (totalDebits.getAprMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getAprMonthTotal();
                closingBalance.setAprMonthTotal(0.0);
            } else {
                closingBalance.setAprMonthTotal(totalDebits.getAprMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getMayMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getMayMonthTotal();
                closingBalance.setMayMonthTotal(0.0);
            } else {
                closingBalance.setMayMonthTotal(totalDebits.getMayMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getJunMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getJunMonthTotal();
                closingBalance.setJunMonthTotal(0.0);
            } else {
                closingBalance.setJunMonthTotal(totalPayments - totalDebits.getJunMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getJulMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getJulMonthTotal();
                closingBalance.setJulMonthTotal(0.0);
            } else {
                closingBalance.setJulMonthTotal(totalDebits.getJulMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getAugMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getAugMonthTotal();
                closingBalance.setAugMonthTotal(0.0);
            } else {
                closingBalance.setAugMonthTotal(totalDebits.getAugMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getSepMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getSepMonthTotal();
                closingBalance.setSepMonthTotal(0.0);
            } else {
                closingBalance.setSepMonthTotal(totalDebits.getSepMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getOctMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getOctMonthTotal();
                closingBalance.setOctMonthTotal(0.0);
            } else {
                closingBalance.setOctMonthTotal(totalDebits.getOctMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getNovMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getNovMonthTotal();
                closingBalance.setNovMonthTotal(0.0);
            } else {
                closingBalance.setNovMonthTotal(totalDebits.getNovMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getDecMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getDecMonthTotal();
                closingBalance.setDecMonthTotal(0.0);
            } else {
                closingBalance.setDecMonthTotal(totalDebits.getDecMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getJanMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getJanMonthTotal();
                closingBalance.setJanMonthTotal(0.0);
            } else {
                closingBalance.setJanMonthTotal(totalDebits.getJanMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getFebMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getFebMonthTotal();
                closingBalance.setFebMonthTotal(0.0);
            } else {
                closingBalance.setFebMonthTotal(totalDebits.getFebMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
        if (totalPayments > 0) {
            if (totalDebits.getMarMonthTotal() <= totalPayments) {
                totalPayments = totalPayments - totalDebits.getMarMonthTotal();
                closingBalance.setMarMonthTotal(0.0);
            } else {
                closingBalance.setMarMonthTotal(totalDebits.getMarMonthTotal() - totalPayments);
                totalPayments = 0.0;
            }
        }
    }

    private String getLedgerHeadName(Long id) {
        String ledgerHeadName = "NotFOund";
        if (id != null);
        Optional<SchoolLedgerHead> ledgerHead = schoolLedgerHeadRepo.findById(id);
        if (ledgerHead.isPresent()) {
            ledgerHeadName = ledgerHead.get().getLedgerHeadName();
        }
        return ledgerHeadName;
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentChargesSummaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentChargesSummary> specification = createSpecification(criteria);
        return studentChargesSummaryRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentChargesSummaryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentChargesSummary> createSpecification(StudentChargesSummaryCriteria criteria) {
        Specification<StudentChargesSummary> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentChargesSummary_.id));
            }
            if (criteria.getSummaryType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummaryType(), StudentChargesSummary_.summaryType));
            }
            if (criteria.getFeeYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeeYear(), StudentChargesSummary_.feeYear));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), StudentChargesSummary_.dueDate));
            }
            if (criteria.getAprSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAprSummary(), StudentChargesSummary_.aprSummary));
            }
            if (criteria.getMaySummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaySummary(), StudentChargesSummary_.maySummary));
            }
            if (criteria.getJunSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJunSummary(), StudentChargesSummary_.junSummary));
            }
            if (criteria.getJulSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJulSummary(), StudentChargesSummary_.julSummary));
            }
            if (criteria.getAugSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAugSummary(), StudentChargesSummary_.augSummary));
            }
            if (criteria.getSepSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSepSummary(), StudentChargesSummary_.sepSummary));
            }
            if (criteria.getOctSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOctSummary(), StudentChargesSummary_.octSummary));
            }
            if (criteria.getNovSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNovSummary(), StudentChargesSummary_.novSummary));
            }
            if (criteria.getDecSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDecSummary(), StudentChargesSummary_.decSummary));
            }
            if (criteria.getJanSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJanSummary(), StudentChargesSummary_.janSummary));
            }
            if (criteria.getFebSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFebSummary(), StudentChargesSummary_.febSummary));
            }
            if (criteria.getMarSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarSummary(), StudentChargesSummary_.marSummary));
            }
            if (criteria.getAdditionalInfo1() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdditionalInfo1(), StudentChargesSummary_.additionalInfo1));
            }
            if (criteria.getAdditionalInfo2() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdditionalInfo2(), StudentChargesSummary_.additionalInfo2));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), StudentChargesSummary_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), StudentChargesSummary_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), StudentChargesSummary_.cancelDate));
            }
            if (criteria.getSchoolLedgerHeadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolLedgerHeadId(),
                            root -> root.join(StudentChargesSummary_.schoolLedgerHead, JoinType.LEFT).get(SchoolLedgerHead_.id)
                        )
                    );
            }
            if (criteria.getClassStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassStudentId(),
                            root -> root.join(StudentChargesSummary_.classStudent, JoinType.LEFT).get(ClassStudent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
