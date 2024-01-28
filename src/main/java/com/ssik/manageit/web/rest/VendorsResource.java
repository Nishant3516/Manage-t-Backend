package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssik.manageit.domain.Vendors;
import com.ssik.manageit.repository.VendorsRepository;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.Vendors}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VendorsResource {

	private final Logger log = LoggerFactory.getLogger(VendorsResource.class);

	private static final String ENTITY_NAME = "vendors";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final VendorsRepository vendorsRepository;

	public VendorsResource(VendorsRepository vendorsRepository) {
		this.vendorsRepository = vendorsRepository;
	}

	/**
	 * {@code POST  /vendors} : Create a new vendors.
	 *
	 * @param vendors the vendors to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new vendors, or with status {@code 400 (Bad Request)} if the
	 *         vendors has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/vendors")
	public ResponseEntity<Vendors> createVendors(@Valid @RequestBody Vendors vendors) throws URISyntaxException {
		log.debug("REST request to save Vendors : {}", vendors);
		if (vendors.getId() != null) {
			throw new BadRequestAlertException("A new vendors cannot already have an ID", ENTITY_NAME, "idexists");
		}
		vendors.setCreateDate(LocalDate.now());
		Vendors result = vendorsRepository.save(vendors);
		return ResponseEntity
				.created(new URI("/api/vendors/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /vendors/:id} : Updates an existing vendors.
	 *
	 * @param id      the id of the vendors to save.
	 * @param vendors the vendors to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated vendors, or with status {@code 400 (Bad Request)} if the
	 *         vendors is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the vendors couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/vendors/{id}")
	public ResponseEntity<Vendors> updateVendors(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody Vendors vendors) throws URISyntaxException {
		log.debug("REST request to update Vendors : {}, {}", id, vendors);
		if (vendors.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, vendors.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!vendorsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		// vendors.set
		Vendors result = vendorsRepository.save(vendors);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendors.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /vendors/:id} : Partial updates given fields of an existing
	 * vendors, field will ignore if it is null
	 *
	 * @param id      the id of the vendors to save.
	 * @param vendors the vendors to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated vendors, or with status {@code 400 (Bad Request)} if the
	 *         vendors is not valid, or with status {@code 404 (Not Found)} if the
	 *         vendors is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the vendors couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/vendors/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Vendors> partialUpdateVendors(@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody Vendors vendors) throws URISyntaxException {
		log.debug("REST request to partial update Vendors partially : {}, {}", id, vendors);
		if (vendors.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, vendors.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!vendorsRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<Vendors> result = vendorsRepository.findById(vendors.getId()).map(existingVendors -> {
			if (vendors.getVendorPhoto() != null) {
				existingVendors.setVendorPhoto(vendors.getVendorPhoto());
			}
			if (vendors.getVendorPhotoContentType() != null) {
				existingVendors.setVendorPhotoContentType(vendors.getVendorPhotoContentType());
			}
			if (vendors.getVendorPhotoLink() != null) {
				existingVendors.setVendorPhotoLink(vendors.getVendorPhotoLink());
			}
			if (vendors.getVendorId() != null) {
				existingVendors.setVendorId(vendors.getVendorId());
			}
			if (vendors.getVendorName() != null) {
				existingVendors.setVendorName(vendors.getVendorName());
			}
			if (vendors.getPhoneNumber() != null) {
				existingVendors.setPhoneNumber(vendors.getPhoneNumber());
			}
			if (vendors.getDateOfBirth() != null) {
				existingVendors.setDateOfBirth(vendors.getDateOfBirth());
			}
			if (vendors.getAddressLine1() != null) {
				existingVendors.setAddressLine1(vendors.getAddressLine1());
			}
			if (vendors.getAddressLine2() != null) {
				existingVendors.setAddressLine2(vendors.getAddressLine2());
			}
			if (vendors.getNickName() != null) {
				existingVendors.setNickName(vendors.getNickName());
			}
			if (vendors.getEmail() != null) {
				existingVendors.setEmail(vendors.getEmail());
			}
			if (vendors.getCreateDate() != null) {
				existingVendors.setCreateDate(vendors.getCreateDate());
			}
			if (vendors.getCancelDate() != null) {
				existingVendors.setCancelDate(vendors.getCancelDate());
			}
			if (vendors.getVendorType() != null) {
				existingVendors.setVendorType(vendors.getVendorType());
			}

			return existingVendors;
		}).map(vendorsRepository::save);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendors.getId().toString()));
	}

	/**
	 * {@code GET  /vendors} : get all the vendors.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of vendors in body.
	 */
	@GetMapping("/vendors")
	public List<Vendors> getAllVendors() {
		log.debug("REST request to get all Vendors");
		return vendorsRepository.findAll();
	}

	/**
	 * {@code GET  /vendors/:id} : get the "id" vendors.
	 *
	 * @param id the id of the vendors to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the vendors, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/vendors/{id}")
	public ResponseEntity<Vendors> getVendors(@PathVariable Long id) {
		log.debug("REST request to get Vendors : {}", id);
		Optional<Vendors> vendors = vendorsRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(vendors);
	}

	/**
	 * {@code DELETE  /vendors/:id} : delete the "id" vendors.
	 *
	 * @param id the id of the vendors to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/vendors/{id}")
	public ResponseEntity<Void> deleteVendors(@PathVariable Long id) {
		log.debug("REST request to delete Vendors : {}", id);
		vendorsRepository.deleteById(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
