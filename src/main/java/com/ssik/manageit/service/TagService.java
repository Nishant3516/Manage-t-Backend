package com.ssik.manageit.service;

import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.repository.TagRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Save a tag.
     *
     * @param tag the entity to save.
     * @return the persisted entity.
     */
    public Tag save(Tag tag) {
        log.debug("Request to save Tag : {}", tag);
        return tagRepository.save(tag);
    }

    /**
     * Partially update a tag.
     *
     * @param tag the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tag> partialUpdate(Tag tag) {
        log.debug("Request to partially update Tag : {}", tag);

        return tagRepository
            .findById(tag.getId())
            .map(existingTag -> {
                if (tag.getTagText() != null) {
                    existingTag.setTagText(tag.getTagText());
                }
                if (tag.getTagLevel() != null) {
                    existingTag.setTagLevel(tag.getTagLevel());
                }
                if (tag.getCreateDate() != null) {
                    existingTag.setCreateDate(tag.getCreateDate());
                }
                if (tag.getLastModified() != null) {
                    existingTag.setLastModified(tag.getLastModified());
                }
                if (tag.getCancelDate() != null) {
                    existingTag.setCancelDate(tag.getCancelDate());
                }

                return existingTag;
            })
            .map(tagRepository::save);
    }

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAll(pageable);
    }

    /**
     * Get one tag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tag> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id);
    }

    /**
     * Delete the tag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }
}
