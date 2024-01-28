package com.ssik.manageit.service;

//for static metamodels
import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.StudentPayments;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.repository.SchoolClassRepository;
import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.repository.StudentPaymentsRepository;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.service.mapper.StudentPaymentsMapper;
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
 * Service for executing complex queries for {@link StudentPayments} entities in the database.
 * The main input is a {@link StudentPaymentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentPaymentsDTO} or a {@link Page} of {@link StudentPaymentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentPaymentsQueryService extends QueryService<StudentPayments> {

    private final Logger log = LoggerFactory.getLogger(StudentPaymentsQueryService.class);

    private final StudentPaymentsRepository studentPaymentsRepository;

    private final StudentPaymentsMapper studentPaymentsMapper;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private ClassStudentService classStudentService;

    public StudentPaymentsQueryService(StudentPaymentsRepository studentPaymentsRepository, StudentPaymentsMapper studentPaymentsMapper) {
        this.studentPaymentsRepository = studentPaymentsRepository;
        this.studentPaymentsMapper = studentPaymentsMapper;
    }

    /**
     * Return a {@link List} of {@link StudentPaymentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentPaymentsDTO> findByCriteria(StudentPaymentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<StudentPayments> specification = createSpecification(criteria);
        List<StudentPaymentsDTO> studentsPaymentsDto = studentPaymentsMapper.toDto(studentPaymentsRepository.findAll(specification));

        for (StudentPaymentsDTO studentPayment : studentsPaymentsDto) {
            studentPayment.setClassStudent(classStudentService.getStudentWithClassDetails(studentPayment.getClassStudent().getId()));
        }
        return studentsPaymentsDto;
    }

    /**
     * Return a {@link Page} of {@link StudentPaymentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentPaymentsDTO> findByCriteria(StudentPaymentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentPayments> specification = createSpecification(criteria);
        return studentPaymentsRepository.findAll(specification, page).map(studentPaymentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentPaymentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentPayments> specification = createSpecification(criteria);
        return studentPaymentsRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentPaymentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentPayments> createSpecification(StudentPaymentsCriteria criteria) {
        Specification<StudentPayments> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentPayments_.id));
            }
            if (criteria.getAmountPaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountPaid(), StudentPayments_.amountPaid));
            }
            if (criteria.getModeOfPay() != null) {
                specification = specification.and(buildSpecification(criteria.getModeOfPay(), StudentPayments_.modeOfPay));
            }
            if (criteria.getNoteNumbers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoteNumbers(), StudentPayments_.noteNumbers));
            }
            if (criteria.getUpiId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpiId(), StudentPayments_.upiId));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), StudentPayments_.remarks));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), StudentPayments_.paymentDate));
            }
            if (criteria.getReceiptId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReceiptId(), StudentPayments_.receiptId));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), StudentPayments_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), StudentPayments_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), StudentPayments_.cancelDate));
            }
            if (criteria.getClassStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassStudentId(),
                            root -> root.join(StudentPayments_.classStudent, JoinType.LEFT).get(ClassStudent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
