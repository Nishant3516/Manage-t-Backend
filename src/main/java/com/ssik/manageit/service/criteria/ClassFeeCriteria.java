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
 * Criteria class for the {@link com.ssik.manageit.domain.ClassFee} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.ClassFeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-fees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassFeeCriteria implements Serializable, Criteria {

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

    private DoubleFilter janFee;

    private DoubleFilter febFee;

    private DoubleFilter marFee;

    private DoubleFilter aprFee;

    private DoubleFilter mayFee;

    private DoubleFilter junFee;

    private DoubleFilter julFee;

    private DoubleFilter augFee;

    private DoubleFilter sepFee;

    private DoubleFilter octFee;

    private DoubleFilter novFee;

    private DoubleFilter decFee;

    private LocalDateFilter payByDate;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolClassId;

    private LongFilter schoolLedgerHeadId;

    public ClassFeeCriteria() {}

    public ClassFeeCriteria(ClassFeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.feeYear = other.feeYear == null ? null : other.feeYear.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.janFee = other.janFee == null ? null : other.janFee.copy();
        this.febFee = other.febFee == null ? null : other.febFee.copy();
        this.marFee = other.marFee == null ? null : other.marFee.copy();
        this.aprFee = other.aprFee == null ? null : other.aprFee.copy();
        this.mayFee = other.mayFee == null ? null : other.mayFee.copy();
        this.junFee = other.junFee == null ? null : other.junFee.copy();
        this.julFee = other.julFee == null ? null : other.julFee.copy();
        this.augFee = other.augFee == null ? null : other.augFee.copy();
        this.sepFee = other.sepFee == null ? null : other.sepFee.copy();
        this.octFee = other.octFee == null ? null : other.octFee.copy();
        this.novFee = other.novFee == null ? null : other.novFee.copy();
        this.decFee = other.decFee == null ? null : other.decFee.copy();
        this.payByDate = other.payByDate == null ? null : other.payByDate.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolClassId = other.schoolClassId == null ? null : other.schoolClassId.copy();
        this.schoolLedgerHeadId = other.schoolLedgerHeadId == null ? null : other.schoolLedgerHeadId.copy();
    }

    @Override
    public ClassFeeCriteria copy() {
        return new ClassFeeCriteria(this);
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

    public DoubleFilter getJanFee() {
        return janFee;
    }

    public DoubleFilter janFee() {
        if (janFee == null) {
            janFee = new DoubleFilter();
        }
        return janFee;
    }

    public void setJanFee(DoubleFilter janFee) {
        this.janFee = janFee;
    }

    public DoubleFilter getFebFee() {
        return febFee;
    }

    public DoubleFilter febFee() {
        if (febFee == null) {
            febFee = new DoubleFilter();
        }
        return febFee;
    }

    public void setFebFee(DoubleFilter febFee) {
        this.febFee = febFee;
    }

    public DoubleFilter getMarFee() {
        return marFee;
    }

    public DoubleFilter marFee() {
        if (marFee == null) {
            marFee = new DoubleFilter();
        }
        return marFee;
    }

    public void setMarFee(DoubleFilter marFee) {
        this.marFee = marFee;
    }

    public DoubleFilter getAprFee() {
        return aprFee;
    }

    public DoubleFilter aprFee() {
        if (aprFee == null) {
            aprFee = new DoubleFilter();
        }
        return aprFee;
    }

    public void setAprFee(DoubleFilter aprFee) {
        this.aprFee = aprFee;
    }

    public DoubleFilter getMayFee() {
        return mayFee;
    }

    public DoubleFilter mayFee() {
        if (mayFee == null) {
            mayFee = new DoubleFilter();
        }
        return mayFee;
    }

    public void setMayFee(DoubleFilter mayFee) {
        this.mayFee = mayFee;
    }

    public DoubleFilter getJunFee() {
        return junFee;
    }

    public DoubleFilter junFee() {
        if (junFee == null) {
            junFee = new DoubleFilter();
        }
        return junFee;
    }

    public void setJunFee(DoubleFilter junFee) {
        this.junFee = junFee;
    }

    public DoubleFilter getJulFee() {
        return julFee;
    }

    public DoubleFilter julFee() {
        if (julFee == null) {
            julFee = new DoubleFilter();
        }
        return julFee;
    }

    public void setJulFee(DoubleFilter julFee) {
        this.julFee = julFee;
    }

    public DoubleFilter getAugFee() {
        return augFee;
    }

    public DoubleFilter augFee() {
        if (augFee == null) {
            augFee = new DoubleFilter();
        }
        return augFee;
    }

    public void setAugFee(DoubleFilter augFee) {
        this.augFee = augFee;
    }

    public DoubleFilter getSepFee() {
        return sepFee;
    }

    public DoubleFilter sepFee() {
        if (sepFee == null) {
            sepFee = new DoubleFilter();
        }
        return sepFee;
    }

    public void setSepFee(DoubleFilter sepFee) {
        this.sepFee = sepFee;
    }

    public DoubleFilter getOctFee() {
        return octFee;
    }

    public DoubleFilter octFee() {
        if (octFee == null) {
            octFee = new DoubleFilter();
        }
        return octFee;
    }

    public void setOctFee(DoubleFilter octFee) {
        this.octFee = octFee;
    }

    public DoubleFilter getNovFee() {
        return novFee;
    }

    public DoubleFilter novFee() {
        if (novFee == null) {
            novFee = new DoubleFilter();
        }
        return novFee;
    }

    public void setNovFee(DoubleFilter novFee) {
        this.novFee = novFee;
    }

    public DoubleFilter getDecFee() {
        return decFee;
    }

    public DoubleFilter decFee() {
        if (decFee == null) {
            decFee = new DoubleFilter();
        }
        return decFee;
    }

    public void setDecFee(DoubleFilter decFee) {
        this.decFee = decFee;
    }

    public LocalDateFilter getPayByDate() {
        return payByDate;
    }

    public LocalDateFilter payByDate() {
        if (payByDate == null) {
            payByDate = new LocalDateFilter();
        }
        return payByDate;
    }

    public void setPayByDate(LocalDateFilter payByDate) {
        this.payByDate = payByDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassFeeCriteria that = (ClassFeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(feeYear, that.feeYear) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(janFee, that.janFee) &&
            Objects.equals(febFee, that.febFee) &&
            Objects.equals(marFee, that.marFee) &&
            Objects.equals(aprFee, that.aprFee) &&
            Objects.equals(mayFee, that.mayFee) &&
            Objects.equals(junFee, that.junFee) &&
            Objects.equals(julFee, that.julFee) &&
            Objects.equals(augFee, that.augFee) &&
            Objects.equals(sepFee, that.sepFee) &&
            Objects.equals(octFee, that.octFee) &&
            Objects.equals(novFee, that.novFee) &&
            Objects.equals(decFee, that.decFee) &&
            Objects.equals(payByDate, that.payByDate) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(schoolClassId, that.schoolClassId) &&
            Objects.equals(schoolLedgerHeadId, that.schoolLedgerHeadId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            feeYear,
            dueDate,
            janFee,
            febFee,
            marFee,
            aprFee,
            mayFee,
            junFee,
            julFee,
            augFee,
            sepFee,
            octFee,
            novFee,
            decFee,
            payByDate,
            createDate,
            lastModified,
            cancelDate,
            schoolClassId,
            schoolLedgerHeadId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassFeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (feeYear != null ? "feeYear=" + feeYear + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (janFee != null ? "janFee=" + janFee + ", " : "") +
            (febFee != null ? "febFee=" + febFee + ", " : "") +
            (marFee != null ? "marFee=" + marFee + ", " : "") +
            (aprFee != null ? "aprFee=" + aprFee + ", " : "") +
            (mayFee != null ? "mayFee=" + mayFee + ", " : "") +
            (junFee != null ? "junFee=" + junFee + ", " : "") +
            (julFee != null ? "julFee=" + julFee + ", " : "") +
            (augFee != null ? "augFee=" + augFee + ", " : "") +
            (sepFee != null ? "sepFee=" + sepFee + ", " : "") +
            (octFee != null ? "octFee=" + octFee + ", " : "") +
            (novFee != null ? "novFee=" + novFee + ", " : "") +
            (decFee != null ? "decFee=" + decFee + ", " : "") +
            (payByDate != null ? "payByDate=" + payByDate + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolClassId != null ? "schoolClassId=" + schoolClassId + ", " : "") +
            (schoolLedgerHeadId != null ? "schoolLedgerHeadId=" + schoolLedgerHeadId + ", " : "") +
            "}";
    }
}
