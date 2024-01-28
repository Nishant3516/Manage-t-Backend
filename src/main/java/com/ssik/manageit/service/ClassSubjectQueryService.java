package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.repository.ClassSubjectRepository;
import com.ssik.manageit.service.criteria.ClassSubjectCriteria;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import com.ssik.manageit.service.mapper.ClassSubjectMapper;
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
 * Service for executing complex queries for {@link ClassSubject} entities in the database.
 * The main input is a {@link ClassSubjectCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassSubjectDTO} or a {@link Page} of {@link ClassSubjectDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassSubjectQueryService extends QueryService<ClassSubject> {

    private final Logger log = LoggerFactory.getLogger(ClassSubjectQueryService.class);

    private final ClassSubjectRepository classSubjectRepository;

    private final ClassSubjectMapper classSubjectMapper;

    public ClassSubjectQueryService(ClassSubjectRepository classSubjectRepository, ClassSubjectMapper classSubjectMapper) {
        this.classSubjectRepository = classSubjectRepository;
        this.classSubjectMapper = classSubjectMapper;
    }

    /**
     * Return a {@link List} of {@link ClassSubjectDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassSubjectDTO> findByCriteria(ClassSubjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassSubject> specification = createSpecification(criteria);
        return classSubjectMapper.toDto(classSubjectRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClassSubjectDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassSubjectDTO> findByCriteria(ClassSubjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassSubject> specification = createSpecification(criteria);
        return classSubjectRepository.findAll(specification, page).map(classSubjectMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassSubjectCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassSubject> specification = createSpecification(criteria);
        return classSubjectRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassSubjectCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassSubject> createSpecification(ClassSubjectCriteria criteria) {
        Specification<ClassSubject> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassSubject_.id));
            }
            if (criteria.getSubjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubjectName(), ClassSubject_.subjectName));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), ClassSubject_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), ClassSubject_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), ClassSubject_.cancelDate));
            }
            if (criteria.getSubjectChapterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubjectChapterId(),
                            root -> root.join(ClassSubject_.subjectChapters, JoinType.LEFT).get(SubjectChapter_.id)
                        )
                    );
            }
            if (criteria.getClassLessionPlanId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassLessionPlanId(),
                            root -> root.join(ClassSubject_.classLessionPlans, JoinType.LEFT).get(ClassLessionPlan_.id)
                        )
                    );
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(ClassSubject_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
            if (criteria.getSchoolUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolUserId(),
                            root -> root.join(ClassSubject_.schoolUsers, JoinType.LEFT).get(SchoolUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
