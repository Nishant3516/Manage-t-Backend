package com.ssik.manageit.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.repository.SubjectChapterRepository;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import com.ssik.manageit.service.mapper.SubjectChapterMapper;

/**
 * Service Implementation for managing {@link SubjectChapter}.
 */
@Service
@Transactional
public class SubjectChapterService {

	private final Logger log = LoggerFactory.getLogger(SubjectChapterService.class);

	private final SubjectChapterRepository subjectChapterRepository;

	private final SubjectChapterMapper subjectChapterMapper;

	public SubjectChapterService(SubjectChapterRepository subjectChapterRepository,
			SubjectChapterMapper subjectChapterMapper) {
		this.subjectChapterRepository = subjectChapterRepository;
		this.subjectChapterMapper = subjectChapterMapper;
	}

	/**
	 * Save a subjectChapter.
	 *
	 * @param subjectChapterDTO the entity to save.
	 * @return the persisted entity.
	 */
	public SubjectChapterDTO save(SubjectChapterDTO subjectChapterDTO) {
		log.debug("Request to save SubjectChapter : {}", subjectChapterDTO);
		SubjectChapter subjectChapter = subjectChapterMapper.toEntity(subjectChapterDTO);
		subjectChapter = subjectChapterRepository.save(subjectChapter);
		return subjectChapterMapper.toDto(subjectChapter);
	}

	/**
	 * Partially update a subjectChapter.
	 *
	 * @param subjectChapterDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<SubjectChapterDTO> partialUpdate(SubjectChapterDTO subjectChapterDTO) {
		log.debug("Request to partially update SubjectChapter : {}", subjectChapterDTO);

		return subjectChapterRepository.findById(subjectChapterDTO.getId()).map(existingSubjectChapter -> {
			subjectChapterMapper.partialUpdate(existingSubjectChapter, subjectChapterDTO);
			return existingSubjectChapter;
		}).map(subjectChapterRepository::save).map(subjectChapterMapper::toDto);
	}

	/**
	 * Get all the subjectChapters.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<SubjectChapterDTO> findAll(Pageable pageable) {
		log.debug("Request to get all SubjectChapters");
		return subjectChapterRepository.findAll(pageable).map(subjectChapterMapper::toDto);
	}

	/**
	 * Get one subjectChapter by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<SubjectChapter> findOne(Long id) {
		log.debug("Request to get SubjectChapter : {}", id);
		return subjectChapterRepository.findById(id);
	}

	/**
	 * Delete the subjectChapter by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete SubjectChapter : {}", id);
		subjectChapterRepository.deleteById(id);
	}
}
