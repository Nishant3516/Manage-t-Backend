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
import com.ssik.manageit.domain.ClassHomeWork_;
import com.ssik.manageit.domain.ClassStudent_;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.domain.StudentHomeWorkTrack_;
import com.ssik.manageit.repository.StudentHomeWorkTrackRepository;
import com.ssik.manageit.service.criteria.StudentHomeWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentHomeWorkTrackMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentHomeWorkTrack}
 * entities in the database. The main input is a
 * {@link StudentHomeWorkTrackCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link StudentHomeWorkTrackDTO} or a {@link Page} of
 * {@link StudentHomeWorkTrackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentHomeWorkTrackQueryService extends QueryService<StudentHomeWorkTrack> {

	private final Logger log = LoggerFactory.getLogger(StudentHomeWorkTrackQueryService.class);

	private final StudentHomeWorkTrackRepository studentHomeWorkTrackRepository;

	private final StudentHomeWorkTrackMapper studentHomeWorkTrackMapper;

	public StudentHomeWorkTrackQueryService(StudentHomeWorkTrackRepository studentHomeWorkTrackRepository,
			StudentHomeWorkTrackMapper studentHomeWorkTrackMapper) {
		this.studentHomeWorkTrackRepository = studentHomeWorkTrackRepository;
		this.studentHomeWorkTrackMapper = studentHomeWorkTrackMapper;
	}

	/**
	 * Return a {@link List} of {@link StudentHomeWorkTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<StudentHomeWorkTrack> findByCriteria(StudentHomeWorkTrackCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
//		LocalDateFilter localDateFilter = new LocalDateFilter();
//		localDateFilter.setSpecified(false);
//		criteria.setCancelDate(localDateFilter);

		final Specification<StudentHomeWorkTrack> specification = createSpecification(criteria);
		return studentHomeWorkTrackRepository.findAll(specification);
	}

	/**
	 * Return a {@link Page} of {@link StudentHomeWorkTrackDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<StudentHomeWorkTrack> findByCriteria(StudentHomeWorkTrackCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<StudentHomeWorkTrack> specification = createSpecification(criteria);
		return studentHomeWorkTrackRepository.findAll(specification, page);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(StudentHomeWorkTrackCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<StudentHomeWorkTrack> specification = createSpecification(criteria);
		return studentHomeWorkTrackRepository.count(specification);
	}

	/**
	 * Function to convert {@link StudentHomeWorkTrackCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<StudentHomeWorkTrack> createSpecification(StudentHomeWorkTrackCriteria criteria) {
		Specification<StudentHomeWorkTrack> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), StudentHomeWorkTrack_.id));
			}
			if (criteria.getWorkStatus() != null) {
				specification = specification
						.and(buildSpecification(criteria.getWorkStatus(), StudentHomeWorkTrack_.workStatus));
			}
			if (criteria.getRemarks() != null) {
				specification = specification
						.and(buildStringSpecification(criteria.getRemarks(), StudentHomeWorkTrack_.remarks));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), StudentHomeWorkTrack_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), StudentHomeWorkTrack_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), StudentHomeWorkTrack_.cancelDate));
			}
			if (criteria.getClassStudentId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassStudentId(),
						root -> root.join(StudentHomeWorkTrack_.classStudent, JoinType.LEFT).get(ClassStudent_.id)));
			}
			if (criteria.getClassHomeWorkId() != null) {
				specification = specification.and(buildSpecification(criteria.getClassHomeWorkId(),
						root -> root.join(StudentHomeWorkTrack_.classHomeWork, JoinType.LEFT).get(ClassHomeWork_.id)));
			}
		}
		return specification;
	}
}
