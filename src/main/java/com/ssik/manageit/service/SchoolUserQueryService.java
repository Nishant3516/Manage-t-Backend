package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.repository.SchoolUserRepository;
import com.ssik.manageit.service.criteria.SchoolUserCriteria;
import com.ssik.manageit.service.dto.SchoolUserDTO;
import com.ssik.manageit.service.mapper.SchoolUserMapper;
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
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link SchoolUser} entities in the database.
 * The main input is a {@link SchoolUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolUserDTO} or a {@link Page} of {@link SchoolUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolUserQueryService extends QueryService<SchoolUser> {

    private final Logger log = LoggerFactory.getLogger(SchoolUserQueryService.class);

    private final SchoolUserRepository schoolUserRepository;

    private final SchoolUserMapper schoolUserMapper;

    public SchoolUserQueryService(SchoolUserRepository schoolUserRepository, SchoolUserMapper schoolUserMapper) {
        this.schoolUserRepository = schoolUserRepository;
        this.schoolUserMapper = schoolUserMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolUserDTO> findByCriteria(SchoolUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<SchoolUser> specification = createSpecification(criteria);
        return schoolUserMapper.toDto(schoolUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolUserDTO> findByCriteria(SchoolUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolUser> specification = createSpecification(criteria);
        return schoolUserRepository.findAll(specification, page).map(schoolUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolUser> specification = createSpecification(criteria);
        return schoolUserRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolUser> createSpecification(SchoolUserCriteria criteria) {
        Specification<SchoolUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolUser_.id));
            }
            if (criteria.getLoginName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLoginName(), SchoolUser_.loginName));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), SchoolUser_.password));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), SchoolUser_.userEmail));
            }
            if (criteria.getExtraInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtraInfo(), SchoolUser_.extraInfo));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), SchoolUser_.activated));
            }
            if (criteria.getUserType() != null) {
                specification = specification.and(buildSpecification(criteria.getUserType(), SchoolUser_.userType));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolUser_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolUser_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolUser_.cancelDate));
            }
            if (criteria.getAuditLogId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAuditLogId(),
                            root -> root.join(SchoolUser_.auditLogs, JoinType.LEFT).get(AuditLog_.id)
                        )
                    );
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolUser_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
            if (criteria.getClassSubjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassSubjectId(),
                            root -> root.join(SchoolUser_.classSubjects, JoinType.LEFT).get(ClassSubject_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
