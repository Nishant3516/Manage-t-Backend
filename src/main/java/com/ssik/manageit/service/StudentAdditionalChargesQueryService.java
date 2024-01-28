package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.criteria.StudentAdditionalChargesCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.service.mapper.StudentAdditionalChargesMapper;
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
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link StudentAdditionalCharges} entities in the database.
 * The main input is a {@link StudentAdditionalChargesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentAdditionalChargesDTO} or a {@link Page} of {@link StudentAdditionalChargesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentAdditionalChargesQueryService extends QueryService<StudentAdditionalCharges> {

    private final Logger log = LoggerFactory.getLogger(StudentAdditionalChargesQueryService.class);

    private final StudentAdditionalChargesRepository studentAdditionalChargesRepository;

    private final StudentAdditionalChargesMapper studentAdditionalChargesMapper;
    private final SchoolLedgerHeadService schoolLedgerHeadService;

    private final ClassStudentService classStudentService;

    public StudentAdditionalChargesQueryService(
        StudentAdditionalChargesRepository studentAdditionalChargesRepository,
        StudentAdditionalChargesMapper studentAdditionalChargesMapper,
        SchoolLedgerHeadService schoolLedgerHeadService,
        ClassStudentService classStudentService
    ) {
        this.studentAdditionalChargesRepository = studentAdditionalChargesRepository;
        this.studentAdditionalChargesMapper = studentAdditionalChargesMapper;
        this.schoolLedgerHeadService = schoolLedgerHeadService;
        this.classStudentService = classStudentService;
    }

    /**
     * Return a {@link List} of {@link StudentAdditionalChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentAdditionalChargesDTO> findByCriteria(StudentAdditionalChargesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<StudentAdditionalCharges> specification = createSpecification(criteria);
        List<StudentAdditionalChargesDTO> studentAdditionalChargesDTOs = studentAdditionalChargesMapper.toDto(
            studentAdditionalChargesRepository.findAll(specification)
        );
        for (StudentAdditionalChargesDTO studentAdditionalChargesDTO : studentAdditionalChargesDTOs) {
            studentAdditionalChargesDTO.setSchoolLedgerHead(
                schoolLedgerHeadService.getOneIfExists(studentAdditionalChargesDTO.getSchoolLedgerHead().getId())
            );
            studentAdditionalChargesDTO.setClassStudent(
                classStudentService.getStudentWithClassDetails(studentAdditionalChargesDTO.getClassStudent().getId())
            );
        }
        return studentAdditionalChargesDTOs;
    }

    /**
     * Return a {@link Page} of {@link StudentAdditionalChargesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentAdditionalChargesDTO> findByCriteria(StudentAdditionalChargesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentAdditionalCharges> specification = createSpecification(criteria);
        return studentAdditionalChargesRepository.findAll(specification, page).map(studentAdditionalChargesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentAdditionalChargesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentAdditionalCharges> specification = createSpecification(criteria);
        return studentAdditionalChargesRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentAdditionalChargesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentAdditionalCharges> createSpecification(StudentAdditionalChargesCriteria criteria) {
        Specification<StudentAdditionalCharges> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentAdditionalCharges_.id));
            }
            if (criteria.getFeeYear() != null) {
                specification = specification.and(buildSpecification(criteria.getFeeYear(), StudentAdditionalCharges_.feeYear));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), StudentAdditionalCharges_.dueDate));
            }
            if (criteria.getJanAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJanAddChrg(), StudentAdditionalCharges_.janAddChrg));
            }
            if (criteria.getFebAddChrgc() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFebAddChrgc(), StudentAdditionalCharges_.febAddChrgc));
            }
            if (criteria.getMarAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarAddChrg(), StudentAdditionalCharges_.marAddChrg));
            }
            if (criteria.getAprAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAprAddChrg(), StudentAdditionalCharges_.aprAddChrg));
            }
            if (criteria.getMayAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMayAddChrg(), StudentAdditionalCharges_.mayAddChrg));
            }
            if (criteria.getJunAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJunAddChrg(), StudentAdditionalCharges_.junAddChrg));
            }
            if (criteria.getJulAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJulAddChrg(), StudentAdditionalCharges_.julAddChrg));
            }
            if (criteria.getAugAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAugAddChrg(), StudentAdditionalCharges_.augAddChrg));
            }
            if (criteria.getSepAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSepAddChrg(), StudentAdditionalCharges_.sepAddChrg));
            }
            if (criteria.getOctAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOctAddChrg(), StudentAdditionalCharges_.octAddChrg));
            }
            if (criteria.getNovAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNovAddChrg(), StudentAdditionalCharges_.novAddChrg));
            }
            if (criteria.getDecAddChrg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecAddChrg(), StudentAdditionalCharges_.decAddChrg));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), StudentAdditionalCharges_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModified(), StudentAdditionalCharges_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), StudentAdditionalCharges_.cancelDate));
            }
            if (criteria.getSchoolLedgerHeadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolLedgerHeadId(),
                            root -> root.join(StudentAdditionalCharges_.schoolLedgerHead, JoinType.LEFT).get(SchoolLedgerHead_.id)
                        )
                    );
            }
            if (criteria.getClassStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassStudentId(),
                            root -> root.join(StudentAdditionalCharges_.classStudent, JoinType.LEFT).get(ClassStudent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
