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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolNotifications} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolNotificationsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolNotificationsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notificationTitle;

    private StringFilter notificationDetails;

    private StringFilter notificationFileLink;

    private LocalDateFilter showTillDate;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    public SchoolNotificationsCriteria() {}

    public SchoolNotificationsCriteria(SchoolNotificationsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notificationTitle = other.notificationTitle == null ? null : other.notificationTitle.copy();
        this.notificationDetails = other.notificationDetails == null ? null : other.notificationDetails.copy();
        this.notificationFileLink = other.notificationFileLink == null ? null : other.notificationFileLink.copy();
        this.showTillDate = other.showTillDate == null ? null : other.showTillDate.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public SchoolNotificationsCriteria copy() {
        return new SchoolNotificationsCriteria(this);
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

    public StringFilter getNotificationTitle() {
        return notificationTitle;
    }

    public StringFilter notificationTitle() {
        if (notificationTitle == null) {
            notificationTitle = new StringFilter();
        }
        return notificationTitle;
    }

    public void setNotificationTitle(StringFilter notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public StringFilter getNotificationDetails() {
        return notificationDetails;
    }

    public StringFilter notificationDetails() {
        if (notificationDetails == null) {
            notificationDetails = new StringFilter();
        }
        return notificationDetails;
    }

    public void setNotificationDetails(StringFilter notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public StringFilter getNotificationFileLink() {
        return notificationFileLink;
    }

    public StringFilter notificationFileLink() {
        if (notificationFileLink == null) {
            notificationFileLink = new StringFilter();
        }
        return notificationFileLink;
    }

    public void setNotificationFileLink(StringFilter notificationFileLink) {
        this.notificationFileLink = notificationFileLink;
    }

    public LocalDateFilter getShowTillDate() {
        return showTillDate;
    }

    public LocalDateFilter showTillDate() {
        if (showTillDate == null) {
            showTillDate = new LocalDateFilter();
        }
        return showTillDate;
    }

    public void setShowTillDate(LocalDateFilter showTillDate) {
        this.showTillDate = showTillDate;
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
        final SchoolNotificationsCriteria that = (SchoolNotificationsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(notificationTitle, that.notificationTitle) &&
            Objects.equals(notificationDetails, that.notificationDetails) &&
            Objects.equals(notificationFileLink, that.notificationFileLink) &&
            Objects.equals(showTillDate, that.showTillDate) &&
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
            notificationTitle,
            notificationDetails,
            notificationFileLink,
            showTillDate,
            createDate,
            lastModified,
            cancelDate,
            schoolClassId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolNotificationsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (notificationTitle != null ? "notificationTitle=" + notificationTitle + ", " : "") +
            (notificationDetails != null ? "notificationDetails=" + notificationDetails + ", " : "") +
            (notificationFileLink != null ? "notificationFileLink=" + notificationFileLink + ", " : "") +
            (showTillDate != null ? "showTillDate=" + showTillDate + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
