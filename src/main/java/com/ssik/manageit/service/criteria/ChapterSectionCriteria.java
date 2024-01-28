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
 * Criteria class for the {@link com.ssik.manageit.domain.ChapterSection} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ChapterSectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chapter-sections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChapterSectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectionName;

    private StringFilter sectionNumber;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classHomeWorkId;

    private LongFilter classClassWorkId;

    private LongFilter classLessionPlanId;

    private LongFilter subjectChapterId;

    public ChapterSectionCriteria() {}

    public ChapterSectionCriteria(ChapterSectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sectionName = other.sectionName == null ? null : other.sectionName.copy();
        this.sectionNumber = other.sectionNumber == null ? null : other.sectionNumber.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classHomeWorkId = other.classHomeWorkId == null ? null : other.classHomeWorkId.copy();
        this.classClassWorkId = other.classClassWorkId == null ? null : other.classClassWorkId.copy();
        this.classLessionPlanId = other.classLessionPlanId == null ? null : other.classLessionPlanId.copy();
        this.subjectChapterId = other.subjectChapterId == null ? null : other.subjectChapterId.copy();
    }

    @Override
    public ChapterSectionCriteria copy() {
        return new ChapterSectionCriteria(this);
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

    public StringFilter getSectionName() {
        return sectionName;
    }

    public StringFilter sectionName() {
        if (sectionName == null) {
            sectionName = new StringFilter();
        }
        return sectionName;
    }

    public void setSectionName(StringFilter sectionName) {
        this.sectionName = sectionName;
    }

    public StringFilter getSectionNumber() {
        return sectionNumber;
    }

    public StringFilter sectionNumber() {
        if (sectionNumber == null) {
            sectionNumber = new StringFilter();
        }
        return sectionNumber;
    }

    public void setSectionNumber(StringFilter sectionNumber) {
        this.sectionNumber = sectionNumber;
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

    public LongFilter getClassHomeWorkId() {
        return classHomeWorkId;
    }

    public LongFilter classHomeWorkId() {
        if (classHomeWorkId == null) {
            classHomeWorkId = new LongFilter();
        }
        return classHomeWorkId;
    }

    public void setClassHomeWorkId(LongFilter classHomeWorkId) {
        this.classHomeWorkId = classHomeWorkId;
    }

    public LongFilter getClassClassWorkId() {
        return classClassWorkId;
    }

    public LongFilter classClassWorkId() {
        if (classClassWorkId == null) {
            classClassWorkId = new LongFilter();
        }
        return classClassWorkId;
    }

    public void setClassClassWorkId(LongFilter classClassWorkId) {
        this.classClassWorkId = classClassWorkId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChapterSectionCriteria that = (ChapterSectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sectionName, that.sectionName) &&
            Objects.equals(sectionNumber, that.sectionNumber) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classHomeWorkId, that.classHomeWorkId) &&
            Objects.equals(classClassWorkId, that.classClassWorkId) &&
            Objects.equals(classLessionPlanId, that.classLessionPlanId) &&
            Objects.equals(subjectChapterId, that.subjectChapterId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sectionName,
            sectionNumber,
            createDate,
            lastModified,
            cancelDate,
            classHomeWorkId,
            classClassWorkId,
            classLessionPlanId,
            subjectChapterId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChapterSectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sectionName != null ? "sectionName=" + sectionName + ", " : "") +
            (sectionNumber != null ? "sectionNumber=" + sectionNumber + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classHomeWorkId != null ? "classHomeWorkId=" + classHomeWorkId + ", " : "") +
            (classClassWorkId != null ? "classClassWorkId=" + classClassWorkId + ", " : "") +
            (classLessionPlanId != null ? "classLessionPlanId=" + classLessionPlanId + ", " : "") +
            (subjectChapterId != null ? "subjectChapterId=" + subjectChapterId + ", " : "") +
            "}";
    }
}
