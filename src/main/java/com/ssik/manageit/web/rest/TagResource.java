package com.ssik.manageit.web.rest;

import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.repository.TagRepository;
import com.ssik.manageit.service.TagQueryService;
import com.ssik.manageit.service.TagService;
import com.ssik.manageit.service.criteria.TagCriteria;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.Tag}.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

	private final Logger log = LoggerFactory.getLogger(TagResource.class);

	private static final String ENTITY_NAME = "tag";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final TagService tagService;

	private final TagRepository tagRepository;

	private final TagQueryService tagQueryService;

	public TagResource(TagService tagService, TagRepository tagRepository, TagQueryService tagQueryService) {
		this.tagService = tagService;
		this.tagRepository = tagRepository;
		this.tagQueryService = tagQueryService;
	}

	/**
	 * {@code POST  /tags} : Create a new tag.
	 *
	 * @param tag the tag to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new tag, or with status {@code 400 (Bad Request)} if the tag
	 *         has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/tags")
	public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws URISyntaxException {
		log.debug("REST request to save Tag : {}", tag);
		if (tag.getId() != null) {
			throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Tag result = tagService.save(tag);
		return ResponseEntity
				.created(new URI("/api/tags/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /tags/:id} : Updates an existing tag.
	 *
	 * @param id  the id of the tag to save.
	 * @param tag the tag to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated tag, or with status {@code 400 (Bad Request)} if the tag
	 *         is not valid, or with status {@code 500 (Internal Server Error)} if
	 *         the tag couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/tags/{id}")
	public ResponseEntity<Tag> updateTag(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody Tag tag) throws URISyntaxException {
		log.debug("REST request to update Tag : {}, {}", id, tag);
		if (tag.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, tag.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!tagRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Tag result = tagService.save(tag);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tag.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /tags/:id} : Partial updates given fields of an existing tag,
	 * field will ignore if it is null
	 *
	 * @param id  the id of the tag to save.
	 * @param tag the tag to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated tag, or with status {@code 400 (Bad Request)} if the tag
	 *         is not valid, or with status {@code 404 (Not Found)} if the tag is
	 *         not found, or with status {@code 500 (Internal Server Error)} if the
	 *         tag couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Tag> partialUpdateTag(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody Tag tag) throws URISyntaxException {
		log.debug("REST request to partial update Tag partially : {}, {}", id, tag);
		if (tag.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, tag.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!tagRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<Tag> result = tagService.partialUpdate(tag);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tag.getId().toString()));
	}

	/**
	 * {@code GET  /tags} : get all the tags.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of tags in body.
	 */
	@GetMapping("/tags")
	public ResponseEntity<List<Tag>> getAllTags(TagCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Tags by criteria: {}", criteria);
		Page<Tag> page = tagQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /tags/count} : count all the tags.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/tags/count")
	public ResponseEntity<Long> countTags(TagCriteria criteria) {
		log.debug("REST request to count Tags by criteria: {}", criteria);
		return ResponseEntity.ok().body(tagQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /tags/:id} : get the "id" tag.
	 *
	 * @param id the id of the tag to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the tag, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/tags/{id}")
	public ResponseEntity<Tag> getTag(@PathVariable Long id) {
		log.debug("REST request to get Tag : {}", id);
		Optional<Tag> tag = tagService.findOne(id);
		return ResponseUtil.wrapOrNotFound(tag);
	}

	/**
	 * {@code DELETE  /tags/:id} : delete the "id" tag.
	 *
	 * @param id the id of the tag to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/tags/{id}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
		log.debug("REST request to delete Tag : {}", id);
		tagService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
