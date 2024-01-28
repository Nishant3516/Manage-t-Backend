package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.BloodGroup;
import com.ssik.manageit.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ssik.manageit.domain.ClassStudent} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassStudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassStudentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering BloodGroup
     */
    public static class BloodGroupFilter extends Filter<BloodGroup> {

        public BloodGroupFilter() {}

        public BloodGroupFilter(BloodGroupFilter filter) {
            super(filter);
        }

        @Override
        public BloodGroupFilter copy() {
            return new BloodGroupFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter studentPhotoLink;

    private StringFilter studentId;

    private StringFilter firstName;

    private GenderFilter gender;

    private StringFilter lastName;

    private StringFilter rollNumber;

    private StringFilter phoneNumber;

    private BloodGroupFilter bloodGroup;

    private InstantFilter dateOfBirth;

    private LocalDateFilter startDate;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter nickName;

    private StringFilter fatherName;

    private StringFilter motherName;

    private StringFilter email;

    private LocalDateFilter admissionDate;

    private StringFilter regNumber;

    private LocalDateFilter endDate;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter studentDiscountId;

    private LongFilter studentAdditionalChargesId;

    private LongFilter studentChargesSummaryId;

    private LongFilter studentPaymentsId;

    private LongFilter studentAttendenceId;

    private LongFilter studentHomeWorkTrackId;

    private LongFilter studentClassWorkTrackId;

    private LongFilter schoolClassId;

    public ClassStudentCriteria() {}

    public ClassStudentCriteria(ClassStudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.studentPhotoLink = other.studentPhotoLink == null ? null : other.studentPhotoLink.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.rollNumber = other.rollNumber == null ? null : other.rollNumber.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.bloodGroup = other.bloodGroup == null ? null : other.bloodGroup.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.nickName = other.nickName == null ? null : other.nickName.copy();
        this.fatherName = other.fatherName == null ? null : other.fatherName.copy();
        this.motherName = other.motherName == null ? null : other.motherName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.admissionDate = other.admissionDate == null ? null : other.admissionDate.copy();
        this.regNumber = other.regNumber == null ? null : other.regNumber.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.studentDiscountId = other.studentDiscountId == null ? null : other.studentDiscountId.copy();
        this.studentAdditionalChargesId = other.studentAdditionalChargesId == null ? null : other.studentAdditionalChargesId.copy();
        this.studentChargesSummaryId = other.studentChargesSummaryId == null ? null : other.studentChargesSummaryId.copy();
        this.studentPaymentsId = other.studentPaymentsId == null ? null : other.studentPaymentsId.copy();
        this.studentAttendenceId = other.studentAttendenceId == null ? null : other.studentAttendenceId.copy();
        this.studentHomeWorkTrackId = other.studentHomeWorkTrackId == null ? null : other.studentHomeWorkTrackId.copy();
        this.studentClassWorkTrackId = other.studentClassWorkTrackId == null ? null : other.studentClassWorkTrackId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public ClassStudentCriteria copy() {
        return new ClassStudentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStudentPhotoLink() {
        return studentPhotoLink;
    }

    public StringFilter studentPhotoLink() {
        if (studentPhotoLink == null) {
            studentPhotoLink = new StringFilter();
        }
        return studentPhotoLink;
    }

    public void setStudentPhotoLink(StringFilter studentPhotoLink) {
        this.studentPhotoLink = studentPhotoLink;
    }

    public StringFilter getStudentId() {
        return studentId;
    }

    public StringFilter studentId() {
        if (studentId == null) {
            studentId = new StringFilter();
        }
        return studentId;
    }

    public void setStudentId(StringFilter studentId) {
        this.studentId = studentId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getRollNumber() {
        return rollNumber;
    }

    public StringFilter rollNumber() {
        if (rollNumber == null) {
            rollNumber = new StringFilter();
        }
        return rollNumber;
    }

    public void setRollNumber(StringFilter rollNumber) {
        this.rollNumber = rollNumber;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BloodGroupFilter getBloodGroup() {
        return bloodGroup;
    }

    public BloodGroupFilter bloodGroup() {
        if (bloodGroup == null) {
            bloodGroup = new BloodGroupFilter();
        }
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroupFilter bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public InstantFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public InstantFilter dateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new InstantFilter();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(InstantFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public StringFilter addressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new StringFilter();
        }
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public StringFilter addressLine2() {
        if (addressLine2 == null) {
            addressLine2 = new StringFilter();
        }
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getNickName() {
        return nickName;
    }

    public StringFilter nickName() {
        if (nickName == null) {
            nickName = new StringFilter();
        }
        return nickName;
    }

    public void setNickName(StringFilter nickName) {
        this.nickName = nickName;
    }

    public StringFilter getFatherName() {
        return fatherName;
    }

    public StringFilter fatherName() {
        if (fatherName == null) {
            fatherName = new StringFilter();
        }
        return fatherName;
    }

    public void setFatherName(StringFilter fatherName) {
        this.fatherName = fatherName;
    }

    public StringFilter getMotherName() {
        return motherName;
    }

    public StringFilter motherName() {
        if (motherName == null) {
            motherName = new StringFilter();
        }
        return motherName;
    }

    public void setMotherName(StringFilter motherName) {
        this.motherName = motherName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getAdmissionDate() {
        return admissionDate;
    }

    public LocalDateFilter admissionDate() {
        if (admissionDate == null) {
            admissionDate = new LocalDateFilter();
        }
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateFilter admissionDate) {
        this.admissionDate = admissionDate;
    }

    public StringFilter getRegNumber() {
        return regNumber;
    }

    public StringFilter regNumber() {
        if (regNumber == null) {
            regNumber = new StringFilter();
        }
        return regNumber;
    }

    public void setRegNumber(StringFilter regNumber) {
        this.regNumber = regNumber;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LocalDateFilter getCreateDate() {
        return createDate;
    }

    public LocalDateFilter createDate() {
        if (createDate == null) {
            createDate = new LocalDateFilter();
        }
        return createDate;
    }

    public void setCreateDate(LocalDateFilter createDate) {
        this.createDate = createDate;
    }

    public LocalDateFilter getLastModified() {
        return lastModified;
    }

    public LocalDateFilter lastModified() {
        if (lastModified == null) {
            lastModified = new LocalDateFilter();
        }
        return lastModified;
    }

    public void setLastModified(LocalDateFilter lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDateFilter getCancelDate() {
        return cancelDate;
    }

    public LocalDateFilter cancelDate() {
        if (cancelDate == null) {
            cancelDate = new LocalDateFilter();
        }
        return cancelDate;
    }

    public void setCancelDate(LocalDateFilter cancelDate) {
        this.cancelDate = cancelDate;
    }

    public LongFilter getStudentDiscountId() {
        return studentDiscountId;
    }

    public LongFilter studentDiscountId() {
        if (studentDiscountId == null) {
            studentDiscountId = new LongFilter();
        }
        return studentDiscountId;
    }

    public void setStudentDiscountId(LongFilter studentDiscountId) {
        this.studentDiscountId = studentDiscountId;
    }

    public LongFilter getStudentAdditionalChargesId() {
        return studentAdditionalChargesId;
    }

    public LongFilter studentAdditionalChargesId() {
        if (studentAdditionalChargesId == null) {
            studentAdditionalChargesId = new LongFilter();
        }
        return studentAdditionalChargesId;
    }

    public void setStudentAdditionalChargesId(LongFilter studentAdditionalChargesId) {
        this.studentAdditionalChargesId = studentAdditionalChargesId;
    }

    public LongFilter getStudentChargesSummaryId() {
        return studentChargesSummaryId;
    }

    public LongFilter studentChargesSummaryId() {
        if (studentChargesSummaryId == null) {
            studentChargesSummaryId = new LongFilter();
        }
        return studentChargesSummaryId;
    }

    public void setStudentChargesSummaryId(LongFilter studentChargesSummaryId) {
        this.studentChargesSummaryId = studentChargesSummaryId;
    }

    public LongFilter getStudentPaymentsId() {
        return studentPaymentsId;
    }

    public LongFilter studentPaymentsId() {
        if (studentPaymentsId == null) {
            studentPaymentsId = new LongFilter();
        }
        return studentPaymentsId;
    }

    public void setStudentPaymentsId(LongFilter studentPaymentsId) {
        this.studentPaymentsId = studentPaymentsId;
    }

    public LongFilter getStudentAttendenceId() {
        return studentAttendenceId;
    }

    public LongFilter studentAttendenceId() {
        if (studentAttendenceId == null) {
            studentAttendenceId = new LongFilter();
        }
        return studentAttendenceId;
    }

    public void setStudentAttendenceId(LongFilter studentAttendenceId) {
        this.studentAttendenceId = studentAttendenceId;
    }

    public LongFilter getStudentHomeWorkTrackId() {
        return studentHomeWorkTrackId;
    }

    public LongFilter studentHomeWorkTrackId() {
        if (studentHomeWorkTrackId == null) {
            studentHomeWorkTrackId = new LongFilter();
        }
        return studentHomeWorkTrackId;
    }

    public void setStudentHomeWorkTrackId(LongFilter studentHomeWorkTrackId) {
        this.studentHomeWorkTrackId = studentHomeWorkTrackId;
    }

    public LongFilter getStudentClassWorkTrackId() {
        return studentClassWorkTrackId;
    }

    public LongFilter studentClassWorkTrackId() {
        if (studentClassWorkTrackId == null) {
            studentClassWorkTrackId = new LongFilter();
        }
        return studentClassWorkTrackId;
    }

    public void setStudentClassWorkTrackId(LongFilter studentClassWorkTrackId) {
        this.studentClassWorkTrackId = studentClassWorkTrackId;
    }

    public LongFilter getSchoolClassId() {
        return schoolClassId;
    }

    public LongFilter schoolClassId() {
        if (schoolClassId == null) {
            schoolClassId = new LongFilter();
        }
        return schoolClassId;
    }

    public void setSchoolClassId(LongFilter schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassStudentCriteria that = (ClassStudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(studentPhotoLink, that.studentPhotoLink) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(rollNumber, that.rollNumber) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(bloodGroup, that.bloodGroup) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(nickName, that.nickName) &&
            Objects.equals(fatherName, that.fatherName) &&
            Objects.equals(motherName, that.motherName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(admissionDate, that.admissionDate) &&
            Objects.equals(regNumber, that.regNumber) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(studentDiscountId, that.studentDiscountId) &&
            Objects.equals(studentAdditionalChargesId, that.studentAdditionalChargesId) &&
            Objects.equals(studentChargesSummaryId, that.studentChargesSummaryId) &&
            Objects.equals(studentPaymentsId, that.studentPaymentsId) &&
            Objects.equals(studentAttendenceId, that.studentAttendenceId) &&
            Objects.equals(studentHomeWorkTrackId, that.studentHomeWorkTrackId) &&
            Objects.equals(studentClassWorkTrackId, that.studentClassWorkTrackId) &&
            Objects.equals(schoolClassId, that.schoolClassId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            studentPhotoLink,
            studentId,
            firstName,
            gender,
            lastName,
            rollNumber,
            phoneNumber,
            bloodGroup,
            dateOfBirth,
            startDate,
            addressLine1,
            addressLine2,
            nickName,
            fatherName,
            motherName,
            email,
            admissionDate,
            regNumber,
            endDate,
            createDate,
            lastModified,
            cancelDate,
            studentDiscountId,
            studentAdditionalChargesId,
            studentChargesSummaryId,
            studentPaymentsId,
            studentAttendenceId,
            studentHomeWorkTrackId,
            studentClassWorkTrackId,
            schoolClassId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassStudentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (studentPhotoLink != null ? "studentPhotoLink=" + studentPhotoLink + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (rollNumber != null ? "rollNumber=" + rollNumber + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (bloodGroup != null ? "bloodGroup=" + bloodGroup + ", " : "") +
            (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
            (nickName != null ? "nickName=" + nickName + ", " : "") +
            (fatherName != null ? "fatherName=" + fatherName + ", " : "") +
            (motherName != null ? "motherName=" + motherName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (admissionDate != null ? "admissionDate=" + admissionDate + ", " : "") +
            (regNumber != null ? "regNumber=" + regNumber + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (studentDiscountId != null ? "studentDiscountId=" + studentDiscountId + ", " : "") +
            (studentAdditionalChargesId != null ? "studentAdditionalChargesId=" + studentAdditionalChargesId + ", " : "") +
            (studentChargesSummaryId != null ? "studentChargesSummaryId=" + studentChargesSummaryId + ", " : "") +
            (studentPaymentsId != null ? "studentPaymentsId=" + studentPaymentsId + ", " : "") +
            (studentAttendenceId != null ? "studentAttendenceId=" + studentAttendenceId + ", " : "") +
            (studentHomeWorkTrackId != null ? "studentHomeWorkTrackId=" + studentHomeWorkTrackId + ", " : "") +
            (studentClassWorkTrackId != null ? "studentClassWorkTrackId=" + studentClassWorkTrackId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
