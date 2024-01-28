package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.repository.ClassFeeRepository;
import com.ssik.manageit.service.criteria.ClassFeeCriteria;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.mapper.ClassFeeMapper;
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
 * Service for executing complex queries for {@link ClassFee} entities in the database.
 * The main input is a {@link ClassFeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassFeeDTO} or a {@link Page} of {@link ClassFeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassFeeQueryService extends QueryService<ClassFee> {

    private final Logger log = LoggerFactory.getLogger(ClassFeeQueryService.class);

    private final ClassFeeRepository classFeeRepository;

    private final ClassFeeMapper classFeeMapper;
    private final SchoolLedgerHeadService schoolLedgerHeadService;

    public ClassFeeQueryService(
        ClassFeeRepository classFeeRepository,
        ClassFeeMapper classFeeMapper,
        SchoolLedgerHeadService schoolLedgerHeadService
    ) {
        this.classFeeRepository = classFeeRepository;
        this.classFeeMapper = classFeeMapper;
        this.schoolLedgerHeadService = schoolLedgerHeadService;
    }

    /**
     * Return a {@link List} of {@link ClassFeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassFeeDTO> findByCriteria(ClassFeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<ClassFee> specification = createSpecification(criteria);
        List<ClassFeeDTO> classFeeDTOs = classFeeMapper.toDto(classFeeRepository.findAll(specification));
        for (ClassFeeDTO classFeeDTO : classFeeDTOs) {
            Optional<SchoolLedgerHeadDTO> schoolLedgerHeadDtoOpt = schoolLedgerHeadService.findOne(
                classFeeDTO.getSchoolLedgerHead().getId()
            );
            if (schoolLedgerHeadDtoOpt.isPresent()) {
                classFeeDTO.setSchoolLedgerHead(schoolLedgerHeadDtoOpt.get());
            }
        }
        return classFeeDTOs;
    }

    /**
     * Return a {@link Page} of {@link ClassFeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassFeeDTO> findByCriteria(ClassFeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassFee> specification = createSpecification(criteria);
        return classFeeRepository.findAll(specification, page).map(classFeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassFeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassFee> specification = createSpecification(criteria);
        return classFeeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassFeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassFee> createSpecification(ClassFeeCriteria criteria) {
        Specification<ClassFee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassFee_.id));
            }
            if (criteria.getFeeYear() != null) {
                specification = specification.and(buildSpecification(criteria.getFeeYear(), ClassFee_.feeYear));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), ClassFee_.dueDate));
            }
            if (criteria.getJanFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJanFee(), ClassFee_.janFee));
            }
            if (criteria.getFebFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFebFee(), ClassFee_.febFee));
            }
            if (criteria.getMarFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarFee(), ClassFee_.marFee));
            }
            if (criteria.getAprFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAprFee(), ClassFee_.aprFee));
            }
            if (criteria.getMayFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMayFee(), ClassFee_.mayFee));
            }
            if (criteria.getJunFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJunFee(), ClassFee_.junFee));
            }
            if (criteria.getJulFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJulFee(), ClassFee_.julFee));
            }
            if (criteria.getAugFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAugFee(), ClassFee_.augFee));
            }
            if (criteria.getSepFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSepFee(), ClassFee_.sepFee));
            }
            if (criteria.getOctFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOctFee(), ClassFee_.octFee));
            }
            if (criteria.getNovFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNovFee(), ClassFee_.novFee));
            }
            if (criteria.getDecFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecFee(), ClassFee_.decFee));
            }
            if (criteria.getPayByDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayByDate(), ClassFee_.payByDate));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), ClassFee_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), ClassFee_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), ClassFee_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(ClassFee_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
            if (criteria.getSchoolLedgerHeadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolLedgerHeadId(),
                            root -> root.join(ClassFee_.schoolLedgerHead, JoinType.LEFT).get(SchoolLedgerHead_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
