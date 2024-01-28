package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolEvent;
import com.ssik.manageit.repository.SchoolEventRepository;
import com.ssik.manageit.service.criteria.SchoolEventCriteria;
import com.ssik.manageit.service.dto.SchoolEventDTO;
import com.ssik.manageit.service.mapper.SchoolEventMapper;
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
 * Service for executing complex queries for {@link SchoolEvent} entities in the database.
 * The main input is a {@link SchoolEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolEventDTO} or a {@link Page} of {@link SchoolEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolEventQueryService extends QueryService<SchoolEvent> {

    private final Logger log = LoggerFactory.getLogger(SchoolEventQueryService.class);

    private final SchoolEventRepository schoolEventRepository;

    private final SchoolEventMapper schoolEventMapper;

    public SchoolEventQueryService(SchoolEventRepository schoolEventRepository, SchoolEventMapper schoolEventMapper) {
        this.schoolEventRepository = schoolEventRepository;
        this.schoolEventMapper = schoolEventMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolEventDTO> findByCriteria(SchoolEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<SchoolEvent> specification = createSpecification(criteria);
        return schoolEventMapper.toDto(schoolEventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolEventDTO> findByCriteria(SchoolEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolEvent> specification = createSpecification(criteria);
        return schoolEventRepository.findAll(specification, page).map(schoolEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolEvent> specification = createSpecification(criteria);
        return schoolEventRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolEvent> createSpecification(SchoolEventCriteria criteria) {
        Specification<SchoolEvent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolEvent_.id));
            }
            if (criteria.getEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventName(), SchoolEvent_.eventName));
            }
            if (criteria.getEventDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventDetails(), SchoolEvent_.eventDetails));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), SchoolEvent_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), SchoolEvent_.endDate));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolEvent_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolEvent_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolEvent_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolEvent_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
