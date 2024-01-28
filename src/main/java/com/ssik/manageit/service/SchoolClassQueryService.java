package com.ssik.manageit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import com.ssik.manageit.domain.ClassFee_;
import com.ssik.manageit.domain.ClassLessionPlan_;
import com.ssik.manageit.domain.ClassStudent_;
import com.ssik.manageit.domain.ClassSubject_;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolClass_;
import com.ssik.manageit.domain.SchoolDaysOff_;
import com.ssik.manageit.domain.SchoolEvent_;
import com.ssik.manageit.domain.SchoolNotifications_;
import com.ssik.manageit.domain.SchoolPictureGallery_;
import com.ssik.manageit.domain.SchoolReport_;
import com.ssik.manageit.domain.SchoolUser_;
import com.ssik.manageit.domain.SchoolVideoGallery_;
import com.ssik.manageit.domain.School_;
import com.ssik.manageit.repository.SchoolClassRepository;
import com.ssik.manageit.service.criteria.SchoolClassCriteria;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.mapper.SchoolClassMapper;

import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link SchoolClass} entities in the
 * database. The main input is a {@link SchoolClassCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolClassDTO} or a {@link Page} of
 * {@link SchoolClassDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolClassQueryService extends QueryService<SchoolClass> {

	private final Logger log = LoggerFactory.getLogger(SchoolClassQueryService.class);

	private final SchoolClassRepository schoolClassRepository;

	private final SchoolClassMapper schoolClassMapper;

	public SchoolClassQueryService(SchoolClassRepository schoolClassRepository, SchoolClassMapper schoolClassMapper) {
		this.schoolClassRepository = schoolClassRepository;
		this.schoolClassMapper = schoolClassMapper;
	}

	/**
	 * Return a {@link List} of {@link SchoolClassDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<SchoolClass> findByCriteria(SchoolClassCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		LocalDateFilter localDateFilter = new LocalDateFilter();
		localDateFilter.setSpecified(false);
		criteria.setCancelDate(localDateFilter);

		final Specification<SchoolClass> specification = createSpecification(criteria);
		return schoolClassRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link SchoolClassDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<SchoolClass> findByCriteria(SchoolClassCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<SchoolClass> specification = createSpecification(criteria);
		return schoolClassRepository.findAll(specification, Sort.by(Sort.Direction.ASC, "id"));
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(SchoolClassCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<SchoolClass> specification = createSpecification(criteria);
		return schoolClassRepository.count(specification);
	}

	/**
	 * Function to convert {@link SchoolClassCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<SchoolClass> createSpecification(SchoolClassCriteria criteria) {
		Specification<SchoolClass> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolClass_.id));
			}
			if (criteria.getClassName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getClassName(), SchoolClass_.className));
			}
			if (criteria.getClassLongName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getClassLongName(), SchoolClass_.classLongName));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), SchoolClass_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), SchoolClass_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), SchoolClass_.cancelDate));
			}
			if (criteria.getClassStudentId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassStudentId(),
						root -> root.join(SchoolClass_.classStudents, JoinType.LEFT).get(ClassStudent_.id)));
			}
			if (criteria.getClassLessionPlanId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassLessionPlanId(),
						root -> root.join(SchoolClass_.classLessionPlans, JoinType.LEFT).get(ClassLessionPlan_.id)));
			}
			if (criteria.getSchoolId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolId(),
						root -> root.join(SchoolClass_.school, JoinType.LEFT).get(School_.id)));
			}
			if (criteria.getSchoolNotificationsId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolNotificationsId(), root -> root
						.join(SchoolClass_.schoolNotifications, JoinType.LEFT).get(SchoolNotifications_.id)));
			}
			if (criteria.getClassFeeId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassFeeId(),
						root -> root.join(SchoolClass_.classFees, JoinType.LEFT).get(ClassFee_.id)));
			}
			if (criteria.getClassSubjectId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassSubjectId(),
						root -> root.join(SchoolClass_.classSubjects, JoinType.LEFT).get(ClassSubject_.id)));
			}
			if (criteria.getSchoolUserId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolUserId(),
						root -> root.join(SchoolClass_.schoolUsers, JoinType.LEFT).get(SchoolUser_.id)));
			}
			if (criteria.getSchoolDaysOffId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolDaysOffId(),
						root -> root.join(SchoolClass_.schoolDaysOffs, JoinType.LEFT).get(SchoolDaysOff_.id)));
			}
			if (criteria.getSchoolEventId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolEventId(),
						root -> root.join(SchoolClass_.schoolEvents, JoinType.LEFT).get(SchoolEvent_.id)));
			}
			if (criteria.getSchoolPictureGalleryId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolPictureGalleryId(), root -> root
						.join(SchoolClass_.schoolPictureGalleries, JoinType.LEFT).get(SchoolPictureGallery_.id)));
			}
			if (criteria.getVchoolVideoGalleryId() != null) {
				specification = specification.and(buildSpecification(criteria.getVchoolVideoGalleryId(), root -> root
						.join(SchoolClass_.vchoolVideoGalleries, JoinType.LEFT).get(SchoolVideoGallery_.id)));
			}
			if (criteria.getSchoolReportId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolReportId(),
						root -> root.join(SchoolClass_.schoolReports, JoinType.LEFT).get(SchoolReport_.id)));
			}
		}
		return specification;
	}
}
