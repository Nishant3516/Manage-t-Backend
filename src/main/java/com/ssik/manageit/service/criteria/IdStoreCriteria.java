package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.IdType;
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
 * Criteria class for the {@link com.ssik.manageit.domain.IdStore} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.IdStoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /id-stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IdStoreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IdType
     */
    public static class IdTypeFilter extends Filter<IdType> {

        public IdTypeFilter() {}

        public IdTypeFilter(IdTypeFilter filter) {
            super(filter);
        }

        @Override
        public IdTypeFilter copy() {
            return new IdTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IdTypeFilter entrytype;

    private LongFilter lastGeneratedId;

    private LongFilter startId;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolId;

    public IdStoreCriteria() {}

    public IdStoreCriteria(IdStoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entrytype = other.entrytype == null ? null : other.entrytype.copy();
        this.lastGeneratedId = other.lastGeneratedId == null ? null : other.lastGeneratedId.copy();
        this.startId = other.startId == null ? null : other.startId.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolId = other.schoolId == null ? null : other.schoolId.copy();
    }

    @Override
    public IdStoreCriteria copy() {
        return new IdStoreCriteria(this);
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

    public IdTypeFilter getEntrytype() {
        return entrytype;
    }

    public IdTypeFilter entrytype() {
        if (entrytype == null) {
            entrytype = new IdTypeFilter();
        }
        return entrytype;
    }

    public void setEntrytype(IdTypeFilter entrytype) {
        this.entrytype = entrytype;
    }

    public LongFilter getLastGeneratedId() {
        return lastGeneratedId;
    }

    public LongFilter lastGeneratedId() {
        if (lastGeneratedId == null) {
            lastGeneratedId = new LongFilter();
        }
        return lastGeneratedId;
    }

    public void setLastGeneratedId(LongFilter lastGeneratedId) {
        this.lastGeneratedId = lastGeneratedId;
    }

    public LongFilter getStartId() {
        return startId;
    }

    public LongFilter startId() {
        if (startId == null) {
            startId = new LongFilter();
        }
        return startId;
    }

    public void setStartId(LongFilter startId) {
        this.startId = startId;
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

    public LongFilter getSchoolId() {
        return schoolId;
    }

    public LongFilter schoolId() {
        if (schoolId == null) {
            schoolId = new LongFilter();
        }
        return schoolId;
    }

    public void setSchoolId(LongFilter schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IdStoreCriteria that = (IdStoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entrytype, that.entrytype) &&
            Objects.equals(lastGeneratedId, that.lastGeneratedId) &&
            Objects.equals(startId, that.startId) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolId, that.schoolId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entrytype, lastGeneratedId, startId, createDate, lastModified, cancelDate, schoolId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdStoreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entrytype != null ? "entrytype=" + entrytype + ", " : "") +
            (lastGeneratedId != null ? "lastGeneratedId=" + lastGeneratedId + ", " : "") +
            (startId != null ? "startId=" + startId + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolId != null ? "schoolId=" + schoolId + ", " : "") +
            "}";
    }
}
