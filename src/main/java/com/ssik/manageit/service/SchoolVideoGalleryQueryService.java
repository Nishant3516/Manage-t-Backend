package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolVideoGallery;
import com.ssik.manageit.repository.SchoolVideoGalleryRepository;
import com.ssik.manageit.service.criteria.SchoolVideoGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolVideoGalleryMapper;
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
 * Service for executing complex queries for {@link SchoolVideoGallery} entities in the database.
 * The main input is a {@link SchoolVideoGalleryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolVideoGalleryDTO} or a {@link Page} of {@link SchoolVideoGalleryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolVideoGalleryQueryService extends QueryService<SchoolVideoGallery> {

    private final Logger log = LoggerFactory.getLogger(SchoolVideoGalleryQueryService.class);

    private final SchoolVideoGalleryRepository schoolVideoGalleryRepository;

    private final SchoolVideoGalleryMapper schoolVideoGalleryMapper;

    public SchoolVideoGalleryQueryService(
        SchoolVideoGalleryRepository schoolVideoGalleryRepository,
        SchoolVideoGalleryMapper schoolVideoGalleryMapper
    ) {
        this.schoolVideoGalleryRepository = schoolVideoGalleryRepository;
        this.schoolVideoGalleryMapper = schoolVideoGalleryMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolVideoGalleryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolVideoGalleryDTO> findByCriteria(SchoolVideoGalleryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SchoolVideoGallery> specification = createSpecification(criteria);
        return schoolVideoGalleryMapper.toDto(schoolVideoGalleryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolVideoGalleryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolVideoGalleryDTO> findByCriteria(SchoolVideoGalleryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolVideoGallery> specification = createSpecification(criteria);
        return schoolVideoGalleryRepository.findAll(specification, page).map(schoolVideoGalleryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolVideoGalleryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolVideoGallery> specification = createSpecification(criteria);
        return schoolVideoGalleryRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolVideoGalleryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolVideoGallery> createSpecification(SchoolVideoGalleryCriteria criteria) {
        Specification<SchoolVideoGallery> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolVideoGallery_.id));
            }
            if (criteria.getVideoTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideoTitle(), SchoolVideoGallery_.videoTitle));
            }
            if (criteria.getVideoDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getVideoDescription(), SchoolVideoGallery_.videoDescription));
            }
            if (criteria.getVideoLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideoLink(), SchoolVideoGallery_.videoLink));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolVideoGallery_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolVideoGallery_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolVideoGallery_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolVideoGallery_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
