package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.BloodGroup;
import com.ssik.manageit.domain.enumeration.Gender;

/**
 * A ClassStudent.
 */
@Entity
@Table(name = "class_student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassStudent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Lob
	@Column(name = "student_photo")
	private byte[] studentPhoto;

	@Column(name = "student_photo_content_type")
	private String studentPhotoContentType;

	@Column(name = "student_photo_link")
	private String studentPhotoLink;

	@Column(name = "student_id")
	private String studentId;

	@NotNull
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "roll_number")
	private String rollNumber;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "blood_group")
	private BloodGroup bloodGroup;

	@Column(name = "date_of_birth")
	private Instant dateOfBirth;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "address_line_1")
	private String addressLine1;

	@Column(name = "address_line_2")
	private String addressLine2;

	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "mother_name")
	private String motherName;

	@Column(name = "email")
	private String email;

	@Column(name = "admission_date")
	private LocalDate admissionDate;

	@Column(name = "reg_number")
	private String regNumber;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentDiscount> studentDiscounts = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentAdditionalCharges> studentAdditionalCharges = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentChargesSummary> studentChargesSummaries = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent" }, allowSetters = true)
	private Set<StudentPayments> studentPayments = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent" }, allowSetters = true)
	private Set<StudentAttendence> studentAttendences = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent", "classHomeWork" }, allowSetters = true)
	private Set<StudentHomeWorkTrack> studentHomeWorkTracks = new HashSet<>();

	@OneToMany(mappedBy = "classStudent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent", "classClassWork" }, allowSetters = true)
	private Set<StudentClassWorkTrack> studentClassWorkTracks = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classStudents", "classLessionPlans", "school", "schoolNotifications", "classFees",
			"classSubjects", "schoolUsers", "schoolDaysOffs", "schoolEvents", "schoolPictureGalleries",
			"vchoolVideoGalleries", "schoolReports", }, allowSetters = true)
	private SchoolClass schoolClass;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClassStudent id(Long id) {
		this.id = id;
		return this;
	}

	public byte[] getStudentPhoto() {
		return this.studentPhoto;
	}

	public ClassStudent studentPhoto(byte[] studentPhoto) {
		this.studentPhoto = studentPhoto;
		return this;
	}

	public void setStudentPhoto(byte[] studentPhoto) {
		this.studentPhoto = studentPhoto;
	}

	public String getStudentPhotoContentType() {
		return this.studentPhotoContentType;
	}

	public ClassStudent studentPhotoContentType(String studentPhotoContentType) {
		this.studentPhotoContentType = studentPhotoContentType;
		return this;
	}

	public void setStudentPhotoContentType(String studentPhotoContentType) {
		this.studentPhotoContentType = studentPhotoContentType;
	}

	public String getStudentPhotoLink() {
		return this.studentPhotoLink;
	}

	public ClassStudent studentPhotoLink(String studentPhotoLink) {
		this.studentPhotoLink = studentPhotoLink;
		return this;
	}

	public void setStudentPhotoLink(String studentPhotoLink) {
		this.studentPhotoLink = studentPhotoLink;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public ClassStudent studentId(String studentId) {
		this.studentId = studentId;
		return this;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public ClassStudent firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Gender getGender() {
		return this.gender;
	}

	public ClassStudent gender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return this.lastName;
	}

	public ClassStudent lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRollNumber() {
		return this.rollNumber;
	}

	public ClassStudent rollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
		return this;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public ClassStudent phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BloodGroup getBloodGroup() {
		return this.bloodGroup;
	}

	public ClassStudent bloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
		return this;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Instant getDateOfBirth() {
		return this.dateOfBirth;
	}

	public ClassStudent dateOfBirth(Instant dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public void setDateOfBirth(Instant dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public ClassStudent startDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getAddressLine1() {
		return this.addressLine1;
	}

	public ClassStudent addressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
		return this;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}

	public ClassStudent addressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
		return this;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getNickName() {
		return this.nickName;
	}

	public ClassStudent nickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public ClassStudent fatherName(String fatherName) {
		this.fatherName = fatherName;
		return this;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return this.motherName;
	}

	public ClassStudent motherName(String motherName) {
		this.motherName = motherName;
		return this;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getEmail() {
		return this.email;
	}

	public ClassStudent email(String email) {
		this.email = email;
		return this;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getAdmissionDate() {
		return this.admissionDate;
	}

	public ClassStudent admissionDate(LocalDate admissionDate) {
		this.admissionDate = admissionDate;
		return this;
	}

	public void setAdmissionDate(LocalDate admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getRegNumber() {
		return this.regNumber;
	}

	public ClassStudent regNumber(String regNumber) {
		this.regNumber = regNumber;
		return this;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public ClassStudent endDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public ClassStudent createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public ClassStudent lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public ClassStudent cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<StudentDiscount> getStudentDiscounts() {
		return this.studentDiscounts;
	}

	public ClassStudent studentDiscounts(Set<StudentDiscount> studentDiscounts) {
		this.setStudentDiscounts(studentDiscounts);
		return this;
	}

	public ClassStudent addStudentDiscount(StudentDiscount studentDiscount) {
		this.studentDiscounts.add(studentDiscount);
		studentDiscount.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentDiscount(StudentDiscount studentDiscount) {
		this.studentDiscounts.remove(studentDiscount);
		studentDiscount.setClassStudent(null);
		return this;
	}

	public void setStudentDiscounts(Set<StudentDiscount> studentDiscounts) {
		if (this.studentDiscounts != null) {
			this.studentDiscounts.forEach(i -> i.setClassStudent(null));
		}
		if (studentDiscounts != null) {
			studentDiscounts.forEach(i -> i.setClassStudent(this));
		}
		this.studentDiscounts = studentDiscounts;
	}

	public Set<StudentAdditionalCharges> getStudentAdditionalCharges() {
		return this.studentAdditionalCharges;
	}

	public ClassStudent studentAdditionalCharges(Set<StudentAdditionalCharges> studentAdditionalCharges) {
		this.setStudentAdditionalCharges(studentAdditionalCharges);
		return this;
	}

	public ClassStudent addStudentAdditionalCharges(StudentAdditionalCharges studentAdditionalCharges) {
		this.studentAdditionalCharges.add(studentAdditionalCharges);
		studentAdditionalCharges.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentAdditionalCharges(StudentAdditionalCharges studentAdditionalCharges) {
		this.studentAdditionalCharges.remove(studentAdditionalCharges);
		studentAdditionalCharges.setClassStudent(null);
		return this;
	}

	public void setStudentAdditionalCharges(Set<StudentAdditionalCharges> studentAdditionalCharges) {
		if (this.studentAdditionalCharges != null) {
			this.studentAdditionalCharges.forEach(i -> i.setClassStudent(null));
		}
		if (studentAdditionalCharges != null) {
			studentAdditionalCharges.forEach(i -> i.setClassStudent(this));
		}
		this.studentAdditionalCharges = studentAdditionalCharges;
	}

	public Set<StudentChargesSummary> getStudentChargesSummaries() {
		return this.studentChargesSummaries;
	}

	public ClassStudent studentChargesSummaries(Set<StudentChargesSummary> studentChargesSummaries) {
		this.setStudentChargesSummaries(studentChargesSummaries);
		return this;
	}

	public ClassStudent addStudentChargesSummary(StudentChargesSummary studentChargesSummary) {
		this.studentChargesSummaries.add(studentChargesSummary);
		studentChargesSummary.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentChargesSummary(StudentChargesSummary studentChargesSummary) {
		this.studentChargesSummaries.remove(studentChargesSummary);
		studentChargesSummary.setClassStudent(null);
		return this;
	}

	public void setStudentChargesSummaries(Set<StudentChargesSummary> studentChargesSummaries) {
		if (this.studentChargesSummaries != null) {
			this.studentChargesSummaries.forEach(i -> i.setClassStudent(null));
		}
		if (studentChargesSummaries != null) {
			studentChargesSummaries.forEach(i -> i.setClassStudent(this));
		}
		this.studentChargesSummaries = studentChargesSummaries;
	}

	public Set<StudentPayments> getStudentPayments() {
		return this.studentPayments;
	}

	public ClassStudent studentPayments(Set<StudentPayments> studentPayments) {
		this.setStudentPayments(studentPayments);
		return this;
	}

	public ClassStudent addStudentPayments(StudentPayments studentPayments) {
		this.studentPayments.add(studentPayments);
		studentPayments.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentPayments(StudentPayments studentPayments) {
		this.studentPayments.remove(studentPayments);
		studentPayments.setClassStudent(null);
		return this;
	}

	public void setStudentPayments(Set<StudentPayments> studentPayments) {
		if (this.studentPayments != null) {
			this.studentPayments.forEach(i -> i.setClassStudent(null));
		}
		if (studentPayments != null) {
			studentPayments.forEach(i -> i.setClassStudent(this));
		}
		this.studentPayments = studentPayments;
	}

	public Set<StudentAttendence> getStudentAttendences() {
		return this.studentAttendences;
	}

	public ClassStudent studentAttendences(Set<StudentAttendence> studentAttendences) {
		this.setStudentAttendences(studentAttendences);
		return this;
	}

	public ClassStudent addStudentAttendence(StudentAttendence studentAttendence) {
		this.studentAttendences.add(studentAttendence);
		studentAttendence.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentAttendence(StudentAttendence studentAttendence) {
		this.studentAttendences.remove(studentAttendence);
		studentAttendence.setClassStudent(null);
		return this;
	}

	public void setStudentAttendences(Set<StudentAttendence> studentAttendences) {
		if (this.studentAttendences != null) {
			this.studentAttendences.forEach(i -> i.setClassStudent(null));
		}
		if (studentAttendences != null) {
			studentAttendences.forEach(i -> i.setClassStudent(this));
		}
		this.studentAttendences = studentAttendences;
	}

	public Set<StudentHomeWorkTrack> getStudentHomeWorkTracks() {
		return this.studentHomeWorkTracks;
	}

	public ClassStudent studentHomeWorkTracks(Set<StudentHomeWorkTrack> studentHomeWorkTracks) {
		this.setStudentHomeWorkTracks(studentHomeWorkTracks);
		return this;
	}

	public ClassStudent addStudentHomeWorkTrack(StudentHomeWorkTrack studentHomeWorkTrack) {
		this.studentHomeWorkTracks.add(studentHomeWorkTrack);
		studentHomeWorkTrack.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentHomeWorkTrack(StudentHomeWorkTrack studentHomeWorkTrack) {
		this.studentHomeWorkTracks.remove(studentHomeWorkTrack);
		studentHomeWorkTrack.setClassStudent(null);
		return this;
	}

	public void setStudentHomeWorkTracks(Set<StudentHomeWorkTrack> studentHomeWorkTracks) {
		if (this.studentHomeWorkTracks != null) {
			this.studentHomeWorkTracks.forEach(i -> i.setClassStudent(null));
		}
		if (studentHomeWorkTracks != null) {
			studentHomeWorkTracks.forEach(i -> i.setClassStudent(this));
		}
		this.studentHomeWorkTracks = studentHomeWorkTracks;
	}

	public Set<StudentClassWorkTrack> getStudentClassWorkTracks() {
		return this.studentClassWorkTracks;
	}

	public ClassStudent studentClassWorkTracks(Set<StudentClassWorkTrack> studentClassWorkTracks) {
		this.setStudentClassWorkTracks(studentClassWorkTracks);
		return this;
	}

	public ClassStudent addStudentClassWorkTrack(StudentClassWorkTrack studentClassWorkTrack) {
		this.studentClassWorkTracks.add(studentClassWorkTrack);
		studentClassWorkTrack.setClassStudent(this);
		return this;
	}

	public ClassStudent removeStudentClassWorkTrack(StudentClassWorkTrack studentClassWorkTrack) {
		this.studentClassWorkTracks.remove(studentClassWorkTrack);
		studentClassWorkTrack.setClassStudent(null);
		return this;
	}

	public void setStudentClassWorkTracks(Set<StudentClassWorkTrack> studentClassWorkTracks) {
		if (this.studentClassWorkTracks != null) {
			this.studentClassWorkTracks.forEach(i -> i.setClassStudent(null));
		}
		if (studentClassWorkTracks != null) {
			studentClassWorkTracks.forEach(i -> i.setClassStudent(this));
		}
		this.studentClassWorkTracks = studentClassWorkTracks;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public ClassStudent schoolClass(SchoolClass schoolClass) {
		this.setSchoolClass(schoolClass);
		return this;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public Long getSchoolClassId() {
		if (schoolClass != null)
			return this.schoolClass.getId();
		else {
			return -1L;
		}
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassStudent)) {
			return false;
		}
		return id != null && id.equals(((ClassStudent) o).id);
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "ClassStudent{" + "id=" + getId() + ", studentPhoto='" + getStudentPhoto() + "'"
				+ ", studentPhotoContentType='" + getStudentPhotoContentType() + "'" + ", studentPhotoLink='"
				+ getStudentPhotoLink() + "'" + ", studentId='" + getStudentId() + "'" + ", firstName='"
				+ getFirstName() + "'" + ", gender='" + getGender() + "'" + ", lastName='" + getLastName() + "'"
				+ ", rollNumber='" + getRollNumber() + "'" + ", phoneNumber='" + getPhoneNumber() + "'"
				+ ", bloodGroup='" + getBloodGroup() + "'" + ", dateOfBirth='" + getDateOfBirth() + "'"
				+ ", startDate='" + getStartDate() + "'" + ", addressLine1='" + getAddressLine1() + "'"
				+ ", addressLine2='" + getAddressLine2() + "'" + ", nickName='" + getNickName() + "'" + ", fatherName='"
				+ getFatherName() + "'" + ", motherName='" + getMotherName() + "'" + ", email='" + getEmail() + "'"
				+ ", admissionDate='" + getAdmissionDate() + "'" + ", regNumber='" + getRegNumber() + "'"
				+ ", endDate='" + getEndDate() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
