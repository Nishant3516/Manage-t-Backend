package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolNotifications;
import com.ssik.manageit.repository.SchoolNotificationsRepository;
import com.ssik.manageit.service.criteria.SchoolNotificationsCriteria;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;
import com.ssik.manageit.service.mapper.SchoolNotificationsMapper;
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
 * Service for executing complex queries for {@link SchoolNotifications}
 * entities in the database. The main input is a
 * {@link SchoolNotificationsCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link SchoolNotificationsDTO} or a {@link Page} of
 * {@link SchoolNotificationsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolNotificationsQueryService extends QueryService<SchoolNotifications> {

	private final Logger log = LoggerFactory.getLogger(SchoolNotificationsQueryService.class);

	private final SchoolNotificationsRepository schoolNotificationsRepository;

	private final SchoolNotificationsMapper schoolNotificationsMapper;

	public SchoolNotificationsQueryService(SchoolNotificationsRepository schoolNotificationsRepository,
			SchoolNotificationsMapper schoolNotificationsMapper) {
		this.schoolNotificationsRepository = schoolNotificationsRepository;
		this.schoolNotificationsMapper = schoolNotificationsMapper;
	}

	/**
	 * Return a {@link List} of {@link SchoolNotificationsDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public List<SchoolNotificationsDTO> findByCriteria(SchoolNotificationsCriteria criteria) {
		log.debug("find by criteria : {}", criteria);
		LocalDateFilter localDateFilter = new LocalDateFilter();
		localDateFilter.setSpecified(false);
		criteria.setCancelDate(localDateFilter);

		final Specification<SchoolNotifications> specification = createSpecification(criteria);
		return schoolNotificationsMapper.toDto(schoolNotificationsRepository.findAll(specification));
	}

	/**
	 * Return a {@link Page} of {@link SchoolNotificationsDTO} which matches the
	 * criteria from the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @param page     The page, which should be returned.
	 * @return the matching entities.
	 */
	@Transactional(readOnly = true)
	public Page<SchoolNotificationsDTO> findByCriteria(SchoolNotificationsCriteria criteria, Pageable page) {
		log.debug("find by criteria : {}, page: {}", criteria, page);
		final Specification<SchoolNotifications> specification = createSpecification(criteria);
		List<SchoolNotifications> notifications = schoolNotificationsRepository.findAll();
		log.debug("Total notifications found are :: " + notifications.size());
		return schoolNotificationsRepository.findAll(specification, page).map(schoolNotificationsMapper::toDto);
	}

	/**
	 * Return the number of matching entities in the database.
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the number of matching entities.
	 */
	@Transactional(readOnly = true)
	public long countByCriteria(SchoolNotificationsCriteria criteria) {
		log.debug("count by criteria : {}", criteria);
		final Specification<SchoolNotifications> specification = createSpecification(criteria);
		return schoolNotificationsRepository.count(specification);
	}

	/**
	 * Function to convert {@link SchoolNotificationsCriteria} to a
	 * {@link Specification}
	 * 
	 * @param criteria The object which holds all the filters, which the entities
	 *                 should match.
	 * @return the matching {@link Specification} of the entity.
	 */
	protected Specification<SchoolNotifications> createSpecification(SchoolNotificationsCriteria criteria) {
		Specification<SchoolNotifications> specification = Specification.where(null);
		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolNotifications_.id));
			}
			if (criteria.getNotificationTitle() != null) {
				specification = specification.and(buildStringSpecification(criteria.getNotificationTitle(),
						SchoolNotifications_.notificationTitle));
			}
			if (criteria.getNotificationDetails() != null) {
				specification = specification.and(buildStringSpecification(criteria.getNotificationDetails(),
						SchoolNotifications_.notificationDetails));
			}
			if (criteria.getNotificationFileLink() != null) {
				specification = specification.and(buildStringSpecification(criteria.getNotificationFileLink(),
						SchoolNotifications_.notificationFileLink));
			}
			if (criteria.getShowTillDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getShowTillDate(), SchoolNotifications_.showTillDate));
			}
			if (criteria.getCreateDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCreateDate(), SchoolNotifications_.createDate));
			}
			if (criteria.getLastModified() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getLastModified(), SchoolNotifications_.lastModified));
			}
			if (criteria.getCancelDate() != null) {
				specification = specification
						.and(buildRangeSpecification(criteria.getCancelDate(), SchoolNotifications_.cancelDate));
			}
			if (criteria.getSchoolClassId() != null) {
				specification = specification.and(buildSpecification(criteria.getSchoolClassId(),
						root -> root.join(SchoolNotifications_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)));
			}
		}
		return specification;
	}
}
