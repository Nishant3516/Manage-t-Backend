package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
// for static metamodels
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.repository.StudentDiscountRepository;
import com.ssik.manageit.service.criteria.StudentDiscountCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.service.mapper.StudentDiscountMapper;
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

/**
 * Service for executing complex queries for {@link StudentDiscount} entities in the database.
 * The main input is a {@link StudentDiscountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentDiscountDTO} or a {@link Page} of {@link StudentDiscountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentDiscountQueryService extends QueryService<StudentDiscount> {

    private final Logger log = LoggerFactory.getLogger(StudentDiscountQueryService.class);

    private final StudentDiscountRepository studentDiscountRepository;

    private final StudentDiscountMapper studentDiscountMapper;
    private final SchoolLedgerHeadService schoolLedgerHeadService;

    @Autowired
    ClassStudentService classStudentService;

    public StudentDiscountQueryService(
        StudentDiscountRepository studentDiscountRepository,
        StudentDiscountMapper studentDiscountMapper,
        SchoolLedgerHeadService schoolLedgerHeadService
    ) {
        this.studentDiscountRepository = studentDiscountRepository;
        this.studentDiscountMapper = studentDiscountMapper;
        this.schoolLedgerHeadService = schoolLedgerHeadService;
    }

    /**
     * Return a {@link List} of {@link StudentDiscountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentDiscountDTO> findByCriteria(StudentDiscountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<StudentDiscount> specification = createSpecification(criteria);
        List<StudentDiscountDTO> studentDiscountDTOs = studentDiscountMapper.toDto(studentDiscountRepository.findAll(specification));
        for (StudentDiscountDTO studentDiscountDTO : studentDiscountDTOs) {
            if (studentDiscountDTO.getSchoolLedgerHead() != null) {
                studentDiscountDTO.setSchoolLedgerHead(
                    schoolLedgerHeadService.getOneIfExists(studentDiscountDTO.getSchoolLedgerHead().getId())
                );
            }
            if (studentDiscountDTO.getClassStudent() != null) {
                studentDiscountDTO.setClassStudent(
                    classStudentService.getStudentWithClassDetails(studentDiscountDTO.getClassStudent().getId())
                );
            }
        }
        return studentDiscountDTOs;
    }

    /**
     * Return a {@link Page} of {@link StudentDiscountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDiscountDTO> findByCriteria(StudentDiscountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentDiscount> specification = createSpecification(criteria);
        return studentDiscountRepository.findAll(specification, page).map(studentDiscountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentDiscountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentDiscount> specification = createSpecification(criteria);
        return studentDiscountRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentDiscountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentDiscount> createSpecification(StudentDiscountCriteria criteria) {
        Specification<StudentDiscount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentDiscount_.id));
            }
            if (criteria.getFeeYear() != null) {
                specification = specification.and(buildSpecification(criteria.getFeeYear(), StudentDiscount_.feeYear));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), StudentDiscount_.dueDate));
            }
            if (criteria.getJanFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJanFeeDisc(), StudentDiscount_.janFeeDisc));
            }
            if (criteria.getFebFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFebFeeDisc(), StudentDiscount_.febFeeDisc));
            }
            if (criteria.getMarFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarFeeDisc(), StudentDiscount_.marFeeDisc));
            }
            if (criteria.getAprFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAprFeeDisc(), StudentDiscount_.aprFeeDisc));
            }
            if (criteria.getMayFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMayFeeDisc(), StudentDiscount_.mayFeeDisc));
            }
            if (criteria.getJunFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJunFeeDisc(), StudentDiscount_.junFeeDisc));
            }
            if (criteria.getJulFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJulFeeDisc(), StudentDiscount_.julFeeDisc));
            }
            if (criteria.getAugFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAugFeeDisc(), StudentDiscount_.augFeeDisc));
            }
            if (criteria.getSepFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSepFeeDisc(), StudentDiscount_.sepFeeDisc));
            }
            if (criteria.getOctFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOctFeeDisc(), StudentDiscount_.octFeeDisc));
            }
            if (criteria.getNovFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNovFeeDisc(), StudentDiscount_.novFeeDisc));
            }
            if (criteria.getDecFeeDisc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecFeeDisc(), StudentDiscount_.decFeeDisc));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), StudentDiscount_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), StudentDiscount_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), StudentDiscount_.cancelDate));
            }
            if (criteria.getSchoolLedgerHeadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolLedgerHeadId(),
                            root -> root.join(StudentDiscount_.schoolLedgerHead, JoinType.LEFT).get(SchoolLedgerHead_.id)
                        )
                    );
            }
            if (criteria.getClassStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassStudentId(),
                            root -> root.join(StudentDiscount_.classStudent, JoinType.LEFT).get(ClassStudent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
