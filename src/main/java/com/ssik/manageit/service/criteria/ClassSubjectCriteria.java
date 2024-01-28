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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassSubject} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassSubjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-subjects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassSubjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subjectName;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter subjectChapterId;

    private LongFilter classLessionPlanId;

    private LongFilter schoolClassId;

    private LongFilter schoolUserId;

    public ClassSubjectCriteria() {}

    public ClassSubjectCriteria(ClassSubjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subjectName = other.subjectName == null ? null : other.subjectName.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.subjectChapterId = other.subjectChapterId == null ? null : other.subjectChapterId.copy();
        this.classLessionPlanId = other.classLessionPlanId == null ? null : other.classLessionPlanId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.schoolUserId = other.schoolUserId == null ? null : other.schoolUserId.copy();
    }

    @Override
    public ClassSubjectCriteria copy() {
        return new ClassSubjectCriteria(this);
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

    public StringFilter getSubjectName() {
        return subjectName;
    }

    public StringFilter subjectName() {
        if (subjectName == null) {
            subjectName = new StringFilter();
        }
        return subjectName;
    }

    public void setSubjectName(StringFilter subjectName) {
        this.subjectName = subjectName;
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

    public LongFilter getSubjectChapterId() {
        return subjectChapterId;
    }

    public LongFilter subjectChapterId() {
        if (subjectChapterId == null) {
            subjectChapterId = new LongFilter();
        }
        return subjectChapterId;
    }

    public void setSubjectChapterId(LongFilter subjectChapterId) {
        this.subjectChapterId = subjectChapterId;
    }

    public LongFilter getClassLessionPlanId() {
        return classLessionPlanId;
    }

    public LongFilter classLessionPlanId() {
        if (classLessionPlanId == null) {
            classLessionPlanId = new LongFilter();
        }
        return classLessionPlanId;
    }

    public void setClassLessionPlanId(LongFilter classLessionPlanId) {
        this.classLessionPlanId = classLessionPlanId;
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

    public LongFilter getSchoolUserId() {
        return schoolUserId;
    }

    public LongFilter schoolUserId() {
        if (schoolUserId == null) {
            schoolUserId = new LongFilter();
        }
        return schoolUserId;
    }

    public void setSchoolUserId(LongFilter schoolUserId) {
        this.schoolUserId = schoolUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassSubjectCriteria that = (ClassSubjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subjectName, that.subjectName) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(subjectChapterId, that.subjectChapterId) &&
            Objects.equals(classLessionPlanId, that.classLessionPlanId) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(schoolUserId, that.schoolUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            subjectName,
            createDate,
            lastModified,
            cancelDate,
            subjectChapterId,
            classLessionPlanId,
            schoolClassId,
            schoolUserId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassSubjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (subjectName != null ? "subjectName=" + subjectName + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (subjectChapterId != null ? "subjectChapterId=" + subjectChapterId + ", " : "") +
            (classLessionPlanId != null ? "classLessionPlanId=" + classLessionPlanId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (schoolUserId != null ? "schoolUserId=" + schoolUserId + ", " : "") +
            "}";
    }
}
