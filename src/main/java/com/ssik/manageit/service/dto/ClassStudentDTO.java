package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.BloodGroup;
import com.ssik.manageit.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassStudent} entity.
 */
public class ClassStudentDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] studentPhoto;

    private String studentPhotoContentType;
    private String studentPhotoLink;

    private String studentId;

    @NotNull
    private String firstName;

    private Gender gender;

    private String lastName;

    private String rollNumber;

    private String phoneNumber;

    private BloodGroup bloodGroup;

    private Instant dateOfBirth;

    private LocalDate startDate;

    private String addressLine1;

    private String addressLine2;

    private String nickName;

    private String fatherName;

    private String motherName;

    private String email;

    private LocalDate admissionDate;

    private String regNumber;

    private LocalDate endDate;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private SchoolClassDTO schoolClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(byte[] studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getStudentPhotoContentType() {
        return studentPhotoContentType;
    }

    public void setStudentPhotoContentType(String studentPhotoContentType) {
        this.studentPhotoContentType = studentPhotoContentType;
    }

    public String getStudentPhotoLink() {
        return studentPhotoLink;
    }

    public void setStudentPhotoLink(String studentPhotoLink) {
        this.studentPhotoLink = studentPhotoLink;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public SchoolClassDTO getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClassDTO schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassStudentDTO)) {
            return false;
        }

        ClassStudentDTO classStudentDTO = (ClassStudentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classStudentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassStudentDTO{" +
            "id=" + getId() +
            ", studentPhoto='" + getStudentPhoto() + "'" +
            ", studentPhotoLink='" + getStudentPhotoLink() + "'" +
            ", studentId='" + getStudentId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", gender='" + getGender() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", rollNumber='" + getRollNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", email='" + getEmail() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", regNumber='" + getRegNumber() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClass=" + getSchoolClass() +
            "}";
    }
}
