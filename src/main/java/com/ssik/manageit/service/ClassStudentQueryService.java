package com.ssik.manageit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.ClassStudent_;
import com.ssik.manageit.domain.SchoolClass_;
import com.ssik.manageit.domain.StudentAdditionalCharges_;
import com.ssik.manageit.domain.StudentAttendence_;
import com.ssik.manageit.domain.StudentChargesSummary_;
import com.ssik.manageit.domain.StudentClassWorkTrack_;
import com.ssik.manageit.domain.StudentDiscount_;
import com.ssik.manageit.domain.StudentHomeWorkTrack_;
import com.ssik.manageit.domain.StudentPayments_;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;

import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link ClassStudent} entities in
 * the database. The main input is a {@link ClassStudentCriteria} which gets
 * converted to {@link Specification}, in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassStudentDTO} or a {@link Page} of
 * {@link ClassStudentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassStudentQueryService extends QueryService<ClassStudent> {

	private final Logger log = LoggerFactory.getLogger(ClassStudentQueryService.class);

	private final ClassStudentRepository classStudentRepository;

	private final ClassStudentMapper classStudentMapper;

	@Autowired
	ClassStudentService classStudentService;

	public ClassStudentQueryService(ClassStudentRepository classStudentRepository,
			ClassStudentMapper classStudentMapper) {
		this.classStudentRepository = classStudentRepository;
		this.classStudentMapper = classStudentMapper;
	}

	/**
	 * Return a {@link List} of {@link ClassStudentDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassStudent> findByCriteria(ClassStudentCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		if (criteria.getCancelDate() == null) {
			LocalDateFilter localDateFilter = new LocalDateFilter();
			localDateFilter.setSpecified(false);
			criteria.setCancelDate(localDateFilter);
		}
		final Specification<ClassStudent> specification = createSpecification(criteria);
		return classStudentRepository.findAll(specification);
	}

	@Transactional(readOnly = true)
	public List<ClassStudent> findEntitiesByCriteria(ClassStudentCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		if (criteria.getCancelDate() == null) {
			LocalDateFilter localDateFilter = new LocalDateFilter();
			localDateFilter.setSpecified(false);
			criteria.setCancelDate(localDateFilter);
		}
		final Specification<ClassStudent> specification = createSpecification(criteria);
		return classStudentRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link ClassStudentDTO} which matches the criteria
	 * from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<ClassStudent> findByCriteria(ClassStudentCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		if (criteria.getCancelDate() == null) {
			LocalDateFilter localDateFilter = new LocalDateFilter();
			localDateFilter.setSpecified(false);
			criteria.setCancelDate(localDateFilter);
		}
		final Specification<ClassStudent> specification = createSpecification(criteria);
		return classStudentRepository.findAll(specification, Sort.by(Sort.Direction.ASC, "studentId"));
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(ClassStudentCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<ClassStudent> specification = createSpecification(criteria);
		return classStudentRepository.count(specification);
	}

	/**
	 * Function to convert {@link ClassStudentCriteria} to a {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<ClassStudent> createSpecification(ClassStudentCriteria criteria) {
		Specification<ClassStudent> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), ClassStudent_.id));
			}
			if (criteria.getStudentPhotoLink() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getStudentPhotoLink(), ClassStudent_.studentPhotoLink));
			}
			if (criteria.getStudentId() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getStudentId(), ClassStudent_.studentId));
			}
			if (criteria.getFirstName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getFirstName(), ClassStudent_.firstName));
			}
			if (criteria.getGender() != null) {
				specification = specification.and(buildSpecification(criteria.getGender(), ClassStudent_.gender));
			}
			if (criteria.getLastName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getLastName(), ClassStudent_.lastName));
			}
			if (criteria.getRollNumber() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRollNumber(), ClassStudent_.rollNumber));
			}
			if (criteria.getPhoneNumber() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getPhoneNumber(), ClassStudent_.phoneNumber));
			}
			if (criteria.getBloodGroup() != null) {
				specification = specification
						.and(buildSpecification(criteria.getBloodGroup(), ClassStudent_.bloodGroup));
			}
			if (criteria.getDateOfBirth() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getDateOfBirth(), ClassStudent_.dateOfBirth));
			}
			if (criteria.getStartDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getStartDate(), ClassStudent_.startDate));
			}
			if (criteria.getAddressLine1() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getAddressLine1(), ClassStudent_.addressLine1));
			}
			if (criteria.getAddressLine2() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getAddressLine2(), ClassStudent_.addressLine2));
			}
			if (criteria.getNickName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getNickName(), ClassStudent_.nickName));
			}
			if (criteria.getFatherName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getFatherName(), ClassStudent_.fatherName));
			}
			if (criteria.getMotherName() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getMotherName(), ClassStudent_.motherName));
			}
			if (criteria.getEmail() != null) {
				specification = specification.and(buildStringSpecification(criteria.getEmail(), ClassStudent_.email));
			}
			if (criteria.getAdmissionDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getAdmissionDate(), ClassStudent_.admissionDate));
			}
			if (criteria.getRegNumber() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRegNumber(), ClassStudent_.regNumber));
			}
			if (criteria.getEndDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getEndDate(), ClassStudent_.endDate));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), ClassStudent_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), ClassStudent_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), ClassStudent_.cancelDate));
			}
			if (criteria.getStudentDiscountId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentDiscountId(),
						root -> root.join(ClassStudent_.studentDiscounts, JoinType.LEFT).get(StudentDiscount_.id)));
			}
			if (criteria.getStudentAdditionalChargesId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentAdditionalChargesId(),
						root -> root.join(ClassStudent_.studentAdditionalCharges, JoinType.LEFT)
								.get(StudentAdditionalCharges_.id)));
			}
			if (criteria.getStudentChargesSummaryId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentChargesSummaryId(), root -> root
						.join(ClassStudent_.studentChargesSummaries, JoinType.LEFT).get(StudentChargesSummary_.id)));
			}
			if (criteria.getStudentPaymentsId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentPaymentsId(),
						root -> root.join(ClassStudent_.studentPayments, JoinType.LEFT).get(StudentPayments_.id)));
			}
			if (criteria.getStudentAttendenceId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentAttendenceId(),
						root -> root.join(ClassStudent_.studentAttendences, JoinType.LEFT).get(StudentAttendence_.id)));
			}
			if (criteria.getStudentHomeWorkTrackId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentHomeWorkTrackId(), root -> root
						.join(ClassStudent_.studentHomeWorkTracks, JoinType.LEFT).get(StudentHomeWorkTrack_.id)));
			}
			if (criteria.getStudentClassWorkTrackId() != null) {
				specification = specification.and(buildSpecification(criteria.getStudentClassWorkTrackId(), root -> root
						.join(ClassStudent_.studentClassWorkTracks, JoinType.LEFT).get(StudentClassWorkTrack_.id)));
			}
			if (criteria.getSchoolClassId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolClassId(),
						root -> root.join(ClassStudent_.schoolClass, JoinType.LEFT).get(SchoolClass_.id)));
			}
		}
		return specification;
	}
}
