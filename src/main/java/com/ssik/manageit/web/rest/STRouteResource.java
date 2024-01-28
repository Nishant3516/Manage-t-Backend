package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.repository.STRouteRepository;
import com.ssik.manageit.service.STRouteQueryService;
import com.ssik.manageit.service.STRouteService;
import com.ssik.manageit.service.SchoolCommonService;
import com.ssik.manageit.service.criteria.STRouteCriteria;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.STRoute}.
 */
@RestController
@RequestMapping("/api")
public class STRouteResource {

	private final Logger log = LoggerFactory.getLogger(STRouteResource.class);

	private static final String ENTITY_NAME = "sTRoute";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final STRouteService sTRouteService;

	@Autowired
	private SchoolCommonService schoolCommonService;

	private final STRouteQueryService sTRouteQueryService;

	public STRouteResource(STRouteService sTRouteService, STRouteQueryService sTRouteQueryService) {
		this.sTRouteService = sTRouteService;
		this.sTRouteQueryService = sTRouteQueryService;
	}

	/**
	 * {@code POST  /st-routes} : Create a new sTRoute.
	 *
	 * @param sTRoute the sTRoute to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new sTRoute, or with status {@code 400 (Bad Request)} if the
	 *         sTRoute has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/st-routes")
	public ResponseEntity<STRoute> createSTRoute(@Valid @RequestBody STRoute sTRoute) throws URISyntaxException {
		log.debug("REST request to save STRoute : {}", sTRoute);
		if (sTRoute.getId() != null) {
			throw new BadRequestAlertException("A new sTRoute cannot already have an ID", ENTITY_NAME, "idexists");
		}
		sTRoute.setCreateDate(LocalDate.now());
		School school = schoolCommonService.getUserSchool();
		sTRoute.setSchool(school);
		sTRoute.setTenant(school.getTenant());
		STRoute result = sTRouteService.save(sTRoute);
		return ResponseEntity
				.created(new URI("/api/st-routes/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /st-routes/:id} : Updates an existing sTRoute.
	 *
	 * @param id      the id of the sTRoute to save.
	 * @param sTRoute the sTRoute to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated sTRoute, or with status {@code 400 (Bad Request)} if the
	 *         sTRoute is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the sTRoute couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PutMapping("/st-routes/{id}")
//	public ResponseEntity<STRoute> updateSTRoute(@PathVariable(value = "id", required = false) final Long id,
//			@Valid @RequestBody STRoute sTRoute) throws URISyntaxException {
//		log.debug("REST request to update STRoute : {}, {}", id, sTRoute);
//		if (sTRoute.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, sTRoute.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!sTRouteRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		STRoute result = sTRouteService.save(sTRoute);
//		return ResponseEntity.ok().headers(
//				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sTRoute.getId().toString()))
//				.body(result);
//	}

	/**
	 * {@code PATCH  /st-routes/:id} : Partial updates given fields of an existing
	 * sTRoute, field will ignore if it is null
	 *
	 * @param id      the id of the sTRoute to save.
	 * @param sTRoute the sTRoute to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated sTRoute, or with status {@code 400 (Bad Request)} if the
	 *         sTRoute is not valid, or with status {@code 404 (Not Found)} if the
	 *         sTRoute is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the sTRoute couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
//	@PatchMapping(value = "/st-routes/{id}", consumes = { "application/json", "application/merge-patch+json" })
//	public ResponseEntity<STRoute> partialUpdateSTRoute(@PathVariable(value = "id", required = false) final Long id,
//			@NotNull @RequestBody STRoute sTRoute) throws URISyntaxException {
//		log.debug("REST request to partial update STRoute partially : {}, {}", id, sTRoute);
//		if (sTRoute.getId() == null) {
//			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//		}
//		if (!Objects.equals(id, sTRoute.getId())) {
//			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//		}
//
//		if (!sTRouteRepository.existsById(id)) {
//			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//		}
//
//		Optional<STRoute> result = sTRouteService.partialUpdate(sTRoute);
//
//		return ResponseUtil.wrapOrNotFound(result,
//				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sTRoute.getId().toString()));
//	}

	/**
	 * {@code GET  /st-routes} : get all the sTRoutes.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of sTRoutes in body.
	 */
	@GetMapping("/st-routes")
	public ResponseEntity<List<STRoute>> getAllSTRoutes(STRouteCriteria criteria, Pageable pageable) {
		log.debug("REST request to get STRoutes by criteria: {}", criteria);
		Page<STRoute> page = sTRouteQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /st-routes/count} : count all the sTRoutes.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/st-routes/count")
	public ResponseEntity<Long> countSTRoutes(STRouteCriteria criteria) {
		log.debug("REST request to count STRoutes by criteria: {}", criteria);
		return ResponseEntity.ok().body(sTRouteQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /st-routes/:id} : get the "id" sTRoute.
	 *
	 * @param id the id of the sTRoute to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the sTRoute, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/st-routes/{id}")
	public ResponseEntity<STRoute> getSTRoute(@PathVariable Long id) {
		log.debug("REST request to get STRoute : {}", id);
		Optional<STRoute> sTRoute = sTRouteService.findOne(id);
		return ResponseUtil.wrapOrNotFound(sTRoute);
	}

	/**
	 * {@code DELETE  /st-routes/:id} : delete the "id" sTRoute.
	 *
	 * @param id the id of the sTRoute to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/st-routes/{id}")
	public ResponseEntity<Void> deleteSTRoute(@PathVariable Long id) {
		log.debug("REST request to delete STRoute : {}", id);
		sTRouteService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
