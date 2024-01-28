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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassClassWork} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassClassWorkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-class-works?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassClassWorkCriteria implements Serializable, Criteria {

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

    private StringFilter classWorkText;

    private StringFilter classWorkFileLink;

    private BooleanFilter assign;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter studentClassWorkTrackId;

    private LongFilter chapterSectionId;

    public ClassClassWorkCriteria() {}

    public ClassClassWorkCriteria(ClassClassWorkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.schoolDate = other.schoolDate == null ? null : other.schoolDate.copy();
        this.studentAssignmentType = other.studentAssignmentType == null ? null : other.studentAssignmentType.copy();
        this.classWorkText = other.classWorkText == null ? null : other.classWorkText.copy();
        this.classWorkFileLink = other.classWorkFileLink == null ? null : other.classWorkFileLink.copy();
        this.assign = other.assign == null ? null : other.assign.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.studentClassWorkTrackId = other.studentClassWorkTrackId == null ? null : other.studentClassWorkTrackId.copy();
        this.chapterSectionId = other.chapterSectionId == null ? null : other.chapterSectionId.copy();
    }

    @Override
    public ClassClassWorkCriteria copy() {
        return new ClassClassWorkCriteria(this);
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

    public StringFilter getClassWorkFileLink() {
        return classWorkFileLink;
    }

    public StringFilter classWorkFileLink() {
        if (classWorkFileLink == null) {
            classWorkFileLink = new StringFilter();
        }
        return classWorkFileLink;
    }

    public void setClassWorkFileLink(StringFilter classWorkFileLink) {
        this.classWorkFileLink = classWorkFileLink;
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
        final ClassClassWorkCriteria that = (ClassClassWorkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(schoolDate, that.schoolDate) &&
            Objects.equals(studentAssignmentType, that.studentAssignmentType) &&
            Objects.equals(classWorkText, that.classWorkText) &&
            Objects.equals(classWorkFileLink, that.classWorkFileLink) &&
            Objects.equals(assign, that.assign) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(studentClassWorkTrackId, that.studentClassWorkTrackId) &&
            Objects.equals(chapterSectionId, that.chapterSectionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            schoolDate,
            studentAssignmentType,
            classWorkText,
            classWorkFileLink,
            assign,
            createDate,
            lastModified,
            cancelDate,
            studentClassWorkTrackId,
            chapterSectionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassClassWorkCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (schoolDate != null ? "schoolDate=" + schoolDate + ", " : "") +
            (studentAssignmentType != null ? "studentAssignmentType=" + studentAssignmentType + ", " : "") +
            (classWorkText != null ? "classWorkText=" + classWorkText + ", " : "") +
            (classWorkFileLink != null ? "classWorkFileLink=" + classWorkFileLink + ", " : "") +
            (assign != null ? "assign=" + assign + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (studentClassWorkTrackId != null ? "studentClassWorkTrackId=" + studentClassWorkTrackId + ", " : "") +
            (chapterSectionId != null ? "chapterSectionId=" + chapterSectionId + ", " : "") +
            "}";
    }
}
