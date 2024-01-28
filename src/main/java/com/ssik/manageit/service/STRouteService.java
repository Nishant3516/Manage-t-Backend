package com.ssik.manageit.service;

import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.repository.STRouteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link STRoute}.
 */
@Service
@Transactional
public class STRouteService {

    private final Logger log = LoggerFactory.getLogger(STRouteService.class);

    private final STRouteRepository sTRouteRepository;

    public STRouteService(STRouteRepository sTRouteRepository) {
        this.sTRouteRepository = sTRouteRepository;
    }

    /**
     * Save a sTRoute.
     *
     * @param sTRoute the entity to save.
     * @return the persisted entity.
     */
    public STRoute save(STRoute sTRoute) {
        log.debug("Request to save STRoute : {}", sTRoute);
        return sTRouteRepository.save(sTRoute);
    }

    /**
     * Partially update a sTRoute.
     *
     * @param sTRoute the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<STRoute> partialUpdate(STRoute sTRoute) {
        log.debug("Request to partially update STRoute : {}", sTRoute);

        return sTRouteRepository
            .findById(sTRoute.getId())
            .map(existingSTRoute -> {
                if (sTRoute.getTransportRouteName() != null) {
                    existingSTRoute.setTransportRouteName(sTRoute.getTransportRouteName());
                }
                if (sTRoute.getRouteCharge() != null) {
                    existingSTRoute.setRouteCharge(sTRoute.getRouteCharge());
                }
                if (sTRoute.getTransportRouteAddress() != null) {
                    existingSTRoute.setTransportRouteAddress(sTRoute.getTransportRouteAddress());
                }
                if (sTRoute.getContactNumber() != null) {
                    existingSTRoute.setContactNumber(sTRoute.getContactNumber());
                }
                if (sTRoute.getCreateDate() != null) {
                    existingSTRoute.setCreateDate(sTRoute.getCreateDate());
                }
                if (sTRoute.getCancelDate() != null) {
                    existingSTRoute.setCancelDate(sTRoute.getCancelDate());
                }
                if (sTRoute.getRemarks() != null) {
                    existingSTRoute.setRemarks(sTRoute.getRemarks());
                }

                return existingSTRoute;
            })
            .map(sTRouteRepository::save);
    }

    /**
     * Get all the sTRoutes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<STRoute> findAll(Pageable pageable) {
        log.debug("Request to get all STRoutes");
        return sTRouteRepository.findAll(pageable);
    }

    /**
     * Get one sTRoute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<STRoute> findOne(Long id) {
        log.debug("Request to get STRoute : {}", id);
        return sTRouteRepository.findById(id);
    }

    /**
     * Delete the sTRoute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete STRoute : {}", id);
        sTRouteRepository.deleteById(id);
    }
}
