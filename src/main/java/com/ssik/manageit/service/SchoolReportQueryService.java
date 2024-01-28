package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolReport;
import com.ssik.manageit.repository.SchoolReportRepository;
import com.ssik.manageit.service.criteria.SchoolReportCriteria;
import com.ssik.manageit.service.dto.SchoolReportDTO;
import com.ssik.manageit.service.mapper.SchoolReportMapper;
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
 * Service for executing complex queries for {@link SchoolReport} entities in the database.
 * The main input is a {@link SchoolReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolReportDTO} or a {@link Page} of {@link SchoolReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolReportQueryService extends QueryService<SchoolReport> {

    private final Logger log = LoggerFactory.getLogger(SchoolReportQueryService.class);

    private final SchoolReportRepository schoolReportRepository;

    private final SchoolReportMapper schoolReportMapper;

    public SchoolReportQueryService(SchoolReportRepository schoolReportRepository, SchoolReportMapper schoolReportMapper) {
        this.schoolReportRepository = schoolReportRepository;
        this.schoolReportMapper = schoolReportMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolReportDTO> findByCriteria(SchoolReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<SchoolReport> specification = createSpecification(criteria);
        return schoolReportMapper.toDto(schoolReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolReportDTO> findByCriteria(SchoolReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolReport> specification = createSpecification(criteria);
        return schoolReportRepository.findAll(specification, page).map(schoolReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolReport> specification = createSpecification(criteria);
        return schoolReportRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolReport> createSpecification(SchoolReportCriteria criteria) {
        Specification<SchoolReport> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolReport_.id));
            }
            if (criteria.getReportType() != null) {
                specification = specification.and(buildSpecification(criteria.getReportType(), SchoolReport_.reportType));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), SchoolReport_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), SchoolReport_.endDate));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolReport_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolReport_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolReport_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolReport_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
