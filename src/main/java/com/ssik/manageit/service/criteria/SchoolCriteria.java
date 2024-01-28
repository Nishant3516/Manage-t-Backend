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
 * Criteria class for the {@link com.ssik.manageit.domain.School} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schools?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter groupName;

    private StringFilter schoolName;

    private StringFilter address;

    private StringFilter afflNumber;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    private LongFilter schoolLedgerHeadId;

    private LongFilter idStoreId;

    private LongFilter auditLogId;

    public SchoolCriteria() {}

    public SchoolCriteria(SchoolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.groupName = other.groupName == null ? null : other.groupName.copy();
        this.schoolName = other.schoolName == null ? null : other.schoolName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.afflNumber = other.afflNumber == null ? null : other.afflNumber.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.schoolLedgerHeadId = other.schoolLedgerHeadId == null ? null : other.schoolLedgerHeadId.copy();
        this.idStoreId = other.idStoreId == null ? null : other.idStoreId.copy();
        this.auditLogId = other.auditLogId == null ? null : other.auditLogId.copy();
    }

    @Override
    public SchoolCriteria copy() {
        return new SchoolCriteria(this);
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

    public StringFilter getGroupName() {
        return groupName;
    }

    public StringFilter groupName() {
        if (groupName == null) {
            groupName = new StringFilter();
        }
        return groupName;
    }

    public void setGroupName(StringFilter groupName) {
        this.groupName = groupName;
    }

    public StringFilter getSchoolName() {
        return schoolName;
    }

    public StringFilter schoolName() {
        if (schoolName == null) {
            schoolName = new StringFilter();
        }
        return schoolName;
    }

    public void setSchoolName(StringFilter schoolName) {
        this.schoolName = schoolName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getAfflNumber() {
        return afflNumber;
    }

    public StringFilter afflNumber() {
        if (afflNumber == null) {
            afflNumber = new StringFilter();
        }
        return afflNumber;
    }

    public void setAfflNumber(StringFilter afflNumber) {
        this.afflNumber = afflNumber;
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

    public LongFilter getSchoolLedgerHeadId() {
        return schoolLedgerHeadId;
    }

    public LongFilter schoolLedgerHeadId() {
        if (schoolLedgerHeadId == null) {
            schoolLedgerHeadId = new LongFilter();
        }
        return schoolLedgerHeadId;
    }

    public void setSchoolLedgerHeadId(LongFilter schoolLedgerHeadId) {
        this.schoolLedgerHeadId = schoolLedgerHeadId;
    }

    public LongFilter getIdStoreId() {
        return idStoreId;
    }

    public LongFilter idStoreId() {
        if (idStoreId == null) {
            idStoreId = new LongFilter();
        }
        return idStoreId;
    }

    public void setIdStoreId(LongFilter idStoreId) {
        this.idStoreId = idStoreId;
    }

    public LongFilter getAuditLogId() {
        return auditLogId;
    }

    public LongFilter auditLogId() {
        if (auditLogId == null) {
            auditLogId = new LongFilter();
        }
        return auditLogId;
    }

    public void setAuditLogId(LongFilter auditLogId) {
        this.auditLogId = auditLogId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SchoolCriteria that = (SchoolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(groupName, that.groupName) &&
            Objects.equals(schoolName, that.schoolName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(afflNumber, that.afflNumber) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(schoolLedgerHeadId, that.schoolLedgerHeadId) &&
            Objects.equals(idStoreId, that.idStoreId) &&
            Objects.equals(auditLogId, that.auditLogId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            groupName,
            schoolName,
            address,
            afflNumber,
            createDate,
            lastModified,
            cancelDate,
            schoolClassId,
            schoolLedgerHeadId,
            idStoreId,
            auditLogId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (groupName != null ? "groupName=" + groupName + ", " : "") +
            (schoolName != null ? "schoolName=" + schoolName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (afflNumber != null ? "afflNumber=" + afflNumber + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (schoolLedgerHeadId != null ? "schoolLedgerHeadId=" + schoolLedgerHeadId + ", " : "") +
            (idStoreId != null ? "idStoreId=" + idStoreId + ", " : "") +
            (auditLogId != null ? "auditLogId=" + auditLogId + ", " : "") +
            "}";
    }
}
