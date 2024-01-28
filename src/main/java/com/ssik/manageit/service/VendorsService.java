package com.ssik.manageit.service;

import com.ssik.manageit.domain.Vendors;
import com.ssik.manageit.repository.VendorsRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendors}.
 */
@Service
@Transactional
public class VendorsService {

    private final Logger log = LoggerFactory.getLogger(VendorsService.class);

    private final VendorsRepository vendorsRepository;

    public VendorsService(VendorsRepository vendorsRepository) {
        this.vendorsRepository = vendorsRepository;
    }

    /**
     * Save a vendors.
     *
     * @param vendors the entity to save.
     * @return the persisted entity.
     */
    public Vendors save(Vendors vendors) {
        log.debug("Request to save Vendors : {}", vendors);
        return vendorsRepository.save(vendors);
    }

    /**
     * Partially update a vendors.
     *
     * @param vendors the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vendors> partialUpdate(Vendors vendors) {
        log.debug("Request to partially update Vendors : {}", vendors);

        return vendorsRepository
            .findById(vendors.getId())
            .map(existingVendors -> {
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
            })
            .map(vendorsRepository::save);
    }

    /**
     * Get all the vendors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Vendors> findAll() {
        log.debug("Request to get all Vendors");
        return vendorsRepository.findAll();
    }

    /**
     * Get one vendors by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vendors> findOne(Long id) {
        log.debug("Request to get Vendors : {}", id);
        return vendorsRepository.findById(id);
    }

    /**
     * Delete the vendors by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendors : {}", id);
        vendorsRepository.deleteById(id);
    }
}
