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

import com.ssik.manageit.service.SchoolVideoGalleryQueryService;
import com.ssik.manageit.service.SchoolVideoGalleryService;
import com.ssik.manageit.service.criteria.SchoolVideoGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;

import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolVideoGallery}.
 */
@RestController
@RequestMapping("/api/public")
public class SchoolVideoGalleryPublicResource {

	private final Logger log = LoggerFactory.getLogger(SchoolVideoGalleryPublicResource.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolVideoGalleryService schoolVideoGalleryService;

	private final SchoolVideoGalleryQueryService schoolVideoGalleryQueryService;

	public SchoolVideoGalleryPublicResource(SchoolVideoGalleryService schoolVideoGalleryService,
			SchoolVideoGalleryQueryService schoolVideoGalleryQueryService) {
		this.schoolVideoGalleryService = schoolVideoGalleryService;
		this.schoolVideoGalleryQueryService = schoolVideoGalleryQueryService;
	}

	/**
	 * {@code GET  /school-video-galleries} : get all the schoolVideoGalleries.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolVideoGalleries in body.
	 */
	@GetMapping("/school-video-galleries")
	public ResponseEntity<List<SchoolVideoGalleryDTO>> getAllSchoolVideoGalleries(SchoolVideoGalleryCriteria criteria,
			Pageable pageable) {
		log.debug("REST request to get SchoolVideoGalleries by criteria: {}", criteria);
		Page<SchoolVideoGalleryDTO> page = schoolVideoGalleryQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /school-video-galleries/count} : count all the
	 * schoolVideoGalleries.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-video-galleries/count")
	public ResponseEntity<Long> countSchoolVideoGalleries(SchoolVideoGalleryCriteria criteria) {
		log.debug("REST request to count SchoolVideoGalleries by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolVideoGalleryQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-video-galleries/:id} : get the "id" schoolVideoGallery.
	 *
	 * @param id the id of the schoolVideoGalleryDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolVideoGalleryDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-video-galleries/{id}")
	public ResponseEntity<SchoolVideoGalleryDTO> getSchoolVideoGallery(@PathVariable Long id) {
		log.debug("REST request to get SchoolVideoGallery : {}", id);
		Optional<SchoolVideoGalleryDTO> schoolVideoGalleryDTO = schoolVideoGalleryService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolVideoGalleryDTO);
	}

}
