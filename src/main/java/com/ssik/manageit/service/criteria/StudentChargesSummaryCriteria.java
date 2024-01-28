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
 * Criteria class for the {@link com.ssik.manageit.domain.StudentChargesSummary} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentChargesSummaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-charges-summaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentChargesSummaryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter summaryType;

    private StringFilter feeYear;

    private IntegerFilter dueDate;

    private StringFilter aprSummary;

    private StringFilter maySummary;

    private StringFilter junSummary;

    private StringFilter julSummary;

    private StringFilter augSummary;

    private StringFilter sepSummary;

    private StringFilter octSummary;

    private StringFilter novSummary;

    private StringFilter decSummary;

    private StringFilter janSummary;

    private StringFilter febSummary;

    private StringFilter marSummary;

    private StringFilter additionalInfo1;

    private StringFilter additionalInfo2;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter schoolLedgerHeadId;

    private LongFilter classStudentId;

    public StudentChargesSummaryCriteria() {}

    public StudentChargesSummaryCriteria(StudentChargesSummaryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.summaryType = other.summaryType == null ? null : other.summaryType.copy();
        this.feeYear = other.feeYear == null ? null : other.feeYear.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.aprSummary = other.aprSummary == null ? null : other.aprSummary.copy();
        this.maySummary = other.maySummary == null ? null : other.maySummary.copy();
        this.junSummary = other.junSummary == null ? null : other.junSummary.copy();
        this.julSummary = other.julSummary == null ? null : other.julSummary.copy();
        this.augSummary = other.augSummary == null ? null : other.augSummary.copy();
        this.sepSummary = other.sepSummary == null ? null : other.sepSummary.copy();
        this.octSummary = other.octSummary == null ? null : other.octSummary.copy();
        this.novSummary = other.novSummary == null ? null : other.novSummary.copy();
        this.decSummary = other.decSummary == null ? null : other.decSummary.copy();
        this.janSummary = other.janSummary == null ? null : other.janSummary.copy();
        this.febSummary = other.febSummary == null ? null : other.febSummary.copy();
        this.marSummary = other.marSummary == null ? null : other.marSummary.copy();
        this.additionalInfo1 = other.additionalInfo1 == null ? null : other.additionalInfo1.copy();
        this.additionalInfo2 = other.additionalInfo2 == null ? null : other.additionalInfo2.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.schoolLedgerHeadId = other.schoolLedgerHeadId == null ? null : other.schoolLedgerHeadId.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
    }

    @Override
    public StudentChargesSummaryCriteria copy() {
        return new StudentChargesSummaryCriteria(this);
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

    public StringFilter getSummaryType() {
        return summaryType;
    }

    public StringFilter summaryType() {
        if (summaryType == null) {
            summaryType = new StringFilter();
        }
        return summaryType;
    }

    public void setSummaryType(StringFilter summaryType) {
        this.summaryType = summaryType;
    }

    public StringFilter getFeeYear() {
        return feeYear;
    }

    public StringFilter feeYear() {
        if (feeYear == null) {
            feeYear = new StringFilter();
        }
        return feeYear;
    }

    public void setFeeYear(StringFilter feeYear) {
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

    public StringFilter getAprSummary() {
        return aprSummary;
    }

    public StringFilter aprSummary() {
        if (aprSummary == null) {
            aprSummary = new StringFilter();
        }
        return aprSummary;
    }

    public void setAprSummary(StringFilter aprSummary) {
        this.aprSummary = aprSummary;
    }

    public StringFilter getMaySummary() {
        return maySummary;
    }

    public StringFilter maySummary() {
        if (maySummary == null) {
            maySummary = new StringFilter();
        }
        return maySummary;
    }

    public void setMaySummary(StringFilter maySummary) {
        this.maySummary = maySummary;
    }

    public StringFilter getJunSummary() {
        return junSummary;
    }

    public StringFilter junSummary() {
        if (junSummary == null) {
            junSummary = new StringFilter();
        }
        return junSummary;
    }

    public void setJunSummary(StringFilter junSummary) {
        this.junSummary = junSummary;
    }

    public StringFilter getJulSummary() {
        return julSummary;
    }

    public StringFilter julSummary() {
        if (julSummary == null) {
            julSummary = new StringFilter();
        }
        return julSummary;
    }

    public void setJulSummary(StringFilter julSummary) {
        this.julSummary = julSummary;
    }

    public StringFilter getAugSummary() {
        return augSummary;
    }

    public StringFilter augSummary() {
        if (augSummary == null) {
            augSummary = new StringFilter();
        }
        return augSummary;
    }

    public void setAugSummary(StringFilter augSummary) {
        this.augSummary = augSummary;
    }

    public StringFilter getSepSummary() {
        return sepSummary;
    }

    public StringFilter sepSummary() {
        if (sepSummary == null) {
            sepSummary = new StringFilter();
        }
        return sepSummary;
    }

    public void setSepSummary(StringFilter sepSummary) {
        this.sepSummary = sepSummary;
    }

    public StringFilter getOctSummary() {
        return octSummary;
    }

    public StringFilter octSummary() {
        if (octSummary == null) {
            octSummary = new StringFilter();
        }
        return octSummary;
    }

    public void setOctSummary(StringFilter octSummary) {
        this.octSummary = octSummary;
    }

    public StringFilter getNovSummary() {
        return novSummary;
    }

    public StringFilter novSummary() {
        if (novSummary == null) {
            novSummary = new StringFilter();
        }
        return novSummary;
    }

    public void setNovSummary(StringFilter novSummary) {
        this.novSummary = novSummary;
    }

    public StringFilter getDecSummary() {
        return decSummary;
    }

    public StringFilter decSummary() {
        if (decSummary == null) {
            decSummary = new StringFilter();
        }
        return decSummary;
    }

    public void setDecSummary(StringFilter decSummary) {
        this.decSummary = decSummary;
    }

    public StringFilter getJanSummary() {
        return janSummary;
    }

    public StringFilter janSummary() {
        if (janSummary == null) {
            janSummary = new StringFilter();
        }
        return janSummary;
    }

    public void setJanSummary(StringFilter janSummary) {
        this.janSummary = janSummary;
    }

    public StringFilter getFebSummary() {
        return febSummary;
    }

    public StringFilter febSummary() {
        if (febSummary == null) {
            febSummary = new StringFilter();
        }
        return febSummary;
    }

    public void setFebSummary(StringFilter febSummary) {
        this.febSummary = febSummary;
    }

    public StringFilter getMarSummary() {
        return marSummary;
    }

    public StringFilter marSummary() {
        if (marSummary == null) {
            marSummary = new StringFilter();
        }
        return marSummary;
    }

    public void setMarSummary(StringFilter marSummary) {
        this.marSummary = marSummary;
    }

    public StringFilter getAdditionalInfo1() {
        return additionalInfo1;
    }

    public StringFilter additionalInfo1() {
        if (additionalInfo1 == null) {
            additionalInfo1 = new StringFilter();
        }
        return additionalInfo1;
    }

    public void setAdditionalInfo1(StringFilter additionalInfo1) {
        this.additionalInfo1 = additionalInfo1;
    }

    public StringFilter getAdditionalInfo2() {
        return additionalInfo2;
    }

    public StringFilter additionalInfo2() {
        if (additionalInfo2 == null) {
            additionalInfo2 = new StringFilter();
        }
        return additionalInfo2;
    }

    public void setAdditionalInfo2(StringFilter additionalInfo2) {
        this.additionalInfo2 = additionalInfo2;
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
        final StudentChargesSummaryCriteria that = (StudentChargesSummaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(summaryType, that.summaryType) &&
            Objects.equals(feeYear, that.feeYear) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(aprSummary, that.aprSummary) &&
            Objects.equals(maySummary, that.maySummary) &&
            Objects.equals(junSummary, that.junSummary) &&
            Objects.equals(julSummary, that.julSummary) &&
            Objects.equals(augSummary, that.augSummary) &&
            Objects.equals(sepSummary, that.sepSummary) &&
            Objects.equals(octSummary, that.octSummary) &&
            Objects.equals(novSummary, that.novSummary) &&
            Objects.equals(decSummary, that.decSummary) &&
            Objects.equals(janSummary, that.janSummary) &&
            Objects.equals(febSummary, that.febSummary) &&
            Objects.equals(marSummary, that.marSummary) &&
            Objects.equals(additionalInfo1, that.additionalInfo1) &&
            Objects.equals(additionalInfo2, that.additionalInfo2) &&
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
            summaryType,
            feeYear,
            dueDate,
            aprSummary,
            maySummary,
            junSummary,
            julSummary,
            augSummary,
            sepSummary,
            octSummary,
            novSummary,
            decSummary,
            janSummary,
            febSummary,
            marSummary,
            additionalInfo1,
            additionalInfo2,
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
        return "StudentChargesSummaryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (summaryType != null ? "summaryType=" + summaryType + ", " : "") +
            (feeYear != null ? "feeYear=" + feeYear + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (aprSummary != null ? "aprSummary=" + aprSummary + ", " : "") +
            (maySummary != null ? "maySummary=" + maySummary + ", " : "") +
            (junSummary != null ? "junSummary=" + junSummary + ", " : "") +
            (julSummary != null ? "julSummary=" + julSummary + ", " : "") +
            (augSummary != null ? "augSummary=" + augSummary + ", " : "") +
            (sepSummary != null ? "sepSummary=" + sepSummary + ", " : "") +
            (octSummary != null ? "octSummary=" + octSummary + ", " : "") +
            (novSummary != null ? "novSummary=" + novSummary + ", " : "") +
            (decSummary != null ? "decSummary=" + decSummary + ", " : "") +
            (janSummary != null ? "janSummary=" + janSummary + ", " : "") +
            (febSummary != null ? "febSummary=" + febSummary + ", " : "") +
            (marSummary != null ? "marSummary=" + marSummary + ", " : "") +
            (additionalInfo1 != null ? "additionalInfo1=" + additionalInfo1 + ", " : "") +
            (additionalInfo2 != null ? "additionalInfo2=" + additionalInfo2 + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (schoolLedgerHeadId != null ? "schoolLedgerHeadId=" + schoolLedgerHeadId + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            "}";
    }
}
