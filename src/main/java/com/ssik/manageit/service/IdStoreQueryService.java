package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.IdStore;
import com.ssik.manageit.repository.IdStoreRepository;
import com.ssik.manageit.service.criteria.IdStoreCriteria;
import com.ssik.manageit.service.dto.IdStoreDTO;
import com.ssik.manageit.service.mapper.IdStoreMapper;
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

/**
 * Service for executing complex queries for {@link IdStore} entities in the database.
 * The main input is a {@link IdStoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IdStoreDTO} or a {@link Page} of {@link IdStoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IdStoreQueryService extends QueryService<IdStore> {

    private final Logger log = LoggerFactory.getLogger(IdStoreQueryService.class);

    private final IdStoreRepository idStoreRepository;

    private final IdStoreMapper idStoreMapper;

    public IdStoreQueryService(IdStoreRepository idStoreRepository, IdStoreMapper idStoreMapper) {
        this.idStoreRepository = idStoreRepository;
        this.idStoreMapper = idStoreMapper;
    }

    /**
     * Return a {@link List} of {@link IdStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IdStoreDTO> findByCriteria(IdStoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IdStore> specification = createSpecification(criteria);
        return idStoreMapper.toDto(idStoreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IdStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IdStoreDTO> findByCriteria(IdStoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IdStore> specification = createSpecification(criteria);
        return idStoreRepository.findAll(specification, page).map(idStoreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IdStoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IdStore> specification = createSpecification(criteria);
        return idStoreRepository.count(specification);
    }

    /**
     * Function to convert {@link IdStoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IdStore> createSpecification(IdStoreCriteria criteria) {
        Specification<IdStore> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IdStore_.id));
            }
            if (criteria.getEntrytype() != null) {
                specification = specification.and(buildSpecification(criteria.getEntrytype(), IdStore_.entrytype));
            }
            if (criteria.getLastGeneratedId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastGeneratedId(), IdStore_.lastGeneratedId));
            }
            if (criteria.getStartId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartId(), IdStore_.startId));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), IdStore_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), IdStore_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), IdStore_.cancelDate));
            }
            if (criteria.getSchoolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSchoolId(), root -> root.join(IdStore_.school, JoinType.LEFT).get(School_.id))
                    );
            }
        }
        return specification;
    }
}
