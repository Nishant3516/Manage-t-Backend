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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassLessionPlanTrack} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassLessionPlanTrackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-lession-plan-tracks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassLessionPlanTrackCriteria implements Serializable, Criteria {

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

    private TaskStatusFilter workStatus;

    private StringFilter remarks;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classLessionPlanId;

    public ClassLessionPlanTrackCriteria() {}

    public ClassLessionPlanTrackCriteria(ClassLessionPlanTrackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.workStatus = other.workStatus == null ? null : other.workStatus.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classLessionPlanId = other.classLessionPlanId == null ? null : other.classLessionPlanId.copy();
    }

    @Override
    public ClassLessionPlanTrackCriteria copy() {
        return new ClassLessionPlanTrackCriteria(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassLessionPlanTrackCriteria that = (ClassLessionPlanTrackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(workStatus, that.workStatus) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classLessionPlanId, that.classLessionPlanId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workStatus, remarks, createDate, lastModified, cancelDate, classLessionPlanId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassLessionPlanTrackCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (workStatus != null ? "workStatus=" + workStatus + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classLessionPlanId != null ? "classLessionPlanId=" + classLessionPlanId + ", " : "") +
            "}";
    }
}
