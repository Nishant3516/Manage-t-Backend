package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassHomeWork} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassHomeWorkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-home-works?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassHomeWorkCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StudentAssignmentType
     */
    public static class StudentAssignmentTypeFilter extends Filter<StudentAssignmentType> {

        public StudentAssignmentTypeFilter() {}

        public StudentAssignmentTypeFilter(StudentAssignmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public StudentAssignmentTypeFilter copy() {
            return new StudentAssignmentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter schoolDate;

    private StudentAssignmentTypeFilter studentAssignmentType;

    private StringFilter homeWorkText;

    private StringFilter homeWorkFileLink;

    private BooleanFilter assign;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter studentHomeWorkTrackId;

    private LongFilter chapterSectionId;

    public ClassHomeWorkCriteria() {}

    public ClassHomeWorkCriteria(ClassHomeWorkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.schoolDate = other.schoolDate == null ? null : other.schoolDate.copy();
        this.studentAssignmentType = other.studentAssignmentType == null ? null : other.studentAssignmentType.copy();
        this.homeWorkText = other.homeWorkText == null ? null : other.homeWorkText.copy();
        this.homeWorkFileLink = other.homeWorkFileLink == null ? null : other.homeWorkFileLink.copy();
        this.assign = other.assign == null ? null : other.assign.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.studentHomeWorkTrackId = other.studentHomeWorkTrackId == null ? null : other.studentHomeWorkTrackId.copy();
        this.chapterSectionId = other.chapterSectionId == null ? null : other.chapterSectionId.copy();
    }

    @Override
    public ClassHomeWorkCriteria copy() {
        return new ClassHomeWorkCriteria(this);
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

    public StudentAssignmentTypeFilter getStudentAssignmentType() {
        return studentAssignmentType;
    }

    public StudentAssignmentTypeFilter studentAssignmentType() {
        if (studentAssignmentType == null) {
            studentAssignmentType = new StudentAssignmentTypeFilter();
        }
        return studentAssignmentType;
    }

    public void setStudentAssignmentType(StudentAssignmentTypeFilter studentAssignmentType) {
        this.studentAssignmentType = studentAssignmentType;
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

    public StringFilter getHomeWorkFileLink() {
        return homeWorkFileLink;
    }

    public StringFilter homeWorkFileLink() {
        if (homeWorkFileLink == null) {
            homeWorkFileLink = new StringFilter();
        }
        return homeWorkFileLink;
    }

    public void setHomeWorkFileLink(StringFilter homeWorkFileLink) {
        this.homeWorkFileLink = homeWorkFileLink;
    }

    public BooleanFilter getAssign() {
        return assign;
    }

    public BooleanFilter assign() {
        if (assign == null) {
            assign = new BooleanFilter();
        }
        return assign;
    }

    public void setAssign(BooleanFilter assign) {
        this.assign = assign;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassHomeWorkCriteria that = (ClassHomeWorkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(schoolDate, that.schoolDate) &&
            Objects.equals(studentAssignmentType, that.studentAssignmentType) &&
            Objects.equals(homeWorkText, that.homeWorkText) &&
            Objects.equals(homeWorkFileLink, that.homeWorkFileLink) &&
            Objects.equals(assign, that.assign) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(studentHomeWorkTrackId, that.studentHomeWorkTrackId) &&
            Objects.equals(chapterSectionId, that.chapterSectionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            schoolDate,
            studentAssignmentType,
            homeWorkText,
            homeWorkFileLink,
            assign,
            createDate,
            lastModified,
            cancelDate,
            studentHomeWorkTrackId,
            chapterSectionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassHomeWorkCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (schoolDate != null ? "schoolDate=" + schoolDate + ", " : "") +
            (studentAssignmentType != null ? "studentAssignmentType=" + studentAssignmentType + ", " : "") +
            (homeWorkText != null ? "homeWorkText=" + homeWorkText + ", " : "") +
            (homeWorkFileLink != null ? "homeWorkFileLink=" + homeWorkFileLink + ", " : "") +
            (assign != null ? "assign=" + assign + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (studentHomeWorkTrackId != null ? "studentHomeWorkTrackId=" + studentHomeWorkTrackId + ", " : "") +
            (chapterSectionId != null ? "chapterSectionId=" + chapterSectionId + ", " : "") +
            "}";
    }
}
