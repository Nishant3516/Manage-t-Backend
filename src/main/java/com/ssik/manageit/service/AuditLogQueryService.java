package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.repository.AuditLogRepository;
import com.ssik.manageit.service.criteria.AuditLogCriteria;
import com.ssik.manageit.service.dto.AuditLogDTO;
import com.ssik.manageit.service.mapper.AuditLogMapper;
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
 * Service for executing complex queries for {@link AuditLog} entities in the database.
 * The main input is a {@link AuditLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditLogDTO} or a {@link Page} of {@link AuditLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditLogQueryService extends QueryService<AuditLog> {

    private final Logger log = LoggerFactory.getLogger(AuditLogQueryService.class);

    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper auditLogMapper;

    public AuditLogQueryService(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    /**
     * Return a {@link List} of {@link AuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> findByCriteria(AuditLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditLog> specification = createSpecification(criteria);
        return auditLogMapper.toDto(auditLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditLogDTO> findByCriteria(AuditLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditLog> specification = createSpecification(criteria);
        return auditLogRepository.findAll(specification, page).map(auditLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditLog> specification = createSpecification(criteria);
        return auditLogRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditLog> createSpecification(AuditLogCriteria criteria) {
        Specification<AuditLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditLog_.id));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), AuditLog_.userName));
            }
            if (criteria.getUserDeviceDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserDeviceDetails(), AuditLog_.userDeviceDetails));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), AuditLog_.action));
            }
            if (criteria.getData1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData1(), AuditLog_.data1));
            }
            if (criteria.getData2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData2(), AuditLog_.data2));
            }
            if (criteria.getData3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData3(), AuditLog_.data3));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), AuditLog_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), AuditLog_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), AuditLog_.cancelDate));
            }
            if (criteria.getSchoolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSchoolId(), root -> root.join(AuditLog_.school, JoinType.LEFT).get(School_.id))
                    );
            }
            if (criteria.getSchoolUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolUserId(),
                            root -> root.join(AuditLog_.schoolUser, JoinType.LEFT).get(SchoolUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
