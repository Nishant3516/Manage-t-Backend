package com.ssik.manageit.service;

import com.ssik.manageit.domain.*; // for static metamodels
import com.ssik.manageit.domain.SchoolPictureGallery;
import com.ssik.manageit.repository.SchoolPictureGalleryRepository;
import com.ssik.manageit.service.criteria.SchoolPictureGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolPictureGalleryMapper;
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
 * Service for executing complex queries for {@link SchoolPictureGallery} entities in the database.
 * The main input is a {@link SchoolPictureGalleryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolPictureGalleryDTO} or a {@link Page} of {@link SchoolPictureGalleryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolPictureGalleryQueryService extends QueryService<SchoolPictureGallery> {

    private final Logger log = LoggerFactory.getLogger(SchoolPictureGalleryQueryService.class);

    private final SchoolPictureGalleryRepository schoolPictureGalleryRepository;

    private final SchoolPictureGalleryMapper schoolPictureGalleryMapper;

    public SchoolPictureGalleryQueryService(
        SchoolPictureGalleryRepository schoolPictureGalleryRepository,
        SchoolPictureGalleryMapper schoolPictureGalleryMapper
    ) {
        this.schoolPictureGalleryRepository = schoolPictureGalleryRepository;
        this.schoolPictureGalleryMapper = schoolPictureGalleryMapper;
    }

    /**
     * Return a {@link List} of {@link SchoolPictureGalleryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolPictureGalleryDTO> findByCriteria(SchoolPictureGalleryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SchoolPictureGallery> specification = createSpecification(criteria);
        return schoolPictureGalleryMapper.toDto(schoolPictureGalleryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SchoolPictureGalleryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolPictureGalleryDTO> findByCriteria(SchoolPictureGalleryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolPictureGallery> specification = createSpecification(criteria);
        return schoolPictureGalleryRepository.findAll(specification, page).map(schoolPictureGalleryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolPictureGalleryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolPictureGallery> specification = createSpecification(criteria);
        return schoolPictureGalleryRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolPictureGalleryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolPictureGallery> createSpecification(SchoolPictureGalleryCriteria criteria) {
        Specification<SchoolPictureGallery> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolPictureGallery_.id));
            }
            if (criteria.getPictureTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPictureTitle(), SchoolPictureGallery_.pictureTitle));
            }
            if (criteria.getPictureDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPictureDescription(), SchoolPictureGallery_.pictureDescription));
            }
            if (criteria.getPictureLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPictureLink(), SchoolPictureGallery_.pictureLink));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SchoolPictureGallery_.createDate));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), SchoolPictureGallery_.lastModified));
            }
            if (criteria.getCancelDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelDate(), SchoolPictureGallery_.cancelDate));
            }
            if (criteria.getSchoolClassId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSchoolClassId(),
                            root -> root.join(SchoolPictureGallery_.schoolClasses, JoinType.LEFT).get(SchoolClass_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
