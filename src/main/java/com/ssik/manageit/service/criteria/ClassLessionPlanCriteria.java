package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.TaskStatus;
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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassLessionPlan} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassLessionPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-lession-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassLessionPlanCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TaskStatus
     */
    public static class TaskStatusFilter extends Filter<TaskStatus> {

        public TaskStatusFilter() {}

        public TaskStatusFilter(TaskStatusFilter filter) {
            super(filter);
        }

        @Override
        public TaskStatusFilter copy() {
            return new TaskStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter schoolDate;

    private StringFilter classWorkText;

    private StringFilter homeWorkText;

    private TaskStatusFilter workStatus;

    private StringFilter lessionPlanFileLink;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classLessionPlanTrackId;

    private LongFilter chapterSectionId;

    private LongFilter schoolClassId;

    private LongFilter classSubjectId;

    private LongFilter subjectChapterId;

    public ClassLessionPlanCriteria() {}

    public ClassLessionPlanCriteria(ClassLessionPlanCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.schoolDate = other.schoolDate == null ? null : other.schoolDate.copy();
        this.classWorkText = other.classWorkText == null ? null : other.classWorkText.copy();
        this.homeWorkText = other.homeWorkText == null ? null : other.homeWorkText.copy();
        this.workStatus = other.workStatus == null ? null : other.workStatus.copy();
        this.lessionPlanFileLink = other.lessionPlanFileLink == null ? null : other.lessionPlanFileLink.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classLessionPlanTrackId = other.classLessionPlanTrackId == null ? null : other.classLessionPlanTrackId.copy();
        this.chapterSectionId = other.chapterSectionId == null ? null : other.chapterSectionId.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
        this.subjectChapterId = other.subjectChapterId == null ? null : other.subjectChapterId.copy();
    }

    @Override
    public ClassLessionPlanCriteria copy() {
        return new ClassLessionPlanCriteria(this);
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

    public StringFilter getClassWorkText() {
        return classWorkText;
    }

    public StringFilter classWorkText() {
        if (classWorkText == null) {
            classWorkText = new StringFilter();
        }
        return classWorkText;
    }

    public void setClassWorkText(StringFilter classWorkText) {
        this.classWorkText = classWorkText;
    }

    public StringFilter getHomeWorkText() {
        return homeWorkText;
    }

    public StringFilter homeWorkText() {
        if (homeWorkText == null) {
            homeWorkText = new StringFilter();
        }
        return homeWorkText;
    }

    public void setHomeWorkText(StringFilter homeWorkText) {
        this.homeWorkText = homeWorkText;
    }

    public TaskStatusFilter getWorkStatus() {
        return workStatus;
    }

    public TaskStatusFilter workStatus() {
        if (workStatus == null) {
            workStatus = new TaskStatusFilter();
        }
        return workStatus;
    }

    public void setWorkStatus(TaskStatusFilter workStatus) {
        this.workStatus = workStatus;
    }

    public StringFilter getLessionPlanFileLink() {
        return lessionPlanFileLink;
    }

    public StringFilter lessionPlanFileLink() {
        if (lessionPlanFileLink == null) {
            lessionPlanFileLink = new StringFilter();
        }
        return lessionPlanFileLink;
    }

    public void setLessionPlanFileLink(StringFilter lessionPlanFileLink) {
        this.lessionPlanFileLink = lessionPlanFileLink;
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

    public LongFilter getClassLessionPlanTrackId() {
        return classLessionPlanTrackId;
    }

    public LongFilter classLessionPlanTrackId() {
        if (classLessionPlanTrackId == null) {
            classLessionPlanTrackId = new LongFilter();
        }
        return classLessionPlanTrackId;
    }

    public void setClassLessionPlanTrackId(LongFilter classLessionPlanTrackId) {
        this.classLessionPlanTrackId = classLessionPlanTrackId;
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
        final ClassLessionPlanCriteria that = (ClassLessionPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(schoolDate, that.schoolDate) &&
            Objects.equals(classWorkText, that.classWorkText) &&
            Objects.equals(homeWorkText, that.homeWorkText) &&
            Objects.equals(workStatus, that.workStatus) &&
            Objects.equals(lessionPlanFileLink, that.lessionPlanFileLink) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classLessionPlanTrackId, that.classLessionPlanTrackId) &&
            Objects.equals(chapterSectionId, that.chapterSectionId) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(classSubjectId, that.classSubjectId) &&
            Objects.equals(subjectChapterId, that.subjectChapterId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            schoolDate,
            classWorkText,
            homeWorkText,
            workStatus,
            lessionPlanFileLink,
            createDate,
            lastModified,
            cancelDate,
            classLessionPlanTrackId,
            chapterSectionId,
            schoolClassId,
            classSubjectId,
            subjectChapterId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassLessionPlanCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (schoolDate != null ? "schoolDate=" + schoolDate + ", " : "") +
            (classWorkText != null ? "classWorkText=" + classWorkText + ", " : "") +
            (homeWorkText != null ? "homeWorkText=" + homeWorkText + ", " : "") +
            (workStatus != null ? "workStatus=" + workStatus + ", " : "") +
            (lessionPlanFileLink != null ? "lessionPlanFileLink=" + lessionPlanFileLink + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classLessionPlanTrackId != null ? "classLessionPlanTrackId=" + classLessionPlanTrackId + ", " : "") +
            (chapterSectionId != null ? "chapterSectionId=" + chapterSectionId + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            (subjectChapterId != null ? "subjectChapterId=" + subjectChapterId + ", " : "") +
            "}";
    }
}
