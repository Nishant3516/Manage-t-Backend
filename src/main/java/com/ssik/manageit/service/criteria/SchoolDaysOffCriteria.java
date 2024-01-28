package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.DayOffType;
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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolDaysOff} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolDaysOffResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-days-offs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolDaysOffCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DayOffType
     */
    public static class DayOffTypeFilter extends Filter<DayOffType> {

        public DayOffTypeFilter() {}

        public DayOffTypeFilter(DayOffTypeFilter filter) {
            super(filter);
        }

        @Override
        public DayOffTypeFilter copy() {
            return new DayOffTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DayOffTypeFilter dayOffType;

    private StringFilter dayOffName;

    private StringFilter dayOffDetails;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    public SchoolDaysOffCriteria() {}

    public SchoolDaysOffCriteria(SchoolDaysOffCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dayOffType = other.dayOffType == null ? null : other.dayOffType.copy();
        this.dayOffName = other.dayOffName == null ? null : other.dayOffName.copy();
        this.dayOffDetails = other.dayOffDetails == null ? null : other.dayOffDetails.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public SchoolDaysOffCriteria copy() {
        return new SchoolDaysOffCriteria(this);
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

    public DayOffTypeFilter getDayOffType() {
        return dayOffType;
    }

    public DayOffTypeFilter dayOffType() {
        if (dayOffType == null) {
            dayOffType = new DayOffTypeFilter();
        }
        return dayOffType;
    }

    public void setDayOffType(DayOffTypeFilter dayOffType) {
        this.dayOffType = dayOffType;
    }

    public StringFilter getDayOffName() {
        return dayOffName;
    }

    public StringFilter dayOffName() {
        if (dayOffName == null) {
            dayOffName = new StringFilter();
        }
        return dayOffName;
    }

    public void setDayOffName(StringFilter dayOffName) {
        this.dayOffName = dayOffName;
    }

    public StringFilter getDayOffDetails() {
        return dayOffDetails;
    }

    public StringFilter dayOffDetails() {
        if (dayOffDetails == null) {
            dayOffDetails = new StringFilter();
        }
        return dayOffDetails;
    }

    public void setDayOffDetails(StringFilter dayOffDetails) {
        this.dayOffDetails = dayOffDetails;
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
        final SchoolDaysOffCriteria that = (SchoolDaysOffCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dayOffType, that.dayOffType) &&
            Objects.equals(dayOffName, that.dayOffName) &&
            Objects.equals(dayOffDetails, that.dayOffDetails) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolClassId, that.schoolClassId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dayOffType,
            dayOffName,
            dayOffDetails,
            startDate,
            endDate,
            createDate,
            lastModified,
            cancelDate,
            schoolClassId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolDaysOffCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dayOffType != null ? "dayOffType=" + dayOffType + ", " : "") +
            (dayOffName != null ? "dayOffName=" + dayOffName + ", " : "") +
            (dayOffDetails != null ? "dayOffDetails=" + dayOffDetails + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
