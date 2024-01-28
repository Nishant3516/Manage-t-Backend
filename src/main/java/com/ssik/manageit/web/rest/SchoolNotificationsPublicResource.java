package com.ssik.manageit.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.domain.SchoolNotifications;
import com.ssik.manageit.service.SchoolNotificationsQueryService;
import com.ssik.manageit.service.SchoolNotificationsService;
import com.ssik.manageit.service.criteria.SchoolNotificationsCriteria;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;

import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolNotifications}.
 */
@RestController
@RequestMapping("/public")
public class SchoolNotificationsPublicResource {

	private final Logger log = LoggerFactory.getLogger(SchoolNotificationsPublicResource.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolNotificationsService schoolNotificationsService;

	private final SchoolNotificationsQueryService schoolNotificationsQueryService;

	public SchoolNotificationsPublicResource(SchoolNotificationsService schoolNotificationsService,
			SchoolNotificationsQueryService schoolNotificationsQueryService) {
		this.schoolNotificationsService = schoolNotificationsService;
		this.schoolNotificationsQueryService = schoolNotificationsQueryService;
	}

	/**
	 * {@code GET  /school-notifications} : get all the schoolNotifications.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolNotifications in body.
	 */
	@GetMapping("/school-notifications")
	public ResponseEntity<List<SchoolNotificationsDTO>> getAllSchoolNotifications(SchoolNotificationsCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get SchoolNotifications by criteria: {}", criteria);
		// Page<SchoolNotificationsDTO> page =
		// schoolNotificationsQueryService.findByCriteria(criteria, pageable);
		// HttpHeaders headers = PaginationUtil
		// .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
		// schoolNotificationsQueryService.findByCriteria(criteria, pageable));
		return ResponseEntity.ok().body(schoolNotificationsQueryService.findByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-notifications/count} : count all the schoolNotifications.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-notifications/count")
	public ResponseEntity<Long> countSchoolNotifications(SchoolNotificationsCriteria criteria) {
		log.debug("REST request to count SchoolNotifications by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolNotificationsQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-notifications/:id} : get the "id" schoolNotifications.
	 *
	 * @param id the id of the schoolNotificationsDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolNotificationsDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-notifications/{id}")
	public ResponseEntity<SchoolNotificationsDTO> getSchoolNotifications(@PathVariable Long id) {
		log.debug("REST request to get SchoolNotifications : {}", id);
		Optional<SchoolNotificationsDTO> schoolNotificationsDTO = schoolNotificationsService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolNotificationsDTO);
	}

}
