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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolPictureGallery} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolPictureGalleryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-picture-galleries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolPictureGalleryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pictureTitle;

    private StringFilter pictureDescription;

    private StringFilter pictureLink;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    public SchoolPictureGalleryCriteria() {}

    public SchoolPictureGalleryCriteria(SchoolPictureGalleryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pictureTitle = other.pictureTitle == null ? null : other.pictureTitle.copy();
        this.pictureDescription = other.pictureDescription == null ? null : other.pictureDescription.copy();
        this.pictureLink = other.pictureLink == null ? null : other.pictureLink.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public SchoolPictureGalleryCriteria copy() {
        return new SchoolPictureGalleryCriteria(this);
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

    public StringFilter getPictureTitle() {
        return pictureTitle;
    }

    public StringFilter pictureTitle() {
        if (pictureTitle == null) {
            pictureTitle = new StringFilter();
        }
        return pictureTitle;
    }

    public void setPictureTitle(StringFilter pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public StringFilter getPictureDescription() {
        return pictureDescription;
    }

    public StringFilter pictureDescription() {
        if (pictureDescription == null) {
            pictureDescription = new StringFilter();
        }
        return pictureDescription;
    }

    public void setPictureDescription(StringFilter pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public StringFilter getPictureLink() {
        return pictureLink;
    }

    public StringFilter pictureLink() {
        if (pictureLink == null) {
            pictureLink = new StringFilter();
        }
        return pictureLink;
    }

    public void setPictureLink(StringFilter pictureLink) {
        this.pictureLink = pictureLink;
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
        final SchoolPictureGalleryCriteria that = (SchoolPictureGalleryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pictureTitle, that.pictureTitle) &&
            Objects.equals(pictureDescription, that.pictureDescription) &&
            Objects.equals(pictureLink, that.pictureLink) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolClassId, that.schoolClassId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureTitle, pictureDescription, pictureLink, createDate, lastModified, cancelDate, schoolClassId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolPictureGalleryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pictureTitle != null ? "pictureTitle=" + pictureTitle + ", " : "") +
            (pictureDescription != null ? "pictureDescription=" + pictureDescription + ", " : "") +
            (pictureLink != null ? "pictureLink=" + pictureLink + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
