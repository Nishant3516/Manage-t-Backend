package com.ssik.manageit.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;

/**
 * Service Implementation for managing {@link ClassStudent}.
 */
@Service
@Transactional
public class ClassStudentService {

	private final Logger log = LoggerFactory.getLogger(ClassStudentService.class);

	private final ClassStudentRepository classStudentRepository;

	private final ClassStudentMapper classStudentMapper;

	@Autowired
	private SchoolClassService schoolClassService;

	public ClassStudentService(ClassStudentRepository classStudentRepository, ClassStudentMapper classStudentMapper) {
		this.classStudentRepository = classStudentRepository;
		this.classStudentMapper = classStudentMapper;
	}

	/**
	 * Save a classStudent.
	 *
	 * @param classStudentDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ClassStudentDTO save(ClassStudentDTO classStudentDTO) {
		log.debug("Request to save ClassStudent : {}", classStudentDTO);
		ClassStudent classStudent = classStudentMapper.toEntity(classStudentDTO);
		try {
			classStudent = classStudentRepository.save(classStudent);
		} catch (Exception e) {
			System.out.println("Exception" + e);
		}
		return classStudentMapper.toDto(classStudent);
	}

	/**
	 * Partially update a classStudent.
	 *
	 * @param classStudentDTO the entity to update partially.
	 * @return the persisted entity.
	 */
	public Optional<ClassStudentDTO> partialUpdate(ClassStudentDTO classStudentDTO) {
		log.debug("Request to partially update ClassStudent : {}", classStudentDTO);

		return classStudentRepository.findById(classStudentDTO.getId()).map(existingClassStudent -> {
			classStudentMapper.partialUpdate(existingClassStudent, classStudentDTO);
			return existingClassStudent;
		}).map(classStudentRepository::save).map(classStudentMapper::toDto);
	}

	/**
	 * Get all the classStudents.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ClassStudentDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ClassStudents");
		return classStudentRepository.findAll(pageable).map(classStudentMapper::toDto);
	}

	@Transactional(readOnly = true)
	public List<ClassStudent> findAllStudents() {
		log.debug("Request to get all ClassStudents");

		return classStudentRepository.findAll();
	}

	/**
	 * Get one classStudent by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ClassStudentDTO> findOne(Long id) {
		log.debug("Request to get ClassStudent : {}", id);
		return classStudentRepository.findById(id).map(classStudentMapper::toDto);
	}

	/**
	 * Delete the classStudent by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete ClassStudent : {}", id);
		classStudentRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public ClassStudentDTO getStudentWithClassDetails(Long id) {
		ClassStudentDTO classStudentDTO = null;
		Optional<ClassStudentDTO> classStudentDtoOpt = this.findOne(id);
		if (classStudentDtoOpt.isPresent()) {
			classStudentDTO = classStudentDtoOpt.get();
			if (classStudentDTO.getCancelDate() == null) {
				Optional<SchoolClassDTO> schoolClassDtoOpt = schoolClassService
						.findOne(classStudentDTO.getSchoolClass().getId());
				if (schoolClassDtoOpt.isPresent()) {
					classStudentDTO.setSchoolClass(schoolClassDtoOpt.get());
				}
			}
		}

		return classStudentDTO;
	}

//	@Transactional(readOnly = true)
//	public Page<ClassStudentDTO> getAllStudentWithClassDetails(Specification<ClassStudent> specification,
//			Pageable page) {
//		Page<ClassStudentDTO> classStudentsPage = classStudentRepository.findAll(specification, page)
//				.map(classStudentMapper::toDto);
//		List<ClassStudentDTO> classStudentsDto = classStudentsPage.getContent();
//		for (ClassStudentDTO classStudenDto : classStudentsDto) {
//			if (classStudenDto.getCancelDate() == null) {
//				Optional<SchoolClassDTO> schoolClassDtoOpt = schoolClassService
//						.findOne(classStudenDto.getSchoolClass().getId());
//				if (schoolClassDtoOpt.isPresent()) {
//					classStudenDto.setSchoolClass(schoolClassDtoOpt.get());
//				}
//			} else {
//				log.debug("It is a mess why the hell with a cancel date has been selected");
//			}
//		}
//
//		return classStudentsPage;
//	}

}
