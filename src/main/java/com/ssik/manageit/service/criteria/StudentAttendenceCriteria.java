package com.ssik.manageit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ssik.manageit.domain.StudentAttendence} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentAttendenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-attendences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentAttendenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter schoolDate;

    private BooleanFilter attendence;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classStudentId;

    private LongFilter schoolClassId;

    public StudentAttendenceCriteria() {}

    public StudentAttendenceCriteria(StudentAttendenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.schoolDate = other.schoolDate == null ? null : other.schoolDate.copy();
        this.attendence = other.attendence == null ? null : other.attendence.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public StudentAttendenceCriteria copy() {
        return new StudentAttendenceCriteria(this);
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

    public LocalDateFilter getSchoolDate() {
        return schoolDate;
    }

    public LocalDateFilter schoolDate() {
        if (schoolDate == null) {
            schoolDate = new LocalDateFilter();
        }
        return schoolDate;
    }

    public void setSchoolDate(LocalDateFilter schoolDate) {
        this.schoolDate = schoolDate;
    }

    public BooleanFilter getAttendence() {
        return attendence;
    }

    public BooleanFilter attendence() {
        if (attendence == null) {
            attendence = new BooleanFilter();
        }
        return attendence;
    }

    public void setAttendence(BooleanFilter attendence) {
        this.attendence = attendence;
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

    public LongFilter getClassStudentId() {
        return classStudentId;
    }

    public LongFilter classStudentId() {
        if (classStudentId == null) {
            classStudentId = new LongFilter();
        }
        return classStudentId;
    }

    public void setClassStudentId(LongFilter classStudentId) {
        this.classStudentId = classStudentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentAttendenceCriteria that = (StudentAttendenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(schoolDate, that.schoolDate) &&
            Objects.equals(attendence, that.attendence) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classStudentId, that.classStudentId)&&
            Objects.equals(schoolClassId, that.schoolClassId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schoolDate, attendence, createDate, lastModified, cancelDate, classStudentId, schoolClassId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentAttendenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (schoolDate != null ? "schoolDate=" + schoolDate + ", " : "") +
            (attendence != null ? "attendence=" + attendence + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
