package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.FeeYear;
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
 * Criteria class for the {@link com.ssik.manageit.domain.StudentDiscount} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentDiscountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-discounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentDiscountCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FeeYear
     */
    public static class FeeYearFilter extends Filter<FeeYear> {

        public FeeYearFilter() {}

        public FeeYearFilter(FeeYearFilter filter) {
            super(filter);
        }

        @Override
        public FeeYearFilter copy() {
            return new FeeYearFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FeeYearFilter feeYear;

    private IntegerFilter dueDate;

    private DoubleFilter janFeeDisc;

    private DoubleFilter febFeeDisc;

    private DoubleFilter marFeeDisc;

    private DoubleFilter aprFeeDisc;

    private DoubleFilter mayFeeDisc;

    private DoubleFilter junFeeDisc;

    private DoubleFilter julFeeDisc;

    private DoubleFilter augFeeDisc;

    private DoubleFilter sepFeeDisc;

    private DoubleFilter octFeeDisc;

    private DoubleFilter novFeeDisc;

    private DoubleFilter decFeeDisc;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolLedgerHeadId;

    private LongFilter classStudentId;

    public StudentDiscountCriteria() {}

    public StudentDiscountCriteria(StudentDiscountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.feeYear = other.feeYear == null ? null : other.feeYear.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.janFeeDisc = other.janFeeDisc == null ? null : other.janFeeDisc.copy();
        this.febFeeDisc = other.febFeeDisc == null ? null : other.febFeeDisc.copy();
        this.marFeeDisc = other.marFeeDisc == null ? null : other.marFeeDisc.copy();
        this.aprFeeDisc = other.aprFeeDisc == null ? null : other.aprFeeDisc.copy();
        this.mayFeeDisc = other.mayFeeDisc == null ? null : other.mayFeeDisc.copy();
        this.junFeeDisc = other.junFeeDisc == null ? null : other.junFeeDisc.copy();
        this.julFeeDisc = other.julFeeDisc == null ? null : other.julFeeDisc.copy();
        this.augFeeDisc = other.augFeeDisc == null ? null : other.augFeeDisc.copy();
        this.sepFeeDisc = other.sepFeeDisc == null ? null : other.sepFeeDisc.copy();
        this.octFeeDisc = other.octFeeDisc == null ? null : other.octFeeDisc.copy();
        this.novFeeDisc = other.novFeeDisc == null ? null : other.novFeeDisc.copy();
        this.decFeeDisc = other.decFeeDisc == null ? null : other.decFeeDisc.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolLedgerHeadId = other.schoolLedgerHeadId == null ? null : other.schoolLedgerHeadId.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
    }

    @Override
    public StudentDiscountCriteria copy() {
        return new StudentDiscountCriteria(this);
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

    public FeeYearFilter getFeeYear() {
        return feeYear;
    }

    public FeeYearFilter feeYear() {
        if (feeYear == null) {
            feeYear = new FeeYearFilter();
        }
        return feeYear;
    }

    public void setFeeYear(FeeYearFilter feeYear) {
        this.feeYear = feeYear;
    }

    public IntegerFilter getDueDate() {
        return dueDate;
    }

    public IntegerFilter dueDate() {
        if (dueDate == null) {
            dueDate = new IntegerFilter();
        }
        return dueDate;
    }

    public void setDueDate(IntegerFilter dueDate) {
        this.dueDate = dueDate;
    }

    public DoubleFilter getJanFeeDisc() {
        return janFeeDisc;
    }

    public DoubleFilter janFeeDisc() {
        if (janFeeDisc == null) {
            janFeeDisc = new DoubleFilter();
        }
        return janFeeDisc;
    }

    public void setJanFeeDisc(DoubleFilter janFeeDisc) {
        this.janFeeDisc = janFeeDisc;
    }

    public DoubleFilter getFebFeeDisc() {
        return febFeeDisc;
    }

    public DoubleFilter febFeeDisc() {
        if (febFeeDisc == null) {
            febFeeDisc = new DoubleFilter();
        }
        return febFeeDisc;
    }

    public void setFebFeeDisc(DoubleFilter febFeeDisc) {
        this.febFeeDisc = febFeeDisc;
    }

    public DoubleFilter getMarFeeDisc() {
        return marFeeDisc;
    }

    public DoubleFilter marFeeDisc() {
        if (marFeeDisc == null) {
            marFeeDisc = new DoubleFilter();
        }
        return marFeeDisc;
    }

    public void setMarFeeDisc(DoubleFilter marFeeDisc) {
        this.marFeeDisc = marFeeDisc;
    }

    public DoubleFilter getAprFeeDisc() {
        return aprFeeDisc;
    }

    public DoubleFilter aprFeeDisc() {
        if (aprFeeDisc == null) {
            aprFeeDisc = new DoubleFilter();
        }
        return aprFeeDisc;
    }

    public void setAprFeeDisc(DoubleFilter aprFeeDisc) {
        this.aprFeeDisc = aprFeeDisc;
    }

    public DoubleFilter getMayFeeDisc() {
        return mayFeeDisc;
    }

    public DoubleFilter mayFeeDisc() {
        if (mayFeeDisc == null) {
            mayFeeDisc = new DoubleFilter();
        }
        return mayFeeDisc;
    }

    public void setMayFeeDisc(DoubleFilter mayFeeDisc) {
        this.mayFeeDisc = mayFeeDisc;
    }

    public DoubleFilter getJunFeeDisc() {
        return junFeeDisc;
    }

    public DoubleFilter junFeeDisc() {
        if (junFeeDisc == null) {
            junFeeDisc = new DoubleFilter();
        }
        return junFeeDisc;
    }

    public void setJunFeeDisc(DoubleFilter junFeeDisc) {
        this.junFeeDisc = junFeeDisc;
    }

    public DoubleFilter getJulFeeDisc() {
        return julFeeDisc;
    }

    public DoubleFilter julFeeDisc() {
        if (julFeeDisc == null) {
            julFeeDisc = new DoubleFilter();
        }
        return julFeeDisc;
    }

    public void setJulFeeDisc(DoubleFilter julFeeDisc) {
        this.julFeeDisc = julFeeDisc;
    }

    public DoubleFilter getAugFeeDisc() {
        return augFeeDisc;
    }

    public DoubleFilter augFeeDisc() {
        if (augFeeDisc == null) {
            augFeeDisc = new DoubleFilter();
        }
        return augFeeDisc;
    }

    public void setAugFeeDisc(DoubleFilter augFeeDisc) {
        this.augFeeDisc = augFeeDisc;
    }

    public DoubleFilter getSepFeeDisc() {
        return sepFeeDisc;
    }

    public DoubleFilter sepFeeDisc() {
        if (sepFeeDisc == null) {
            sepFeeDisc = new DoubleFilter();
        }
        return sepFeeDisc;
    }

    public void setSepFeeDisc(DoubleFilter sepFeeDisc) {
        this.sepFeeDisc = sepFeeDisc;
    }

    public DoubleFilter getOctFeeDisc() {
        return octFeeDisc;
    }

    public DoubleFilter octFeeDisc() {
        if (octFeeDisc == null) {
            octFeeDisc = new DoubleFilter();
        }
        return octFeeDisc;
    }

    public void setOctFeeDisc(DoubleFilter octFeeDisc) {
        this.octFeeDisc = octFeeDisc;
    }

    public DoubleFilter getNovFeeDisc() {
        return novFeeDisc;
    }

    public DoubleFilter novFeeDisc() {
        if (novFeeDisc == null) {
            novFeeDisc = new DoubleFilter();
        }
        return novFeeDisc;
    }

    public void setNovFeeDisc(DoubleFilter novFeeDisc) {
        this.novFeeDisc = novFeeDisc;
    }

    public DoubleFilter getDecFeeDisc() {
        return decFeeDisc;
    }

    public DoubleFilter decFeeDisc() {
        if (decFeeDisc == null) {
            decFeeDisc = new DoubleFilter();
        }
        return decFeeDisc;
    }

    public void setDecFeeDisc(DoubleFilter decFeeDisc) {
        this.decFeeDisc = decFeeDisc;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentDiscountCriteria that = (StudentDiscountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(feeYear, that.feeYear) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(janFeeDisc, that.janFeeDisc) &&
            Objects.equals(febFeeDisc, that.febFeeDisc) &&
            Objects.equals(marFeeDisc, that.marFeeDisc) &&
            Objects.equals(aprFeeDisc, that.aprFeeDisc) &&
            Objects.equals(mayFeeDisc, that.mayFeeDisc) &&
            Objects.equals(junFeeDisc, that.junFeeDisc) &&
            Objects.equals(julFeeDisc, that.julFeeDisc) &&
            Objects.equals(augFeeDisc, that.augFeeDisc) &&
            Objects.equals(sepFeeDisc, that.sepFeeDisc) &&
            Objects.equals(octFeeDisc, that.octFeeDisc) &&
            Objects.equals(novFeeDisc, that.novFeeDisc) &&
            Objects.equals(decFeeDisc, that.decFeeDisc) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolLedgerHeadId, that.schoolLedgerHeadId) &&
            Objects.equals(classStudentId, that.classStudentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            feeYear,
            dueDate,
            janFeeDisc,
            febFeeDisc,
            marFeeDisc,
            aprFeeDisc,
            mayFeeDisc,
            junFeeDisc,
            julFeeDisc,
            augFeeDisc,
            sepFeeDisc,
            octFeeDisc,
            novFeeDisc,
            decFeeDisc,
            createDate,
            lastModified,
            cancelDate,
            schoolLedgerHeadId,
            classStudentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentDiscountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (feeYear != null ? "feeYear=" + feeYear + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (janFeeDisc != null ? "janFeeDisc=" + janFeeDisc + ", " : "") +
            (febFeeDisc != null ? "febFeeDisc=" + febFeeDisc + ", " : "") +
            (marFeeDisc != null ? "marFeeDisc=" + marFeeDisc + ", " : "") +
            (aprFeeDisc != null ? "aprFeeDisc=" + aprFeeDisc + ", " : "") +
            (mayFeeDisc != null ? "mayFeeDisc=" + mayFeeDisc + ", " : "") +
            (junFeeDisc != null ? "junFeeDisc=" + junFeeDisc + ", " : "") +
            (julFeeDisc != null ? "julFeeDisc=" + julFeeDisc + ", " : "") +
            (augFeeDisc != null ? "augFeeDisc=" + augFeeDisc + ", " : "") +
            (sepFeeDisc != null ? "sepFeeDisc=" + sepFeeDisc + ", " : "") +
            (octFeeDisc != null ? "octFeeDisc=" + octFeeDisc + ", " : "") +
            (novFeeDisc != null ? "novFeeDisc=" + novFeeDisc + ", " : "") +
            (decFeeDisc != null ? "decFeeDisc=" + decFeeDisc + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolLedgerHeadId != null ? "schoolLedgerHeadId=" + schoolLedgerHeadId + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            "}";
    }
}
