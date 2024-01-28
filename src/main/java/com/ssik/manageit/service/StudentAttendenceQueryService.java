package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.StudentAttendence;
import com.ssik.manageit.repository.StudentAttendenceRepository;
import com.ssik.manageit.service.criteria.StudentAttendenceCriteria;
import com.ssik.manageit.service.dto.StudentAttendenceDTO;
import com.ssik.manageit.service.mapper.StudentAttendenceMapper;
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
 * Service for executing complex queries for {@link StudentAttendence} entities in the database.
 * The main input is a {@link StudentAttendenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentAttendenceDTO} or a {@link Page} of {@link StudentAttendenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentAttendenceQueryService extends QueryService<StudentAttendence> {

    private final Logger log = LoggerFactory.getLogger(StudentAttendenceQueryService.class);

    private final StudentAttendenceRepository studentAttendenceRepository;

    private final StudentAttendenceMapper studentAttendenceMapper;

    public StudentAttendenceQueryService(
        StudentAttendenceRepository studentAttendenceRepository,
        StudentAttendenceMapper studentAttendenceMapper
    ) {
        this.studentAttendenceRepository = studentAttendenceRepository;
        this.studentAttendenceMapper = studentAttendenceMapper;
    }

    /**
     * Return a {@link List} of {@link StudentAttendenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentAttendenceDTO> findByCriteria(StudentAttendenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentAttendence> specification = createSpecification(criteria);
        return studentAttendenceMapper.toDto(studentAttendenceRepository.findAll(specification));
    }
    @Transactional(readOnly = true)
    public List<StudentAttendence> findEntitiesByCriteria(StudentAttendenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentAttendence> specification = createSpecification(criteria);
        return studentAttendenceRepository.findAll(specification);
    }
    /**
     * Return a {@link Page} of {@link StudentAttendenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentAttendenceDTO> findByCriteria(StudentAttendenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        LocalDateFilter localDateFilter = new LocalDateFilter();
        localDateFilter.setSpecified(false);
        criteria.setCancelDate(localDateFilter);

        final Specification<StudentAttendence> specification = createSpecification(criteria);
        return studentAttendenceRepository.findAll(specification, page).map(studentAttendenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentAttendenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentAttendence> specification = createSpecification(criteria);
        return studentAttendenceRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentAttendenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentAttendence> createSpecification(StudentAttendenceCriteria criteria) {
        Specification<StudentAttendence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentAttendence_.id));
            }
            if (criteria.getSchoolDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSchoolDate(), StudentAttendence_.schoolDate));
            }
            if (criteria.getAttendence() != null) {
                specification = specification.and(buildSpecification(criteria.getAttendence(), StudentAttendence_.attendence));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), StudentAttendence_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), StudentAttendence_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), StudentAttendence_.cancelDate));
            }
            if (criteria.getClassStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassStudentId(),
                            root -> root.join(StudentAttendence_.classStudent, JoinType.LEFT).get(ClassStudent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
