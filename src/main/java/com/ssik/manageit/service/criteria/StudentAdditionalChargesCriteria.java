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
 * Criteria class for the {@link com.ssik.manageit.domain.StudentAdditionalCharges} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentAdditionalChargesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-additional-charges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentAdditionalChargesCriteria implements Serializable, Criteria {

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

    private DoubleFilter janAddChrg;

    private DoubleFilter febAddChrgc;

    private DoubleFilter marAddChrg;

    private DoubleFilter aprAddChrg;

    private DoubleFilter mayAddChrg;

    private DoubleFilter junAddChrg;

    private DoubleFilter julAddChrg;

    private DoubleFilter augAddChrg;

    private DoubleFilter sepAddChrg;

    private DoubleFilter octAddChrg;

    private DoubleFilter novAddChrg;

    private DoubleFilter decAddChrg;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolLedgerHeadId;

    private LongFilter classStudentId;

    public StudentAdditionalChargesCriteria() {}

    public StudentAdditionalChargesCriteria(StudentAdditionalChargesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.feeYear = other.feeYear == null ? null : other.feeYear.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.janAddChrg = other.janAddChrg == null ? null : other.janAddChrg.copy();
        this.febAddChrgc = other.febAddChrgc == null ? null : other.febAddChrgc.copy();
        this.marAddChrg = other.marAddChrg == null ? null : other.marAddChrg.copy();
        this.aprAddChrg = other.aprAddChrg == null ? null : other.aprAddChrg.copy();
        this.mayAddChrg = other.mayAddChrg == null ? null : other.mayAddChrg.copy();
        this.junAddChrg = other.junAddChrg == null ? null : other.junAddChrg.copy();
        this.julAddChrg = other.julAddChrg == null ? null : other.julAddChrg.copy();
        this.augAddChrg = other.augAddChrg == null ? null : other.augAddChrg.copy();
        this.sepAddChrg = other.sepAddChrg == null ? null : other.sepAddChrg.copy();
        this.octAddChrg = other.octAddChrg == null ? null : other.octAddChrg.copy();
        this.novAddChrg = other.novAddChrg == null ? null : other.novAddChrg.copy();
        this.decAddChrg = other.decAddChrg == null ? null : other.decAddChrg.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolLedgerHeadId = other.schoolLedgerHeadId == null ? null : other.schoolLedgerHeadId.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
    }

    @Override
    public StudentAdditionalChargesCriteria copy() {
        return new StudentAdditionalChargesCriteria(this);
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

    public DoubleFilter getJanAddChrg() {
        return janAddChrg;
    }

    public DoubleFilter janAddChrg() {
        if (janAddChrg == null) {
            janAddChrg = new DoubleFilter();
        }
        return janAddChrg;
    }

    public void setJanAddChrg(DoubleFilter janAddChrg) {
        this.janAddChrg = janAddChrg;
    }

    public DoubleFilter getFebAddChrgc() {
        return febAddChrgc;
    }

    public DoubleFilter febAddChrgc() {
        if (febAddChrgc == null) {
            febAddChrgc = new DoubleFilter();
        }
        return febAddChrgc;
    }

    public void setFebAddChrgc(DoubleFilter febAddChrgc) {
        this.febAddChrgc = febAddChrgc;
    }

    public DoubleFilter getMarAddChrg() {
        return marAddChrg;
    }

    public DoubleFilter marAddChrg() {
        if (marAddChrg == null) {
            marAddChrg = new DoubleFilter();
        }
        return marAddChrg;
    }

    public void setMarAddChrg(DoubleFilter marAddChrg) {
        this.marAddChrg = marAddChrg;
    }

    public DoubleFilter getAprAddChrg() {
        return aprAddChrg;
    }

    public DoubleFilter aprAddChrg() {
        if (aprAddChrg == null) {
            aprAddChrg = new DoubleFilter();
        }
        return aprAddChrg;
    }

    public void setAprAddChrg(DoubleFilter aprAddChrg) {
        this.aprAddChrg = aprAddChrg;
    }

    public DoubleFilter getMayAddChrg() {
        return mayAddChrg;
    }

    public DoubleFilter mayAddChrg() {
        if (mayAddChrg == null) {
            mayAddChrg = new DoubleFilter();
        }
        return mayAddChrg;
    }

    public void setMayAddChrg(DoubleFilter mayAddChrg) {
        this.mayAddChrg = mayAddChrg;
    }

    public DoubleFilter getJunAddChrg() {
        return junAddChrg;
    }

    public DoubleFilter junAddChrg() {
        if (junAddChrg == null) {
            junAddChrg = new DoubleFilter();
        }
        return junAddChrg;
    }

    public void setJunAddChrg(DoubleFilter junAddChrg) {
        this.junAddChrg = junAddChrg;
    }

    public DoubleFilter getJulAddChrg() {
        return julAddChrg;
    }

    public DoubleFilter julAddChrg() {
        if (julAddChrg == null) {
            julAddChrg = new DoubleFilter();
        }
        return julAddChrg;
    }

    public void setJulAddChrg(DoubleFilter julAddChrg) {
        this.julAddChrg = julAddChrg;
    }

    public DoubleFilter getAugAddChrg() {
        return augAddChrg;
    }

    public DoubleFilter augAddChrg() {
        if (augAddChrg == null) {
            augAddChrg = new DoubleFilter();
        }
        return augAddChrg;
    }

    public void setAugAddChrg(DoubleFilter augAddChrg) {
        this.augAddChrg = augAddChrg;
    }

    public DoubleFilter getSepAddChrg() {
        return sepAddChrg;
    }

    public DoubleFilter sepAddChrg() {
        if (sepAddChrg == null) {
            sepAddChrg = new DoubleFilter();
        }
        return sepAddChrg;
    }

    public void setSepAddChrg(DoubleFilter sepAddChrg) {
        this.sepAddChrg = sepAddChrg;
    }

    public DoubleFilter getOctAddChrg() {
        return octAddChrg;
    }

    public DoubleFilter octAddChrg() {
        if (octAddChrg == null) {
            octAddChrg = new DoubleFilter();
        }
        return octAddChrg;
    }

    public void setOctAddChrg(DoubleFilter octAddChrg) {
        this.octAddChrg = octAddChrg;
    }

    public DoubleFilter getNovAddChrg() {
        return novAddChrg;
    }

    public DoubleFilter novAddChrg() {
        if (novAddChrg == null) {
            novAddChrg = new DoubleFilter();
        }
        return novAddChrg;
    }

    public void setNovAddChrg(DoubleFilter novAddChrg) {
        this.novAddChrg = novAddChrg;
    }

    public DoubleFilter getDecAddChrg() {
        return decAddChrg;
    }

    public DoubleFilter decAddChrg() {
        if (decAddChrg == null) {
            decAddChrg = new DoubleFilter();
        }
        return decAddChrg;
    }

    public void setDecAddChrg(DoubleFilter decAddChrg) {
        this.decAddChrg = decAddChrg;
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
        final StudentAdditionalChargesCriteria that = (StudentAdditionalChargesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(feeYear, that.feeYear) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(janAddChrg, that.janAddChrg) &&
            Objects.equals(febAddChrgc, that.febAddChrgc) &&
            Objects.equals(marAddChrg, that.marAddChrg) &&
            Objects.equals(aprAddChrg, that.aprAddChrg) &&
            Objects.equals(mayAddChrg, that.mayAddChrg) &&
            Objects.equals(junAddChrg, that.junAddChrg) &&
            Objects.equals(julAddChrg, that.julAddChrg) &&
            Objects.equals(augAddChrg, that.augAddChrg) &&
            Objects.equals(sepAddChrg, that.sepAddChrg) &&
            Objects.equals(octAddChrg, that.octAddChrg) &&
            Objects.equals(novAddChrg, that.novAddChrg) &&
            Objects.equals(decAddChrg, that.decAddChrg) &&
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
            janAddChrg,
            febAddChrgc,
            marAddChrg,
            aprAddChrg,
            mayAddChrg,
            junAddChrg,
            julAddChrg,
            augAddChrg,
            sepAddChrg,
            octAddChrg,
            novAddChrg,
            decAddChrg,
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
        return "StudentAdditionalChargesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (feeYear != null ? "feeYear=" + feeYear + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (janAddChrg != null ? "janAddChrg=" + janAddChrg + ", " : "") +
            (febAddChrgc != null ? "febAddChrgc=" + febAddChrgc + ", " : "") +
            (marAddChrg != null ? "marAddChrg=" + marAddChrg + ", " : "") +
            (aprAddChrg != null ? "aprAddChrg=" + aprAddChrg + ", " : "") +
            (mayAddChrg != null ? "mayAddChrg=" + mayAddChrg + ", " : "") +
            (junAddChrg != null ? "junAddChrg=" + junAddChrg + ", " : "") +
            (julAddChrg != null ? "julAddChrg=" + julAddChrg + ", " : "") +
            (augAddChrg != null ? "augAddChrg=" + augAddChrg + ", " : "") +
            (sepAddChrg != null ? "sepAddChrg=" + sepAddChrg + ", " : "") +
            (octAddChrg != null ? "octAddChrg=" + octAddChrg + ", " : "") +
            (novAddChrg != null ? "novAddChrg=" + novAddChrg + ", " : "") +
            (decAddChrg != null ? "decAddChrg=" + decAddChrg + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolLedgerHeadId != null ? "schoolLedgerHeadId=" + schoolLedgerHeadId + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            "}";
    }
}
