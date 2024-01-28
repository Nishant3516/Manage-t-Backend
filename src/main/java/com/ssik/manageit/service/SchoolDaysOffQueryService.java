package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolDaysOff;
import com.ssik.manageit.repository.SchoolDaysOffRepository;
import com.ssik.manageit.service.criteria.SchoolDaysOffCriteria;
import com.ssik.manageit.service.dto.SchoolDaysOffDTO;
import com.ssik.manageit.service.mapper.SchoolDaysOffMapper;
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
 * Service for executing complex queries for {@link SchoolDaysOff} entities in the database.
 * The main input is a {@link SchoolDaysOffCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolDaysOffDTO} or a {@link Page} of {@link SchoolDaysOffDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolDaysOffQueryService extends QueryService<SchoolDaysOff> {

    private final Logger log = LoggerFactory.getLogger(SchoolDaysOffQueryService.class);

    private final SchoolDaysOffRepository schoolDaysOffRepository;

    private final SchoolDaysOffMapper schoolDaysOffMapper;

    public SchoolDaysOffQueryService(SchoolDaysOffRepository schoolDaysOffRepository, SchoolDaysOffMapper schoolDaysOffMapper) {
        this.schoolDaysOffRepository = schoolDaysOffRepository;
        this.schoolDaysOffMapper = schoolDaysOffMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolDaysOffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolDaysOffDTO> findByCriteria(SchoolDaysOffCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<SchoolDaysOff> specification = createSpecification(criteria);
        return schoolDaysOffMapper.toDto(schoolDaysOffRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolDaysOffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolDaysOffDTO> findByCriteria(SchoolDaysOffCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolDaysOff> specification = createSpecification(criteria);
        return schoolDaysOffRepository.findAll(specification, page).map(schoolDaysOffMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolDaysOffCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolDaysOff> specification = createSpecification(criteria);
        return schoolDaysOffRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolDaysOffCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolDaysOff> createSpecification(SchoolDaysOffCriteria criteria) {
        Specification<SchoolDaysOff> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolDaysOff_.id));
            }
            if (criteria.getDayOffType() != null) {
                specification = specification.and(buildSpecification(criteria.getDayOffType(), SchoolDaysOff_.dayOffType));
            }
            if (criteria.getDayOffName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDayOffName(), SchoolDaysOff_.dayOffName));
            }
            if (criteria.getDayOffDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDayOffDetails(), SchoolDaysOff_.dayOffDetails));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), SchoolDaysOff_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), SchoolDaysOff_.endDate));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolDaysOff_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolDaysOff_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolDaysOff_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolDaysOff_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
