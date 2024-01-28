package com.ssik.manageit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.service.ClassFeeQueryService;
import com.ssik.manageit.service.ClassStudentQueryService;
import com.ssik.manageit.service.ClassStudentService;
import com.ssik.manageit.service.StudentDiscountService;
import com.ssik.manageit.service.criteria.ClassFeeCriteria;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.util.IdStoreUtil;
import com.ssik.manageit.web.rest.errors.BadRequestAlertException;

import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ssik.manageit.domain.ClassStudent}.
 */
@RestController
@RequestMapping("/api")
public class ClassStudentResource {

	private final Logger log = LoggerFactory.getLogger(ClassStudentResource.class);

	private static final String ENTITY_NAME = "classStudent";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ClassStudentService classStudentService;

	private final ClassStudentRepository classStudentRepository;

	@Autowired
	private ClassFeeQueryService classFeeQueryService;

	@Autowired
	private StudentDiscountService studentDiscountService;

	@Autowired
	IdStoreUtil idStoreUtil;

	private final ClassStudentQueryService classStudentQueryService;

	public ClassStudentResource(ClassStudentService classStudentService, ClassStudentRepository classStudentRepository,
			ClassStudentQueryService classStudentQueryService) {
		this.classStudentService = classStudentService;
		this.classStudentRepository = classStudentRepository;
		this.classStudentQueryService = classStudentQueryService;
	}

	/**
	 * {@code POST  /class-students} : Create a new classStudent.
	 *
	 * @param classStudentDTO the classStudentDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new classStudentDTO, or with status
	 *         {@code 400 (Bad Request)} if the classStudent has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/class-students")
	public ResponseEntity<ClassStudentDTO> createClassStudent(@Valid @RequestBody ClassStudentDTO classStudentDTO)
			throws URISyntaxException {
		log.debug("REST request to save ClassStudent : {}", classStudentDTO);
		if (classStudentDTO.getId() != null || classStudentDTO.getStudentId() != null) {
			throw new BadRequestAlertException("A new classStudent cannot already have an ID", ENTITY_NAME, "idexists");
		}
		String nextStudentId = "" + idStoreUtil.getAndSaveNextId(IdType.STUDENT);
		classStudentDTO.setStudentId(nextStudentId);
		classStudentDTO.setCreateDate(LocalDate.now());
		ClassStudentDTO result = classStudentService.save(classStudentDTO);

		addStudentDiscountForMissedMonths(result);
		return ResponseEntity
				.created(new URI("/api/class-students/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	private void addStudentDiscountForMissedMonths(ClassStudentDTO classStudentDTO) {
		// Add discount if the start date is later APR
		LocalDate feeStartDate = classStudentDTO.getStartDate();
		LocalDate createDate = classStudentDTO.getCreateDate();
		int startMonth = createDate.getMonthValue();
		if (feeStartDate != null) {
			// If fee start date has been provided , always override the record creation
			// date
			startMonth = feeStartDate.getMonthValue();
		}

		StudentDiscountDTO studentDiscountDto = null;
		if (startMonth > 4) {
			// for(int i=5;i<startMonth;i++) {
			ClassFeeCriteria classFeeCriteria = new ClassFeeCriteria();
			LongFilter schoolClassId = new LongFilter();
			schoolClassId.setEquals(classStudentDTO.getSchoolClass().getId());
			classFeeCriteria.setSchoolClassId(schoolClassId);

			List<ClassFeeDTO> classFeeDtos = classFeeQueryService.findByCriteria(classFeeCriteria);
			// Double totalAprDisc=0.0;
			// Double totalMayDisc=0.0;
			// Double totalJunDisc=0.0;
			// Double totalJulDisc=0.0;
			// Double totalAugDisc=0.0;
			// Double totalSepDisc=0.0;
			// Double totalOctDisc=0.0;
			// Double totalNovDisc=0.0;
			// Double totalDecDisc=0.0;

			for (ClassFeeDTO classFeeDto : classFeeDtos) {
				// totalAprDisc=totalAprDisc+classFeeDto.getAprFee();
				// totalMayDisc=totalMayDisc+classFeeDto.getMayFee();
				// totalJunDisc=totalJunDisc+classFeeDto.getJunFee();
				// totalJulDisc=totalJulDisc+classFeeDto.getJulFee();
				// totalAugDisc=totalAugDisc+classFeeDto.getAugFee();
				// totalSepDisc=totalSepDisc+classFeeDto.getSepFee();
				// totalOctDisc=totalOctDisc+classFeeDto.getOctFee();
				// totalNovDisc=totalNovDisc+classFeeDto.getNovFee();
				// totalDecDisc=totalDecDisc+classFeeDto.getDecFee();

				switch (startMonth) {
				case 5:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
							0.0, 0.0);
					break;
				case 6:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(), 0.0, 0.0, 0.0, 0.0,
							0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					break;
				case 7:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					break;
				case 8:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), classFeeDto.getJulFee(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					break;
				case 9:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), classFeeDto.getJulFee(), classFeeDto.getAugFee(), 0.0, 0.0, 0.0,
							0.0, 0.0, 0.0, 0.0);
					break;
				case 10:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), classFeeDto.getJulFee(), classFeeDto.getAugFee(),
							classFeeDto.getSepFee(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					break;
				case 11:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), classFeeDto.getJulFee(), classFeeDto.getAugFee(),
							classFeeDto.getSepFee(), classFeeDto.getOctFee(), 0.0, 0.0, 0.0, 0.0, 0.0);
					break;
				case 12:
					studentDiscountDto = new StudentDiscountDTO(classFeeDto.getSchoolLedgerHead(), classStudentDTO,
							FeeYear.YEAR_2023, 5, classFeeDto.getAprFee(), classFeeDto.getMayFee(),
							classFeeDto.getJunFee(), classFeeDto.getJulFee(), classFeeDto.getAugFee(),
							classFeeDto.getSepFee(), classFeeDto.getOctFee(), classFeeDto.getNovFee(), 0.0, 0.0, 0.0,
							0.0);
//                    case 1:
//                        studentDiscountDto =
//                            new StudentDiscountDTO(
//                                classFeeDto.getSchoolLedgerHead(),
//                                classStudentDTO,
//                                FeeYear.YEAR_2023,
//                                5,
//                                classFeeDto.getAprFee(),
//                                classFeeDto.getMayFee(),
//                                classFeeDto.getJunFee(),
//                                classFeeDto.getJulFee(),
//                                classFeeDto.getAugFee(),
//                                classFeeDto.getSepFee(),
//                                classFeeDto.getOctFee(),
//                                classFeeDto.getNovFee(),
//                                classFeeDto.getDecFee(),
//                                0.0,
//                                0.0,
//                                0.0
//                            );
//                    case 2:
//                        studentDiscountDto =
//                            new StudentDiscountDTO(
//                                classFeeDto.getSchoolLedgerHead(),
//                                classStudentDTO,
//                                FeeYear.YEAR_2023,
//                                5,
//                                classFeeDto.getAprFee(),
//                                classFeeDto.getMayFee(),
//                                classFeeDto.getJunFee(),
//                                classFeeDto.getJulFee(),
//                                classFeeDto.getAugFee(),
//                                classFeeDto.getSepFee(),
//                                classFeeDto.getOctFee(),
//                                classFeeDto.getNovFee(),
//                                classFeeDto.getDecFee(),
//                                classFeeDto.getJanFee(),
//                                0.0,
//                                0.0
//                            );
//                        
//                    case 3:
//                        studentDiscountDto =
//                            new StudentDiscountDTO(
//                                classFeeDto.getSchoolLedgerHead(),
//                                classStudentDTO,
//                                FeeYear.YEAR_2023,
//                                5,
//                                classFeeDto.getAprFee(),
//                                classFeeDto.getMayFee(),
//                                classFeeDto.getJunFee(),
//                                classFeeDto.getJulFee(),
//                                classFeeDto.getAugFee(),
//                                classFeeDto.getSepFee(),
//                                classFeeDto.getOctFee(),
//                                classFeeDto.getNovFee(),
//                                classFeeDto.getDecFee(),
//                                classFeeDto.getJanFee(),
//                                classFeeDto.getFebFee(),
//                                0.0
//                            );

				default:
					break;
				}
				if (studentDiscountDto != null) {
					studentDiscountService.save(studentDiscountDto);
				}
			}
			// }
		}
		// }

	}

	/**
	 * {@code PUT  /class-students/:id} : Updates an existing classStudent.
	 *
	 * @param id              the id of the classStudentDTO to save.
	 * @param classStudentDTO the classStudentDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classStudentDTO, or with status {@code 400 (Bad Request)}
	 *         if the classStudentDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the classStudentDTO couldn't
	 *         be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/class-students/{id}")
	public ResponseEntity<ClassStudentDTO> updateClassStudent(
			@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody ClassStudentDTO classStudentDTO) throws URISyntaxException {
		log.debug("REST request to update ClassStudent : {}, {}", id, classStudentDTO);
		if (classStudentDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classStudentDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classStudentRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classStudentDTO.setLastModified(LocalDate.now());
		ClassStudentDTO result = classStudentService.save(classStudentDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				classStudentDTO.getId().toString())).body(result);
	}

	/**
	 * {@code PATCH  /class-students/:id} : Partial updates given fields of an
	 * existing classStudent, field will ignore if it is null
	 *
	 * @param id              the id of the classStudentDTO to save.
	 * @param classStudentDTO the classStudentDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated classStudentDTO, or with status {@code 400 (Bad Request)}
	 *         if the classStudentDTO is not valid, or with status
	 *         {@code 404 (Not Found)} if the classStudentDTO is not found, or with
	 *         status {@code 500 (Internal Server Error)} if the classStudentDTO
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/class-students/{id}", consumes = "application/merge-patch+json")
	public ResponseEntity<ClassStudentDTO> partialUpdateClassStudent(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody ClassStudentDTO classStudentDTO) throws URISyntaxException {
		log.debug("REST request to partial update ClassStudent partially : {}, {}", id, classStudentDTO);
		if (classStudentDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, classStudentDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!classStudentRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}
		classStudentDTO.setLastModified(LocalDate.now());
		Optional<ClassStudentDTO> result = classStudentService.partialUpdate(classStudentDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true,
				ENTITY_NAME, classStudentDTO.getId().toString()));
	}

	/**
	 * {@code GET  /class-students} : get all the classStudents.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of classStudents in body.
	 */
	@GetMapping("/class-students")
	public ResponseEntity<List<ClassStudent>> getAllClassStudents(ClassStudentCriteria criteria, Pageable pageable) {
		log.debug("REST request to get ClassStudents by criteria: {}", criteria);
		List<ClassStudent> students = classStudentQueryService.findByCriteria(criteria, pageable);
		return ResponseEntity.ok().body(students);
	}

	/**
	 * {@code GET  /class-students/count} : count all the classStudents.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/class-students/count")
	public ResponseEntity<Long> countClassStudents(ClassStudentCriteria criteria) {
		log.debug("REST request to count ClassStudents by criteria: {}", criteria);
		return ResponseEntity.ok().body(classStudentQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /class-students/:id} : get the "id" classStudent.
	 *
	 * @param id the id of the classStudentDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the classStudentDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/class-students/{id}")
	public ResponseEntity<ClassStudentDTO> getClassStudent(@PathVariable Long id) {
		log.debug("REST request to get ClassStudent : {}", id);
		ClassStudentDTO classStudentDTO = classStudentService.getStudentWithClassDetails(id);
		return ResponseEntity.ok().body(classStudentDTO);
	}

	/**
	 * {@code DELETE  /class-students/:id} : delete the "id" classStudent.
	 *
	 * @param id the id of the classStudentDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/class-students/{id}")
	public ResponseEntity<Void> deleteClassStudent(@PathVariable Long id) {
		log.debug("REST request to delete ClassStudent : {}", id);
		// classStudentService.delete(id);
		String studentId = "" + id;
		ClassStudentDTO classStudentDto = classStudentService.getStudentWithClassDetails(id);
		if (classStudentDto != null) {
			if (classStudentDto.getCancelDate() == null) {
				classStudentDto.setCancelDate(LocalDate.now());
			} else {
				classStudentDto.setCancelDate(null);
			}
			classStudentService.save(classStudentDto);
			studentId = classStudentDto.getStudentId() != null ? classStudentDto.getStudentId() : "";
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, studentId)).build();
	}
}
