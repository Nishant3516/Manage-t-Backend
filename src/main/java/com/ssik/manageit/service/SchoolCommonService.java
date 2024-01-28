package com.ssik.manageit.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.criteria.SchoolClassCriteria;

import tech.jhipster.service.filter.LongFilter;

@Service
public class SchoolCommonService {
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private ClassStudentRepository classStudentRepository;
	@Autowired
	private SchoolClassQueryService schoolClassQueryService;
	@Autowired
	private ClassStudentQueryService classStudentQueryService;

	public ClassStudent getClassStudent(String loggedInUserName) {
		String userNameTuples[] = loggedInUserName.split("\\.");
		return classStudentRepository.findByStudentId(userNameTuples[0]);
	}

	public List<SchoolClass> getListOfClassForASchool(School school) {
		SchoolClassCriteria schoolClassCriteria = new SchoolClassCriteria();
		LongFilter schoolId = new LongFilter();
		schoolId.setEquals(school.getId());
		schoolClassCriteria.setSchoolId(schoolId);

		return schoolClassQueryService.findByCriteria(schoolClassCriteria);

	}

	public List<ClassStudent> getListOfStudentsForASchool(School school) {
		// TODO fix this, criteria not working
		List<ClassStudent> allStudents = new ArrayList<ClassStudent>();
		List<SchoolClass> schoolClasses = this.getListOfClassForASchool(school);

		for (SchoolClass schoolClass : schoolClasses) {
			ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
			LongFilter classId = new LongFilter();
			classId.setEquals(schoolClass.getId());
			classStudentCriteria.setSchoolClassId(classId);
			List<ClassStudent> students = classStudentQueryService.findByCriteria(classStudentCriteria);
			allStudents.addAll(students);
		}
		// List<Long> schoolClassesIds = schoolClasses.stream().map(sc ->
		// sc.getId()).collect(Collectors.toList());

//		ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
//		LongFilter classId = new LongFilter();
//		classId.setIn(schoolClassesIds);
//		classStudentCriteria.setSchoolClassId(classId);
//		List<ClassStudent> students = classStudentQueryService.findByCriteria(classStudentCriteria);

		return allStudents;

	}

	public List<Long> getListOfStudentIdsForASchool(School school) {
		List<SchoolClass> schoolClasses = this.getListOfClassForASchool(school);
		List<Long> schoolClassesIds = schoolClasses.stream().map(sc -> sc.getId()).collect(Collectors.toList());

		ClassStudentCriteria classStudentCriteria = new ClassStudentCriteria();
		LongFilter classId = new LongFilter();
		classId.setIn(schoolClassesIds);
		classStudentCriteria.setSchoolClassId(classId);

		List<ClassStudent> classStudents = classStudentQueryService.findByCriteria(classStudentCriteria);
		return classStudents.stream().map(cs -> cs.getId()).collect(Collectors.toList());

	}

	public School getUserSchool() {
		String userName = "";
		School foundSchool = new School();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			userName = userDetails.getUsername();
		} else {
			userName = principal.toString();
		}
		String userNameTuples[] = userName.split("\\.");
		if (userNameTuples.length > 1) {
			String schoolName = userNameTuples[userNameTuples.length - 1];
			foundSchool = schoolRepository.findBySchoolName(schoolName);

		}
		return foundSchool;
	}

	public String getLoggedinUser() {
		String userName = "";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			userName = userDetails.getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	public boolean isReportForMoreThanAdayAllowed(LocalDate startDate, LocalDate endDate) {
		Boolean isAdmin = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return true;
		}
		if (!isAdmin && ChronoUnit.DAYS.between(startDate, endDate) <= 2) {
			return true;
		}
		return false;
	}

}
