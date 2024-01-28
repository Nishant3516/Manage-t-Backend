package com.ssik.manageit.service;

import com.ssik.manageit.domain.IdStore;
import com.ssik.manageit.repository.IdStoreRepository;
import com.ssik.manageit.service.dto.IdStoreDTO;
import com.ssik.manageit.service.mapper.IdStoreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IdStore}.
 */
@Service
@Transactional
public class IdStoreService {

    private final Logger log = LoggerFactory.getLogger(IdStoreService.class);

    private final IdStoreRepository idStoreRepository;

    private final IdStoreMapper idStoreMapper;

    public IdStoreService(IdStoreRepository idStoreRepository, IdStoreMapper idStoreMapper) {
        this.idStoreRepository = idStoreRepository;
        this.idStoreMapper = idStoreMapper;
    }

    /**
     * Save a idStore.
     *
     * @param idStoreDTO the entity to save.
     * @return the persisted entity.
     */
    public IdStoreDTO save(IdStoreDTO idStoreDTO) {
        log.debug("Request to save IdStore : {}", idStoreDTO);
        IdStore idStore = idStoreMapper.toEntity(idStoreDTO);
        idStore = idStoreRepository.save(idStore);
        return idStoreMapper.toDto(idStore);
    }

    /**
     * Partially update a idStore.
     *
     * @param idStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IdStoreDTO> partialUpdate(IdStoreDTO idStoreDTO) {
        log.debug("Request to partially update IdStore : {}", idStoreDTO);

        return idStoreRepository
            .findById(idStoreDTO.getId())
            .map(
                existingIdStore -> {
                    idStoreMapper.partialUpdate(existingIdStore, idStoreDTO);
                    return existingIdStore;
                }
            )
            .map(idStoreRepository::save)
            .map(idStoreMapper::toDto);
    }

    /**
     * Get all the idStores.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IdStoreDTO> findAll() {
        log.debug("Request to get all IdStores");
        return idStoreRepository.findAll().stream().map(idStoreMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one idStore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IdStoreDTO> findOne(Long id) {
        log.debug("Request to get IdStore : {}", id);
        return idStoreRepository.findById(id).map(idStoreMapper::toDto);
    }

    /**
     * Delete the idStore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IdStore : {}", id);
        idStoreRepository.deleteById(id);
    }
}
