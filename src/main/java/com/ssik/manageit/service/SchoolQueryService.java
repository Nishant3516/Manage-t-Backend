package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.School;
import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.service.criteria.SchoolCriteria;
import com.ssik.manageit.service.dto.SchoolDTO;
import com.ssik.manageit.service.mapper.SchoolMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link School} entities in the database.
 * The main input is a {@link SchoolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolDTO} or a {@link Page} of {@link SchoolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolQueryService extends QueryService<School> {

    private final Logger log = LoggerFactory.getLogger(SchoolQueryService.class);

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    public SchoolQueryService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolDTO> findByCriteria(SchoolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<School> specification = createSpecification(criteria);
        return schoolMapper.toDto(schoolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolDTO> findByCriteria(SchoolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<School> specification = createSpecification(criteria);
        return schoolRepository.findAll(specification, page).map(schoolMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<School> specification = createSpecification(criteria);
        return schoolRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<School> createSpecification(SchoolCriteria criteria) {
        Specification<School> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), School_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), School_.groupName));
            }
            if (criteria.getSchoolName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchoolName(), School_.schoolName));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), School_.address));
            }
            if (criteria.getAfflNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAfflNumber(), School_.afflNumber));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), School_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), School_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), School_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(School_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
            if (criteria.getSchoolLedgerHeadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolLedgerHeadId(),
                            root -> root.join(School_.schoolLedgerHeads, JoinType.LEFT).get(SchoolLedgerHead_.id)
                        )
                    );
            }
            if (criteria.getIdStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIdStoreId(), root -> root.join(School_.idStores, JoinType.LEFT).get(IdStore_.id))
                    );
            }
            if (criteria.getAuditLogId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuditLogId(), root -> root.join(School_.auditLogs, JoinType.LEFT).get(AuditLog_.id))
                    );
            }
        }
        return specification;
    }
}
