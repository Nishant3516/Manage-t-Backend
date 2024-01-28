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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolEvent} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private StringFilter eventDetails;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    public SchoolEventCriteria() {}

    public SchoolEventCriteria(SchoolEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventName = other.eventName == null ? null : other.eventName.copy();
        this.eventDetails = other.eventDetails == null ? null : other.eventDetails.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public SchoolEventCriteria copy() {
        return new SchoolEventCriteria(this);
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

    public StringFilter getEventName() {
        return eventName;
    }

    public StringFilter eventName() {
        if (eventName == null) {
            eventName = new StringFilter();
        }
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventDetails() {
        return eventDetails;
    }

    public StringFilter eventDetails() {
        if (eventDetails == null) {
            eventDetails = new StringFilter();
        }
        return eventDetails;
    }

    public void setEventDetails(StringFilter eventDetails) {
        this.eventDetails = eventDetails;
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
        final SchoolEventCriteria that = (SchoolEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventDetails, that.eventDetails) &&
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
        return Objects.hash(id, eventName, eventDetails, startDate, endDate, createDate, lastModified, cancelDate, schoolClassId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolEventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventName != null ? "eventName=" + eventName + ", " : "") +
            (eventDetails != null ? "eventDetails=" + eventDetails + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
