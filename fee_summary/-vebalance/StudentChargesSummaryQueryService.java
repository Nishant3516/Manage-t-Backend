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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
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
    private final ClassStudentMapper classStudentMapper;
    private final SchoolLedgerHeadRepository schoolLedgerHeadRepo;

    public StudentChargesSummaryQueryService(
        StudentChargesSummaryRepository studentChargesSummaryRepository,
        StudentChargesSummaryMapper studentChargesSummaryMapper,
        ClassStudentRepository classStudentRepository,
        ClassFeeQueryService classFeeQueryServide,
        StudentDiscountQueryService studentDiscountQueryService,
        StudentAdditionalChargesQueryService studentAdditionalChargesQueryService,
        StudentPaymentsQueryService studentPaymentsQueryService,
        SchoolLedgerHeadRepository schoolLedgerHeadRepo,
        ClassStudentMapper classStudentMapper
    ) {
        this.studentChargesSummaryRepository = studentChargesSummaryRepository;
        this.studentChargesSummaryMapper = studentChargesSummaryMapper;
        this.classStudentRepository = classStudentRepository;
        this.classFeeQueryService = classFeeQueryServide;
        this.studentDiscountQueryService = studentDiscountQueryService;
        this.studentAdditionalChargesQueryService = studentAdditionalChargesQueryService;
        this.studentPaymentsQueryService = studentPaymentsQueryService;
        this.schoolLedgerHeadRepo = schoolLedgerHeadRepo;
        this.classStudentMapper = classStudentMapper;
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
            ClassStudent classStudent = classStudentRepository.findByStudentId(studentId);

            if (classStudent != null) {
                ClassFeeCriteria classFeeCriteria = new ClassFeeCriteria();
                LongFilter classIdFilter = new LongFilter();
                classIdFilter.setEquals(classStudent.getSchoolClass().getId());
                classFeeCriteria.setSchoolClassId(classIdFilter);
                List<ClassFeeDTO> classFees = classFeeQueryService.findByCriteria(classFeeCriteria);

                LongFilter studentIdFilter = new LongFilter();
                studentIdFilter.setEquals(classStudent.getId());

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

                MonthWiseDetails totalCharges = new MonthWiseDetails();

                for (ClassFeeDTO classFee : classFees) {
                    totalCharges.setAprMonthTotal(
                        totalCharges.getAprMonthTotal() + (classFee.getAprFee() == null ? 0.0 : classFee.getAprFee())
                    );
                    totalCharges.setMayMonthTotal(
                        totalCharges.getMayMonthTotal() + (classFee.getMayFee() == null ? 0.0 : classFee.getMayFee())
                    );
                    totalCharges.setJunMonthTotal(
                        totalCharges.getJunMonthTotal() + (classFee.getJunFee() == null ? 0.0 : classFee.getJunFee())
                    );
                    totalCharges.setJulMonthTotal(
                        totalCharges.getJulMonthTotal() + (classFee.getJulFee() == null ? 0.0 : classFee.getJulFee())
                    );
                    totalCharges.setAugMonthTotal(
                        totalCharges.getAugMonthTotal() + (classFee.getAugFee() == null ? 0.0 : classFee.getAugFee())
                    );
                    totalCharges.setSepMonthTotal(
                        totalCharges.getSepMonthTotal() + (classFee.getSepFee() == null ? 0.0 : classFee.getSepFee())
                    );
                    totalCharges.setOctMonthTotal(
                        totalCharges.getOctMonthTotal() + (classFee.getOctFee() == null ? 0.0 : classFee.getOctFee())
                    );
                    totalCharges.setNovMonthTotal(
                        totalCharges.getNovMonthTotal() + (classFee.getNovFee() == null ? 0.0 : classFee.getNovFee())
                    );
                    totalCharges.setDecMonthTotal(
                        totalCharges.getDecMonthTotal() + (classFee.getDecFee() == null ? 0.0 : classFee.getDecFee())
                    );
                    totalCharges.setJanMonthTotal(
                        totalCharges.getJanMonthTotal() + (classFee.getJanFee() == null ? 0.0 : classFee.getJanFee())
                    );
                    totalCharges.setFebMonthTotal(
                        totalCharges.getFebMonthTotal() + (classFee.getFebFee() == null ? 0.0 : classFee.getFebFee())
                    );
                    totalCharges.setMarMonthTotal(
                        totalCharges.getMarMonthTotal() + (classFee.getMarFee() == null ? 0.0 : classFee.getMarFee())
                    );

                    StudentChargesSummaryDTO studentChargesSummaryDto = classFee.toStudentChargesSummaryDTO(
                        getLedgerHeadName(classFee.getSchoolLedgerHead().getId())
                    );
                    //Set the student detail in case of the class fee which does not have a student detail
                    studentChargesSummaryDto.setClassStudent(classStudentMapper.toDto(classStudent));
                    studentChargesSummaries.add(studentChargesSummaryDto);
                }
                for (StudentAdditionalChargesDTO studentAddCharge : studentAddCharges) {
                    totalCharges.setAprMonthTotal(
                        totalCharges.getAprMonthTotal() +
                        (studentAddCharge.getAprAddChrg() == null ? 0.0 : studentAddCharge.getAprAddChrg())
                    );
                    totalCharges.setMayMonthTotal(
                        totalCharges.getMayMonthTotal() +
                        (studentAddCharge.getAprAddChrg() == null ? 0.0 : studentAddCharge.getAprAddChrg())
                    );
                    totalCharges.setJunMonthTotal(
                        totalCharges.getJunMonthTotal() +
                        (studentAddCharge.getMayAddChrg() == null ? 0.0 : studentAddCharge.getMayAddChrg())
                    );
                    totalCharges.setJulMonthTotal(
                        totalCharges.getJulMonthTotal() +
                        (studentAddCharge.getJunAddChrg() == null ? 0.0 : studentAddCharge.getJunAddChrg())
                    );
                    totalCharges.setAugMonthTotal(
                        totalCharges.getAugMonthTotal() +
                        (studentAddCharge.getJulAddChrg() == null ? 0.0 : studentAddCharge.getJulAddChrg())
                    );
                    totalCharges.setSepMonthTotal(
                        totalCharges.getSepMonthTotal() +
                        (studentAddCharge.getAugAddChrg() == null ? 0.0 : studentAddCharge.getAugAddChrg())
                    );
                    totalCharges.setOctMonthTotal(
                        totalCharges.getOctMonthTotal() +
                        (studentAddCharge.getSepAddChrg() == null ? 0.0 : studentAddCharge.getSepAddChrg())
                    );
                    totalCharges.setNovMonthTotal(
                        totalCharges.getNovMonthTotal() +
                        (studentAddCharge.getOctAddChrg() == null ? 0.0 : studentAddCharge.getOctAddChrg())
                    );
                    totalCharges.setDecMonthTotal(
                        totalCharges.getDecMonthTotal() +
                        (studentAddCharge.getNovAddChrg() == null ? 0.0 : studentAddCharge.getNovAddChrg())
                    );
                    totalCharges.setJanMonthTotal(
                        totalCharges.getJanMonthTotal() +
                        (studentAddCharge.getDecAddChrg() == null ? 0.0 : studentAddCharge.getDecAddChrg())
                    );
                    totalCharges.setFebMonthTotal(
                        totalCharges.getFebMonthTotal() +
                        (studentAddCharge.getJanAddChrg() == null ? 0.0 : studentAddCharge.getJanAddChrg())
                    );
                    totalCharges.setMarMonthTotal(
                        totalCharges.getMarMonthTotal() +
                        (studentAddCharge.getFebAddChrgc() == null ? 0.0 : studentAddCharge.getFebAddChrgc())
                    );
                    studentChargesSummaries.add(
                        studentAddCharge.toStudentChargesSummaryDTO(getLedgerHeadName(studentAddCharge.getSchoolLedgerHead().getId()))
                    );
                }
                for (StudentDiscountDTO studentDiscount : studentDiscounts) {
                    totalCharges.setAprMonthTotal(
                        totalCharges.getAprMonthTotal() - (studentDiscount.getAprFeeDisc() == null ? 0.0 : studentDiscount.getAprFeeDisc())
                    );
                    totalCharges.setMayMonthTotal(
                        totalCharges.getMayMonthTotal() - (studentDiscount.getMayFeeDisc() == null ? 0.0 : studentDiscount.getMayFeeDisc())
                    );
                    totalCharges.setJunMonthTotal(
                        totalCharges.getJunMonthTotal() - (studentDiscount.getJunFeeDisc() == null ? 0.0 : studentDiscount.getJunFeeDisc())
                    );
                    totalCharges.setJulMonthTotal(
                        totalCharges.getJulMonthTotal() - (studentDiscount.getJulFeeDisc() == null ? 0.0 : studentDiscount.getJulFeeDisc())
                    );
                    totalCharges.setAugMonthTotal(
                        totalCharges.getAugMonthTotal() - (studentDiscount.getAugFeeDisc() == null ? 0.0 : studentDiscount.getAugFeeDisc())
                    );
                    totalCharges.setSepMonthTotal(
                        totalCharges.getSepMonthTotal() - (studentDiscount.getSepFeeDisc() == null ? 0.0 : studentDiscount.getSepFeeDisc())
                    );
                    totalCharges.setOctMonthTotal(
                        totalCharges.getOctMonthTotal() - (studentDiscount.getOctFeeDisc() == null ? 0.0 : studentDiscount.getOctFeeDisc())
                    );
                    totalCharges.setNovMonthTotal(
                        totalCharges.getNovMonthTotal() - (studentDiscount.getNovFeeDisc() == null ? 0.0 : studentDiscount.getNovFeeDisc())
                    );
                    totalCharges.setDecMonthTotal(
                        totalCharges.getDecMonthTotal() - (studentDiscount.getDecFeeDisc() == null ? 0.0 : studentDiscount.getDecFeeDisc())
                    );
                    totalCharges.setJanMonthTotal(
                        totalCharges.getJanMonthTotal() - (studentDiscount.getJanFeeDisc() == null ? 0.0 : studentDiscount.getJanFeeDisc())
                    );
                    totalCharges.setFebMonthTotal(
                        totalCharges.getFebMonthTotal() - (studentDiscount.getFebFeeDisc() == null ? 0.0 : studentDiscount.getFebFeeDisc())
                    );
                    totalCharges.setMarMonthTotal(
                        totalCharges.getMarMonthTotal() - (studentDiscount.getMarFeeDisc() == null ? 0.0 : studentDiscount.getMarFeeDisc())
                    );
                    studentChargesSummaries.add(
                        studentDiscount.toStudentChargesSummaryDTO(getLedgerHeadName(studentDiscount.getSchoolLedgerHead().getId()))
                    );
                }
                MonthWiseDetails cumulativePayments = new MonthWiseDetails();
                for (StudentPaymentsDTO studentPayment : studentPayments) {
                    if (studentPayment.getPaymentDate() != null && studentPayment.getCancelDate() == null) {
                        if (studentPayment.getPaymentDate().getMonthValue() == 4) {
                            totalCharges.setAprMonthTotal(totalCharges.getAprMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setAprMonthTotal(cumulativePayments.getAprMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 5) {
                            totalCharges.setMayMonthTotal(totalCharges.getMayMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setMayMonthTotal(cumulativePayments.getMayMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 6) {
                            totalCharges.setJunMonthTotal(totalCharges.getJunMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setJunMonthTotal(cumulativePayments.getJunMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 7) {
                            totalCharges.setJulMonthTotal(totalCharges.getJulMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setJulMonthTotal(cumulativePayments.getJulMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 8) {
                            totalCharges.setAugMonthTotal(totalCharges.getAugMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setAugMonthTotal(cumulativePayments.getAugMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 9) {
                            totalCharges.setSepMonthTotal(totalCharges.getSepMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setSepMonthTotal(cumulativePayments.getSepMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 10) {
                            totalCharges.setOctMonthTotal(totalCharges.getOctMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setOctMonthTotal(cumulativePayments.getOctMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 11) {
                            totalCharges.setNovMonthTotal(totalCharges.getNovMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setNovMonthTotal(cumulativePayments.getNovMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 12) {
                            totalCharges.setDecMonthTotal(totalCharges.getDecMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setDecMonthTotal(cumulativePayments.getDecMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 1) {
                            totalCharges.setJanMonthTotal(totalCharges.getJanMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setJanMonthTotal(cumulativePayments.getJanMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 2) {
                            totalCharges.setFebMonthTotal(totalCharges.getFebMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setFebMonthTotal(cumulativePayments.getFebMonthTotal() + studentPayment.getAmountPaid());
                        }
                        if (studentPayment.getPaymentDate().getMonthValue() == 3) {
                            totalCharges.setMarMonthTotal(totalCharges.getMarMonthTotal() - studentPayment.getAmountPaid());
                            cumulativePayments.setMarMonthTotal(cumulativePayments.getMarMonthTotal() + studentPayment.getAmountPaid());
                        }
                        studentChargesSummaries.add(studentPayment.toStudentChargesSummaryDTO("Payments"));
                    }
                }

                //studentChargesSummaries.add(totalCharges.toStudentChargesSummaryDTO("Monthly Summary "));
                MonthWiseDetails closingBalance = new MonthWiseDetails();
                closingBalance.setAprMonthTotal(totalCharges.getAprMonthTotal());
                closingBalance.setMayMonthTotal(closingBalance.getAprMonthTotal() + totalCharges.getMayMonthTotal());
                closingBalance.setJunMonthTotal(closingBalance.getMayMonthTotal() + totalCharges.getJunMonthTotal());
                closingBalance.setJulMonthTotal(closingBalance.getJunMonthTotal() + totalCharges.getJulMonthTotal());
                closingBalance.setAugMonthTotal(closingBalance.getJulMonthTotal() + totalCharges.getAugMonthTotal());
                closingBalance.setSepMonthTotal(closingBalance.getAugMonthTotal() + totalCharges.getSepMonthTotal());
                closingBalance.setOctMonthTotal(closingBalance.getSepMonthTotal() + totalCharges.getOctMonthTotal());
                closingBalance.setNovMonthTotal(closingBalance.getOctMonthTotal() + totalCharges.getNovMonthTotal());
                closingBalance.setDecMonthTotal(closingBalance.getNovMonthTotal() + totalCharges.getDecMonthTotal());
                closingBalance.setJanMonthTotal(closingBalance.getDecMonthTotal() + totalCharges.getJanMonthTotal());
                closingBalance.setFebMonthTotal(closingBalance.getJanMonthTotal() + totalCharges.getFebMonthTotal());
                closingBalance.setMarMonthTotal(closingBalance.getFebMonthTotal() + totalCharges.getMarMonthTotal());

                //                if(closingBalance.getAprMonthTotal()>0&&closingBalance.getAprMonthTotal()>=closingBalance.getMayMonthTotal()) {
                //                	closingBalance.setAprMonthTotal(0.0);
                //                }
                //                if(closingBalance.getMayMonthTotal()>0&&closingBalance.getMayMonthTotal()>=closingBalance.getJunMonthTotal()) {
                //                	closingBalance.setMayMonthTotal(0.0);
                //                }
                //                if(closingBalance.getJunMonthTotal()>0&&closingBalance.getJunMonthTotal()>=closingBalance.getJulMonthTotal()) {
                //                	closingBalance.setJunMonthTotal(0.0);
                //                }
                //                if(closingBalance.getJulMonthTotal()>0&&closingBalance.getJulMonthTotal()>=closingBalance.getAugMonthTotal()) {
                //                	closingBalance.setJulMonthTotal(0.0);
                //                }
                //                if(closingBalance.getAugMonthTotal()>0&&closingBalance.getAugMonthTotal()>=closingBalance.getSepMonthTotal()) {
                //                	closingBalance.setAugMonthTotal(0.0);
                //                }
                //                if(closingBalance.getSepMonthTotal()>0&&closingBalance.getSepMonthTotal()>=closingBalance.getOctMonthTotal()) {
                //                	closingBalance.setSepMonthTotal(0.0);
                //                }
                //                if(closingBalance.getOctMonthTotal()>0&&closingBalance.getOctMonthTotal()>=closingBalance.getNovMonthTotal()) {
                //                	closingBalance.setOctMonthTotal(0.0);
                //                }
                //                if(closingBalance.getNovMonthTotal()>0&&closingBalance.getNovMonthTotal()>=closingBalance.getDecMonthTotal()) {
                //                	closingBalance.setNovMonthTotal(0.0);
                //                }
                //                if(closingBalance.getDecMonthTotal()>0&&closingBalance.getDecMonthTotal()>=closingBalance.getJanMonthTotal()) {
                //                	closingBalance.setDecMonthTotal(0.0);
                //                }
                //                if(closingBalance.getJanMonthTotal()>0&&closingBalance.getJanMonthTotal()>=closingBalance.getFebMonthTotal()) {
                //                	closingBalance.setJanMonthTotal(0.0);
                //                }
                //                if(closingBalance.getFebMonthTotal()>0&&closingBalance.getFebMonthTotal()>=closingBalance.getMarMonthTotal()) {
                //                	closingBalance.setFebMonthTotal(0.0);
                //                }

                //              if(closingBalance.getAprMonthTotal()>0&&closingBalance.getAprMonthTotal()<=cumulativePayments.getMayMonthTotal()) {
                //            	closingBalance.setAprMonthTotal(0.0);
                //            }
                //            if(closingBalance.getMayMonthTotal()>0&&closingBalance.getMayMonthTotal()<=cumulativePayments.getJunMonthTotal()) {
                //            	closingBalance.setMayMonthTotal(0.0);
                //            }
                //            if(closingBalance.getJunMonthTotal()>0&&closingBalance.getJunMonthTotal()<=cumulativePayments.getJulMonthTotal()) {
                //            	closingBalance.setJunMonthTotal(0.0);
                //            }
                //            if(closingBalance.getJulMonthTotal()>0&&closingBalance.getJulMonthTotal()<=cumulativePayments.getAugMonthTotal()) {
                //            	closingBalance.setJulMonthTotal(0.0);
                //            }
                //            if(closingBalance.getAugMonthTotal()>0&&closingBalance.getAugMonthTotal()<=cumulativePayments.getSepMonthTotal()) {
                //            	closingBalance.setAugMonthTotal(0.0);
                //            }
                //            if(closingBalance.getSepMonthTotal()>0&&closingBalance.getSepMonthTotal()<=cumulativePayments.getOctMonthTotal()) {
                //            	closingBalance.setSepMonthTotal(0.0);
                //            }
                //            if(closingBalance.getOctMonthTotal()>0&&closingBalance.getOctMonthTotal()<=cumulativePayments.getNovMonthTotal()) {
                //            	closingBalance.setOctMonthTotal(0.0);
                //            }
                //            if(closingBalance.getNovMonthTotal()>0&&closingBalance.getNovMonthTotal()<=cumulativePayments.getDecMonthTotal()) {
                //            	closingBalance.setNovMonthTotal(0.0);
                //            }
                //            if(closingBalance.getDecMonthTotal()>0&&closingBalance.getDecMonthTotal()<=cumulativePayments.getJanMonthTotal()) {
                //            	closingBalance.setDecMonthTotal(0.0);
                //            }
                //            if(closingBalance.getJanMonthTotal()>0&&closingBalance.getJanMonthTotal()<=cumulativePayments.getFebMonthTotal()) {
                //            	closingBalance.setJanMonthTotal(0.0);
                //            }
                //            if(closingBalance.getFebMonthTotal()>0&&closingBalance.getFebMonthTotal()<=cumulativePayments.getMarMonthTotal()) {
                //            	closingBalance.setFebMonthTotal(0.0);
                //            }

                studentChargesSummaries.add(closingBalance.toStudentChargesSummaryDTO("Closing", classStudentMapper.toDto(classStudent)));
                onlyClosingSummaries.add(closingBalance.toStudentChargesSummaryDTO("Closing", classStudentMapper.toDto(classStudent)));

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
                studentChargesSummaries.add(
                    0,
                    openingBalance.toStudentChargesSummaryDTO("Opening", classStudentMapper.toDto(classStudent))
                );
            }
        }
        if (onlyClosingBalance) {
            return onlyClosingSummaries;
        } else {
            return studentChargesSummaries;
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
