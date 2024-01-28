package com.ssik.manageit.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.School;
import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.service.dto.SchoolDTO;
import com.ssik.manageit.service.mapper.SchoolMapper;

/**
 * Service Implementation for managing {@link School}.
 */
@Service
@Transactional
public class SchoolService {

	private final Logger log = LoggerFactory.getLogger(SchoolService.class);

	private final SchoolRepository schoolRepository;

	private final SchoolMapper schoolMapper;
	// @Autowired
	// private SubjectChapterQueryService subjectChapterQueryService;

	public SchoolService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
		this.schoolRepository = schoolRepository;
		this.schoolMapper = schoolMapper;
	}

//    List<ChapterSection> getAllChapterSectionsForASubject(ClassSubject classSubject){
//    	
//    }
//    
//    List<SubjectChapter> getAllChpatersForASubject(Long subjectId){
//    	SubjectChapterCriteria subjectChapterCriteria = new SubjectChapterCriteria();
//    	LongFilter subjectFilter=new LongFilter();
//    	subjectFilter.setEquals(subjectId);
//    	
//    	subjectChapterCriteria.sets
//    	
//    }
	/**
	 * Save a school.
	 *
	 * @param schoolDTO the entity to save.
	 * @return the persisted entity.
	 */
	public SchoolDTO save(SchoolDTO schoolDTO) {
		log.debug("Request to save School : {}", schoolDTO);
		School school = schoolMapper.toEntity(schoolDTO);
		try {
			school = schoolRepository.save(school);
		} catch (Exception e) {
			log.error("Error in school repo call to save", e);
		}
		return schoolMapper.toDto(school);
	}

	/**
	 * Partially update a school.
	 *
	 * @param schoolDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<SchoolDTO> partialUpdate(SchoolDTO schoolDTO) {
		log.debug("Request to partially update School : {}", schoolDTO);

		return schoolRepository.findById(schoolDTO.getId()).map(existingSchool -> {
			schoolMapper.partialUpdate(existingSchool, schoolDTO);
			return existingSchool;
		}).map(schoolRepository::save).map(schoolMapper::toDto);
	}

	/**
	 * Get all the schools.
	 *
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public List<SchoolDTO> findAll() {
		log.debug("Request to get all Schools");
		return schoolRepository.findAll().stream().map(schoolMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * Get one school by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<SchoolDTO> findOne(Long id) {
		log.debug("Request to get School : {}", id);
		return schoolRepository.findById(id).map(schoolMapper::toDto);
	}

	/**
	 * Delete the school by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete School : {}", id);
		schoolRepository.deleteById(id);
	}
}
