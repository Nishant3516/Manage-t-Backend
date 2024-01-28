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
 * Criteria class for the {@link com.ssik.manageit.domain.SchoolClass} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.SchoolClassResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolClassCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter className;

    private StringFilter classLongName;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classStudentId;

    private LongFilter classLessionPlanId;

    private LongFilter schoolId;

    private LongFilter schoolNotificationsId;

    private LongFilter classFeeId;

    private LongFilter classSubjectId;

    private LongFilter schoolUserId;

    private LongFilter schoolDaysOffId;

    private LongFilter schoolEventId;

    private LongFilter schoolPictureGalleryId;

    private LongFilter vchoolVideoGalleryId;

    private LongFilter schoolReportId;

    public SchoolClassCriteria() {}

    public SchoolClassCriteria(SchoolClassCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.className = other.className == null ? null : other.className.copy();
        this.classLongName = other.classLongName == null ? null : other.classLongName.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
        this.classLessionPlanId = other.classLessionPlanId == null ? null : other.classLessionPlanId.copy();
        this.schoolId = other.schoolId == null ? null : other.schoolId.copy();
        this.schoolNotificationsId = other.schoolNotificationsId == null ? null : other.schoolNotificationsId.copy();
        this.classFeeId = other.classFeeId == null ? null : other.classFeeId.copy();
        this.classSubjectId = other.classSubjectId == null ? null : other.classSubjectId.copy();
        this.schoolUserId = other.schoolUserId == null ? null : other.schoolUserId.copy();
        this.schoolDaysOffId = other.schoolDaysOffId == null ? null : other.schoolDaysOffId.copy();
        this.schoolEventId = other.schoolEventId == null ? null : other.schoolEventId.copy();
        this.schoolPictureGalleryId = other.schoolPictureGalleryId == null ? null : other.schoolPictureGalleryId.copy();
        this.vchoolVideoGalleryId = other.vchoolVideoGalleryId == null ? null : other.vchoolVideoGalleryId.copy();
        this.schoolReportId = other.schoolReportId == null ? null : other.schoolReportId.copy();
    }

    @Override
    public SchoolClassCriteria copy() {
        return new SchoolClassCriteria(this);
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

    public StringFilter getClassName() {
        return className;
    }

    public StringFilter className() {
        if (className == null) {
            className = new StringFilter();
        }
        return className;
    }

    public void setClassName(StringFilter className) {
        this.className = className;
    }

    public StringFilter getClassLongName() {
        return classLongName;
    }

    public StringFilter classLongName() {
        if (classLongName == null) {
            classLongName = new StringFilter();
        }
        return classLongName;
    }

    public void setClassLongName(StringFilter classLongName) {
        this.classLongName = classLongName;
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

    public LongFilter getClassStudentId() {
        return classStudentId;
    }

    public LongFilter classStudentId() {
        if (classStudentId == null) {
            classStudentId = new LongFilter();
        }
        return classStudentId;
    }

    public void setClassStudentId(LongFilter classStudentId) {
        this.classStudentId = classStudentId;
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

    public LongFilter getSchoolNotificationsId() {
        return schoolNotificationsId;
    }

    public LongFilter schoolNotificationsId() {
        if (schoolNotificationsId == null) {
            schoolNotificationsId = new LongFilter();
        }
        return schoolNotificationsId;
    }

    public void setSchoolNotificationsId(LongFilter schoolNotificationsId) {
        this.schoolNotificationsId = schoolNotificationsId;
    }

    public LongFilter getClassFeeId() {
        return classFeeId;
    }

    public LongFilter classFeeId() {
        if (classFeeId == null) {
            classFeeId = new LongFilter();
        }
        return classFeeId;
    }

    public void setClassFeeId(LongFilter classFeeId) {
        this.classFeeId = classFeeId;
    }

    public LongFilter getClassSubjectId() {
        return classSubjectId;
    }

    public LongFilter classSubjectId() {
        if (classSubjectId == null) {
            classSubjectId = new LongFilter();
        }
        return classSubjectId;
    }

    public void setClassSubjectId(LongFilter classSubjectId) {
        this.classSubjectId = classSubjectId;
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

    public LongFilter getSchoolDaysOffId() {
        return schoolDaysOffId;
    }

    public LongFilter schoolDaysOffId() {
        if (schoolDaysOffId == null) {
            schoolDaysOffId = new LongFilter();
        }
        return schoolDaysOffId;
    }

    public void setSchoolDaysOffId(LongFilter schoolDaysOffId) {
        this.schoolDaysOffId = schoolDaysOffId;
    }

    public LongFilter getSchoolEventId() {
        return schoolEventId;
    }

    public LongFilter schoolEventId() {
        if (schoolEventId == null) {
            schoolEventId = new LongFilter();
        }
        return schoolEventId;
    }

    public void setSchoolEventId(LongFilter schoolEventId) {
        this.schoolEventId = schoolEventId;
    }

    public LongFilter getSchoolPictureGalleryId() {
        return schoolPictureGalleryId;
    }

    public LongFilter schoolPictureGalleryId() {
        if (schoolPictureGalleryId == null) {
            schoolPictureGalleryId = new LongFilter();
        }
        return schoolPictureGalleryId;
    }

    public void setSchoolPictureGalleryId(LongFilter schoolPictureGalleryId) {
        this.schoolPictureGalleryId = schoolPictureGalleryId;
    }

    public LongFilter getVchoolVideoGalleryId() {
        return vchoolVideoGalleryId;
    }

    public LongFilter vchoolVideoGalleryId() {
        if (vchoolVideoGalleryId == null) {
            vchoolVideoGalleryId = new LongFilter();
        }
        return vchoolVideoGalleryId;
    }

    public void setVchoolVideoGalleryId(LongFilter vchoolVideoGalleryId) {
        this.vchoolVideoGalleryId = vchoolVideoGalleryId;
    }

    public LongFilter getSchoolReportId() {
        return schoolReportId;
    }

    public LongFilter schoolReportId() {
        if (schoolReportId == null) {
            schoolReportId = new LongFilter();
        }
        return schoolReportId;
    }

    public void setSchoolReportId(LongFilter schoolReportId) {
        this.schoolReportId = schoolReportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SchoolClassCriteria that = (SchoolClassCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(className, that.className) &&
            Objects.equals(classLongName, that.classLongName) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classStudentId, that.classStudentId) &&
            Objects.equals(classLessionPlanId, that.classLessionPlanId) &&
            Objects.equals(schoolId, that.schoolId) &&
            Objects.equals(schoolNotificationsId, that.schoolNotificationsId) &&
            Objects.equals(classFeeId, that.classFeeId) &&
            Objects.equals(classSubjectId, that.classSubjectId) &&
            Objects.equals(schoolUserId, that.schoolUserId) &&
            Objects.equals(schoolDaysOffId, that.schoolDaysOffId) &&
            Objects.equals(schoolEventId, that.schoolEventId) &&
            Objects.equals(schoolPictureGalleryId, that.schoolPictureGalleryId) &&
            Objects.equals(vchoolVideoGalleryId, that.vchoolVideoGalleryId) &&
            Objects.equals(schoolReportId, that.schoolReportId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            className,
            classLongName,
            createDate,
            lastModified,
            cancelDate,
            classStudentId,
            classLessionPlanId,
            schoolId,
            schoolNotificationsId,
            classFeeId,
            classSubjectId,
            schoolUserId,
            schoolDaysOffId,
            schoolEventId,
            schoolPictureGalleryId,
            vchoolVideoGalleryId,
            schoolReportId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolClassCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (className != null ? "className=" + className + ", " : "") +
            (classLongName != null ? "classLongName=" + classLongName + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            (classLessionPlanId != null ? "classLessionPlanId=" + classLessionPlanId + ", " : "") +
            (schoolId != null ? "schoolId=" + schoolId + ", " : "") +
            (schoolNotificationsId != null ? "schoolNotificationsId=" + schoolNotificationsId + ", " : "") +
            (classFeeId != null ? "classFeeId=" + classFeeId + ", " : "") +
            (classSubjectId != null ? "classSubjectId=" + classSubjectId + ", " : "") +
            (schoolUserId != null ? "schoolUserId=" + schoolUserId + ", " : "") +
            (schoolDaysOffId != null ? "schoolDaysOffId=" + schoolDaysOffId + ", " : "") +
            (schoolEventId != null ? "schoolEventId=" + schoolEventId + ", " : "") +
            (schoolPictureGalleryId != null ? "schoolPictureGalleryId=" + schoolPictureGalleryId + ", " : "") +
            (vchoolVideoGalleryId != null ? "vchoolVideoGalleryId=" + vchoolVideoGalleryId + ", " : "") +
            (schoolReportId != null ? "schoolReportId=" + schoolReportId + ", " : "") +
            "}";
    }
}
