package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.WorkStatus;
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
 * Criteria class for the {@link com.ssik.manageit.domain.StudentHomeWorkTrack} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentHomeWorkTrackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-home-work-tracks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentHomeWorkTrackCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkStatus
     */
    public static class WorkStatusFilter extends Filter<WorkStatus> {

        public WorkStatusFilter() {}

        public WorkStatusFilter(WorkStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkStatusFilter copy() {
            return new WorkStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WorkStatusFilter workStatus;

    private StringFilter remarks;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classStudentId;

    private LongFilter classHomeWorkId;

    public StudentHomeWorkTrackCriteria() {}

    public StudentHomeWorkTrackCriteria(StudentHomeWorkTrackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.workStatus = other.workStatus == null ? null : other.workStatus.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
        this.classHomeWorkId = other.classHomeWorkId == null ? null : other.classHomeWorkId.copy();
    }

    @Override
    public StudentHomeWorkTrackCriteria copy() {
        return new StudentHomeWorkTrackCriteria(this);
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

    public WorkStatusFilter getWorkStatus() {
        return workStatus;
    }

    public WorkStatusFilter workStatus() {
        if (workStatus == null) {
            workStatus = new WorkStatusFilter();
        }
        return workStatus;
    }

    public void setWorkStatus(WorkStatusFilter workStatus) {
        this.workStatus = workStatus;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public StringFilter remarks() {
        if (remarks == null) {
            remarks = new StringFilter();
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentHomeWorkTrackCriteria that = (StudentHomeWorkTrackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(workStatus, that.workStatus) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classStudentId, that.classStudentId) &&
            Objects.equals(classHomeWorkId, that.classHomeWorkId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workStatus, remarks, createDate, lastModified, cancelDate, classStudentId, classHomeWorkId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentHomeWorkTrackCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (workStatus != null ? "workStatus=" + workStatus + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            (classHomeWorkId != null ? "classHomeWorkId=" + classHomeWorkId + ", " : "") +
            "}";
    }
}
