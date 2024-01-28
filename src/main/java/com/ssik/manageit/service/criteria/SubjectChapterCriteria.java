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
 * Criteria class for the {@link com.ssik.manageit.domain.SubjectChapter} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SubjectChapterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subject-chapters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubjectChapterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter chapterName;

    private IntegerFilter chapterNumber;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter chapterSectionId;

    private LongFilter classLessionPlanId;

    private LongFilter classSubjectId;

    public SubjectChapterCriteria() {}

    public SubjectChapterCriteria(SubjectChapterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.chapterName = other.chapterName == null ? null : other.chapterName.copy();
        this.chapterNumber = other.chapterNumber == null ? null : other.chapterNumber.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.chapterSectionId = other.chapterSectionId == null ? null : other.chapterSectionId.copy();
        this.classLessionPlanId = other.classLessionPlanId == null ? null : other.classLessionPlanId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
    }

    @Override
    public SubjectChapterCriteria copy() {
        return new SubjectChapterCriteria(this);
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

    public StringFilter getChapterName() {
        return chapterName;
    }

    public StringFilter chapterName() {
        if (chapterName == null) {
            chapterName = new StringFilter();
        }
        return chapterName;
    }

    public void setChapterName(StringFilter chapterName) {
        this.chapterName = chapterName;
    }

    public IntegerFilter getChapterNumber() {
        return chapterNumber;
    }

    public IntegerFilter chapterNumber() {
        if (chapterNumber == null) {
            chapterNumber = new IntegerFilter();
        }
        return chapterNumber;
    }

    public void setChapterNumber(IntegerFilter chapterNumber) {
        this.chapterNumber = chapterNumber;
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

    public LongFilter getChapterSectionId() {
        return chapterSectionId;
    }

    public LongFilter chapterSectionId() {
        if (chapterSectionId == null) {
            chapterSectionId = new LongFilter();
        }
        return chapterSectionId;
    }

    public void setChapterSectionId(LongFilter chapterSectionId) {
        this.chapterSectionId = chapterSectionId;
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

    public LongFilter getClassSubjectId() {
        return classSubjectId;
    }

    public LongFilter classSubjectId() {
        if (classSubjectId == null) {
            classSubjectId = new LongFilter();
        }
        return classSubjectId;
    }

    public void setClassSubjectId(LongFilter classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubjectChapterCriteria that = (SubjectChapterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(chapterName, that.chapterName) &&
            Objects.equals(chapterNumber, that.chapterNumber) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(chapterSectionId, that.chapterSectionId) &&
            Objects.equals(classLessionPlanId, that.classLessionPlanId) &&
            Objects.equals(classSubjectId, that.classSubjectId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            chapterName,
            chapterNumber,
            createDate,
            lastModified,
            cancelDate,
            chapterSectionId,
            classLessionPlanId,
            classSubjectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectChapterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (chapterName != null ? "chapterName=" + chapterName + ", " : "") +
            (chapterNumber != null ? "chapterNumber=" + chapterNumber + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (chapterSectionId != null ? "chapterSectionId=" + chapterSectionId + ", " : "") +
            (classLessionPlanId != null ? "classLessionPlanId=" + classLessionPlanId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            "}";
    }
}
