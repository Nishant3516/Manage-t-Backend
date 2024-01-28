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
 * Criteria class for the {@link com.ssik.manageit.domain.STRoute} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.STRouteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /st-routes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class STRouteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transportRouteName;

    private DoubleFilter routeCharge;

    private StringFilter transportRouteAddress;

    private StringFilter contactNumber;

    private LocalDateFilter createDate;

    private LocalDateFilter cancelDate;

    private StringFilter remarks;

    private LongFilter sTIncomeExpensesId;

    private Boolean distinct;

    public STRouteCriteria() {}

    public STRouteCriteria(STRouteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transportRouteName = other.transportRouteName == null ? null : other.transportRouteName.copy();
        this.routeCharge = other.routeCharge == null ? null : other.routeCharge.copy();
        this.transportRouteAddress = other.transportRouteAddress == null ? null : other.transportRouteAddress.copy();
        this.contactNumber = other.contactNumber == null ? null : other.contactNumber.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.sTIncomeExpensesId = other.sTIncomeExpensesId == null ? null : other.sTIncomeExpensesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public STRouteCriteria copy() {
        return new STRouteCriteria(this);
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

    public StringFilter getTransportRouteName() {
        return transportRouteName;
    }

    public StringFilter transportRouteName() {
        if (transportRouteName == null) {
            transportRouteName = new StringFilter();
        }
        return transportRouteName;
    }

    public void setTransportRouteName(StringFilter transportRouteName) {
        this.transportRouteName = transportRouteName;
    }

    public DoubleFilter getRouteCharge() {
        return routeCharge;
    }

    public DoubleFilter routeCharge() {
        if (routeCharge == null) {
            routeCharge = new DoubleFilter();
        }
        return routeCharge;
    }

    public void setRouteCharge(DoubleFilter routeCharge) {
        this.routeCharge = routeCharge;
    }

    public StringFilter getTransportRouteAddress() {
        return transportRouteAddress;
    }

    public StringFilter transportRouteAddress() {
        if (transportRouteAddress == null) {
            transportRouteAddress = new StringFilter();
        }
        return transportRouteAddress;
    }

    public void setTransportRouteAddress(StringFilter transportRouteAddress) {
        this.transportRouteAddress = transportRouteAddress;
    }

    public StringFilter getContactNumber() {
        return contactNumber;
    }

    public StringFilter contactNumber() {
        if (contactNumber == null) {
            contactNumber = new StringFilter();
        }
        return contactNumber;
    }

    public void setContactNumber(StringFilter contactNumber) {
        this.contactNumber = contactNumber;
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

    public LongFilter getSTIncomeExpensesId() {
        return sTIncomeExpensesId;
    }

    public LongFilter sTIncomeExpensesId() {
        if (sTIncomeExpensesId == null) {
            sTIncomeExpensesId = new LongFilter();
        }
        return sTIncomeExpensesId;
    }

    public void setSTIncomeExpensesId(LongFilter sTIncomeExpensesId) {
        this.sTIncomeExpensesId = sTIncomeExpensesId;
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
        final STRouteCriteria that = (STRouteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transportRouteName, that.transportRouteName) &&
            Objects.equals(routeCharge, that.routeCharge) &&
            Objects.equals(transportRouteAddress, that.transportRouteAddress) &&
            Objects.equals(contactNumber, that.contactNumber) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(sTIncomeExpensesId, that.sTIncomeExpensesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transportRouteName,
            routeCharge,
            transportRouteAddress,
            contactNumber,
            createDate,
            cancelDate,
            remarks,
            sTIncomeExpensesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "STRouteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transportRouteName != null ? "transportRouteName=" + transportRouteName + ", " : "") +
            (routeCharge != null ? "routeCharge=" + routeCharge + ", " : "") +
            (transportRouteAddress != null ? "transportRouteAddress=" + transportRouteAddress + ", " : "") +
            (contactNumber != null ? "contactNumber=" + contactNumber + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (sTIncomeExpensesId != null ? "sTIncomeExpensesId=" + sTIncomeExpensesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
