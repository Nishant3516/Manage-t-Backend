package com.ssik.manageit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.repository.ChapterSectionRepository;
import com.ssik.manageit.service.criteria.ChapterSectionCriteria;
import com.ssik.manageit.service.criteria.ClassSubjectCriteria;
import com.ssik.manageit.service.criteria.SubjectChapterCriteria;
import com.ssik.manageit.service.dto.ChapterSectionDTO;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import com.ssik.manageit.service.mapper.ChapterSectionMapper;

import tech.jhipster.service.filter.LongFilter;

/**
 * Service Implementation for managing {@link ChapterSection}.
 */
@Service
@Transactional
public class ChapterSectionService {

	private final Logger log = LoggerFactory.getLogger(ChapterSectionService.class);

	private final ChapterSectionRepository chapterSectionRepository;

	private final ChapterSectionMapper chapterSectionMapper;
	private final ClassSubjectQueryService classSubjectQueryService;
	private final SubjectChapterQueryService subjectChapterQueryService;
	private final ChapterSectionQueryService chapterSectionQueryService;

	public ChapterSectionService(ChapterSectionRepository chapterSectionRepository,
			ChapterSectionMapper chapterSectionMapper, ClassSubjectQueryService classSubjectQueryService,
			SubjectChapterQueryService subjectChapterQueryService,
			ChapterSectionQueryService chapterSectionQueryService) {
		this.chapterSectionRepository = chapterSectionRepository;
		this.chapterSectionMapper = chapterSectionMapper;
		this.classSubjectQueryService = classSubjectQueryService;
		this.subjectChapterQueryService = subjectChapterQueryService;
		this.chapterSectionQueryService = chapterSectionQueryService;
	}

	/**
	 * Save a chapterSection.
	 *
	 * @param chapterSectionDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ChapterSectionDTO save(ChapterSectionDTO chapterSectionDTO) {
		log.debug("Request to save ChapterSection : {}", chapterSectionDTO);
		ChapterSection chapterSection = chapterSectionMapper.toEntity(chapterSectionDTO);
		chapterSection = chapterSectionRepository.save(chapterSection);
		return chapterSectionMapper.toDto(chapterSection);
	}

	/**
	 * Partially update a chapterSection.
	 *
	 * @param chapterSectionDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ChapterSectionDTO> partialUpdate(ChapterSectionDTO chapterSectionDTO) {
		log.debug("Request to partially update ChapterSection : {}", chapterSectionDTO);

		return chapterSectionRepository.findById(chapterSectionDTO.getId()).map(existingChapterSection -> {
			chapterSectionMapper.partialUpdate(existingChapterSection, chapterSectionDTO);
			return existingChapterSection;
		}).map(chapterSectionRepository::save).map(chapterSectionMapper::toDto);
	}

	/**
	 * Get all the chapterSections.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ChapterSectionDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ChapterSections");
		return chapterSectionRepository.findAll(pageable).map(chapterSectionMapper::toDto);
	}

	/**
	 * Get one chapterSection by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ChapterSection> findOne(Long id) {
		log.debug("Request to get ChapterSection : {}", id);
		return chapterSectionRepository.findById(id);
	}

	/**
	 * Delete the chapterSection by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ChapterSection : {}", id);
		chapterSectionRepository.deleteById(id);
	}

	public List<Long> getAllSectionsForAclassAndSubject(Long classId, @RequestParam(required = false) Long subjectId) {
		// Get all the subjects for a given class
		ClassSubjectCriteria classSubjectCriteria = new ClassSubjectCriteria();
		LongFilter classIdFilter = new LongFilter();
		classIdFilter.setEquals(classId);
		classSubjectCriteria.setSchoolClassId(classIdFilter);
		List<ClassSubjectDTO> classSubjects = classSubjectQueryService.findByCriteria(classSubjectCriteria);

		// collect all the subject ID
		List<Long> subjectIds = new ArrayList<Long>();
		if (subjectId != null) {
			subjectIds = classSubjects.stream().filter(cs -> cs.getId().equals(subjectId)).map(ClassSubjectDTO::getId)
					.collect(Collectors.toList());

		} else {
			// no subject Id has been passed so get all the list of subject taught in the
			// class
			subjectIds = classSubjects.stream().map(ClassSubjectDTO::getId).collect(Collectors.toList());

		}

		// get all the chapters for a given subject if subject ID has been passed
		SubjectChapterCriteria subjectChapterCriteria = new SubjectChapterCriteria();
		LongFilter classSubjectFilter = new LongFilter();
		classSubjectFilter.setIn(subjectIds);
		subjectChapterCriteria.setClassSubjectId(classSubjectFilter);
		List<SubjectChapterDTO> subjectChapters = subjectChapterQueryService.findByCriteria(subjectChapterCriteria);
		List<Long> chapterIds = subjectChapters.stream().map(SubjectChapterDTO::getId).collect(Collectors.toList());

		ChapterSectionCriteria chapterSectionCriteria = new ChapterSectionCriteria();
		LongFilter chapterSectionFilter = new LongFilter();
		chapterSectionFilter.setIn(chapterIds);
		chapterSectionCriteria.setSubjectChapterId(chapterSectionFilter);
		List<ChapterSectionDTO> chapterSections = chapterSectionQueryService.findByCriteria(chapterSectionCriteria);
		List<Long> sectionIds = chapterSections.stream().map(ChapterSectionDTO::getId).collect(Collectors.toList());

		return sectionIds;
	}
}
