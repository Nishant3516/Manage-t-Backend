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
 * Criteria class for the {@link com.ssik.manageit.domain.AuditLog} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.AuditLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuditLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userName;

    private StringFilter userDeviceDetails;

    private StringFilter action;

    private StringFilter data1;

    private StringFilter data2;

    private StringFilter data3;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolId;

    private LongFilter schoolUserId;

    private Boolean distinct;

    public AuditLogCriteria() {}

    public AuditLogCriteria(AuditLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.userDeviceDetails = other.userDeviceDetails == null ? null : other.userDeviceDetails.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.data1 = other.data1 == null ? null : other.data1.copy();
        this.data2 = other.data2 == null ? null : other.data2.copy();
        this.data3 = other.data3 == null ? null : other.data3.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolId = other.schoolId == null ? null : other.schoolId.copy();
        this.schoolUserId = other.schoolUserId == null ? null : other.schoolUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AuditLogCriteria copy() {
        return new AuditLogCriteria(this);
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

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getUserDeviceDetails() {
        return userDeviceDetails;
    }

    public StringFilter userDeviceDetails() {
        if (userDeviceDetails == null) {
            userDeviceDetails = new StringFilter();
        }
        return userDeviceDetails;
    }

    public void setUserDeviceDetails(StringFilter userDeviceDetails) {
        this.userDeviceDetails = userDeviceDetails;
    }

    public StringFilter getAction() {
        return action;
    }

    public StringFilter action() {
        if (action == null) {
            action = new StringFilter();
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getData1() {
        return data1;
    }

    public StringFilter data1() {
        if (data1 == null) {
            data1 = new StringFilter();
        }
        return data1;
    }

    public void setData1(StringFilter data1) {
        this.data1 = data1;
    }

    public StringFilter getData2() {
        return data2;
    }

    public StringFilter data2() {
        if (data2 == null) {
            data2 = new StringFilter();
        }
        return data2;
    }

    public void setData2(StringFilter data2) {
        this.data2 = data2;
    }

    public StringFilter getData3() {
        return data3;
    }

    public StringFilter data3() {
        if (data3 == null) {
            data3 = new StringFilter();
        }
        return data3;
    }

    public void setData3(StringFilter data3) {
        this.data3 = data3;
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

    public LongFilter getSchoolUserId() {
        return schoolUserId;
    }

    public LongFilter schoolUserId() {
        if (schoolUserId == null) {
            schoolUserId = new LongFilter();
        }
        return schoolUserId;
    }

    public void setSchoolUserId(LongFilter schoolUserId) {
        this.schoolUserId = schoolUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditLogCriteria that = (AuditLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(userDeviceDetails, that.userDeviceDetails) &&
            Objects.equals(action, that.action) &&
            Objects.equals(data1, that.data1) &&
            Objects.equals(data2, that.data2) &&
            Objects.equals(data3, that.data3) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolId, that.schoolId) &&
            Objects.equals(schoolUserId, that.schoolUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userName,
            userDeviceDetails,
            action,
            data1,
            data2,
            data3,
            createDate,
            lastModified,
            cancelDate,
            schoolId,
            schoolUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (userDeviceDetails != null ? "userDeviceDetails=" + userDeviceDetails + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (data1 != null ? "data1=" + data1 + ", " : "") +
            (data2 != null ? "data2=" + data2 + ", " : "") +
            (data3 != null ? "data3=" + data3 + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolId != null ? "schoolId=" + schoolId + ", " : "") +
            (schoolUserId != null ? "schoolUserId=" + schoolUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
