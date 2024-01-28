package com.ssik.manageit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import com.ssik.manageit.domain.ClassClassWork_;
import com.ssik.manageit.domain.ClassStudent_;
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.domain.StudentClassWorkTrack_;
import com.ssik.manageit.repository.StudentClassWorkTrackRepository;
import com.ssik.manageit.service.criteria.StudentClassWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentClassWorkTrackMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentClassWorkTrack}
 * entities in the database. The main input is a
 * {@link StudentClassWorkTrackCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link StudentClassWorkTrackDTO} or a {@link Page} of
 * {@link StudentClassWorkTrackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentClassWorkTrackQueryService extends QueryService<StudentClassWorkTrack> {

	private final Logger log = LoggerFactory.getLogger(StudentClassWorkTrackQueryService.class);

	private final StudentClassWorkTrackRepository studentClassWorkTrackRepository;

	private final StudentClassWorkTrackMapper studentClassWorkTrackMapper;

	public StudentClassWorkTrackQueryService(StudentClassWorkTrackRepository studentClassWorkTrackRepository,
			StudentClassWorkTrackMapper studentClassWorkTrackMapper) {
		this.studentClassWorkTrackRepository = studentClassWorkTrackRepository;
		this.studentClassWorkTrackMapper = studentClassWorkTrackMapper;
	}

	/**
	 * Return a {@link List} of {@link StudentClassWorkTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<StudentClassWorkTrack> findByCriteria(StudentClassWorkTrackCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
//		LocalDateFilter localDateFilter = new LocalDateFilter();
//		localDateFilter.setSpecified(false);
//		criteria.setCancelDate(localDateFilter);

		final Specification<StudentClassWorkTrack> specification = createSpecification(criteria);
		return studentClassWorkTrackRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link StudentClassWorkTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<StudentClassWorkTrack> findByCriteria(StudentClassWorkTrackCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<StudentClassWorkTrack> specification = createSpecification(criteria);
		return studentClassWorkTrackRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(StudentClassWorkTrackCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<StudentClassWorkTrack> specification = createSpecification(criteria);
		return studentClassWorkTrackRepository.count(specification);
	}

	/**
	 * Function to convert {@link StudentClassWorkTrackCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<StudentClassWorkTrack> createSpecification(StudentClassWorkTrackCriteria criteria) {
		Specification<StudentClassWorkTrack> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), StudentClassWorkTrack_.id));
			}
			if (criteria.getWorkStatus() != null) {
				specification = specification
						.and(buildSpecification(criteria.getWorkStatus(), StudentClassWorkTrack_.workStatus));
			}
			if (criteria.getRemarks() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRemarks(), StudentClassWorkTrack_.remarks));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), StudentClassWorkTrack_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), StudentClassWorkTrack_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), StudentClassWorkTrack_.cancelDate));
			}
			if (criteria.getClassStudentId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassStudentId(),
						root -> root.join(StudentClassWorkTrack_.classStudent, JoinType.LEFT).get(ClassStudent_.id)));
			}
			if (criteria.getClassClassWorkId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassClassWorkId(), root -> root
						.join(StudentClassWorkTrack_.classClassWork, JoinType.LEFT).get(ClassClassWork_.id)));
			}
		}
		return specification;
	}
}
