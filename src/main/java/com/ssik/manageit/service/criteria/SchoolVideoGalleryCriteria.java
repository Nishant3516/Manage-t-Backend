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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolVideoGallery} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolVideoGalleryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-video-galleries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolVideoGalleryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter videoTitle;

    private StringFilter videoDescription;

    private StringFilter videoLink;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    public SchoolVideoGalleryCriteria() {}

    public SchoolVideoGalleryCriteria(SchoolVideoGalleryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.videoTitle = other.videoTitle == null ? null : other.videoTitle.copy();
        this.videoDescription = other.videoDescription == null ? null : other.videoDescription.copy();
        this.videoLink = other.videoLink == null ? null : other.videoLink.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
    }

    @Override
    public SchoolVideoGalleryCriteria copy() {
        return new SchoolVideoGalleryCriteria(this);
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

    public StringFilter getVideoTitle() {
        return videoTitle;
    }

    public StringFilter videoTitle() {
        if (videoTitle == null) {
            videoTitle = new StringFilter();
        }
        return videoTitle;
    }

    public void setVideoTitle(StringFilter videoTitle) {
        this.videoTitle = videoTitle;
    }

    public StringFilter getVideoDescription() {
        return videoDescription;
    }

    public StringFilter videoDescription() {
        if (videoDescription == null) {
            videoDescription = new StringFilter();
        }
        return videoDescription;
    }

    public void setVideoDescription(StringFilter videoDescription) {
        this.videoDescription = videoDescription;
    }

    public StringFilter getVideoLink() {
        return videoLink;
    }

    public StringFilter videoLink() {
        if (videoLink == null) {
            videoLink = new StringFilter();
        }
        return videoLink;
    }

    public void setVideoLink(StringFilter videoLink) {
        this.videoLink = videoLink;
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
        final SchoolVideoGalleryCriteria that = (SchoolVideoGalleryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(videoTitle, that.videoTitle) &&
            Objects.equals(videoDescription, that.videoDescription) &&
            Objects.equals(videoLink, that.videoLink) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolClassId, that.schoolClassId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, videoTitle, videoDescription, videoLink, createDate, lastModified, cancelDate, schoolClassId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolVideoGalleryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (videoTitle != null ? "videoTitle=" + videoTitle + ", " : "") +
            (videoDescription != null ? "videoDescription=" + videoDescription + ", " : "") +
            (videoLink != null ? "videoLink=" + videoLink + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            "}";
    }
}
