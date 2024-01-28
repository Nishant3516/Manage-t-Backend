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
import com.ssik.manageit.service.SchoolPictureGalleryQueryService;
import com.ssik.manageit.service.SchoolPictureGalleryService;
import com.ssik.manageit.service.criteria.SchoolPictureGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;

import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.ssik.manageit.domain.SchoolPictureGallery}.
 */
@RestController
@RequestMapping("/api/public")
public class SchoolPictureGalleryPublicResource {

	private final Logger log = LoggerFactory.getLogger(SchoolPictureGalleryPublicResource.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final SchoolPictureGalleryService schoolPictureGalleryService;

	private final SchoolPictureGalleryQueryService schoolPictureGalleryQueryService;

	public SchoolPictureGalleryPublicResource(SchoolPictureGalleryService schoolPictureGalleryService,
			SchoolPictureGalleryQueryService schoolPictureGalleryQueryService) {
		this.schoolPictureGalleryService = schoolPictureGalleryService;
		this.schoolPictureGalleryQueryService = schoolPictureGalleryQueryService;
	}

	/**
	 * {@code GET  /school-picture-galleries} : get all the schoolPictureGalleries.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of schoolPictureGalleries in body.
	 */
	@GetMapping("/school-picture-galleries")
	public ResponseEntity<List<SchoolPictureGalleryDTO>> getAllSchoolPictureGalleries(
			SchoolPictureGalleryCriteria criteria, Pageable pageable) {
		log.debug("REST request to get SchoolPictureGalleries by criteria: {}", criteria);
		Page<SchoolPictureGalleryDTO> page = schoolPictureGalleryQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /school-picture-galleries/count} : count all the
	 * schoolPictureGalleries.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/school-picture-galleries/count")
	public ResponseEntity<Long> countSchoolPictureGalleries(SchoolPictureGalleryCriteria criteria) {
		log.debug("REST request to count SchoolPictureGalleries by criteria: {}", criteria);
		return ResponseEntity.ok().body(schoolPictureGalleryQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /school-picture-galleries/:id} : get the "id"
	 * schoolPictureGallery.
	 *
	 * @param id the id of the schoolPictureGalleryDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the schoolPictureGalleryDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/school-picture-galleries/{id}")
	public ResponseEntity<SchoolPictureGalleryDTO> getSchoolPictureGallery(@PathVariable Long id) {
		log.debug("REST request to get SchoolPictureGallery : {}", id);
		Optional<SchoolPictureGalleryDTO> schoolPictureGalleryDTO = schoolPictureGalleryService.findOne(id);
		return ResponseUtil.wrapOrNotFound(schoolPictureGalleryDTO);
	}

}
