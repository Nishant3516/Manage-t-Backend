package com.ssik.manageit.service.criteria;

import com.ssik.manageit.domain.enumeration.ModeOfPayment;
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
 * Criteria class for the {@link com.ssik.manageit.domain.StudentPayments} entity. This class is used
 * in {@link com.ssik.manageit.web.rest.StudentPaymentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentPaymentsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ModeOfPayment
     */
    public static class ModeOfPaymentFilter extends Filter<ModeOfPayment> {

        public ModeOfPaymentFilter() {}

        public ModeOfPaymentFilter(ModeOfPaymentFilter filter) {
            super(filter);
        }

        @Override
        public ModeOfPaymentFilter copy() {
            return new ModeOfPaymentFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amountPaid;

    private ModeOfPaymentFilter modeOfPay;

    private StringFilter noteNumbers;

    private StringFilter upiId;

    private StringFilter remarks;

    private LocalDateFilter paymentDate;

    private StringFilter receiptId;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModified;

    private LocalDateFilter cancelDate;

    private LongFilter classStudentId;

    public StudentPaymentsCriteria() {}

    public StudentPaymentsCriteria(StudentPaymentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amountPaid = other.amountPaid == null ? null : other.amountPaid.copy();
        this.modeOfPay = other.modeOfPay == null ? null : other.modeOfPay.copy();
        this.noteNumbers = other.noteNumbers == null ? null : other.noteNumbers.copy();
        this.upiId = other.upiId == null ? null : other.upiId.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.receiptId = other.receiptId == null ? null : other.receiptId.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.cancelDate = other.cancelDate == null ? null : other.cancelDate.copy();
        this.classStudentId = other.classStudentId == null ? null : other.classStudentId.copy();
    }

    @Override
    public StudentPaymentsCriteria copy() {
        return new StudentPaymentsCriteria(this);
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

    public DoubleFilter getAmountPaid() {
        return amountPaid;
    }

    public DoubleFilter amountPaid() {
        if (amountPaid == null) {
            amountPaid = new DoubleFilter();
        }
        return amountPaid;
    }

    public void setAmountPaid(DoubleFilter amountPaid) {
        this.amountPaid = amountPaid;
    }

    public ModeOfPaymentFilter getModeOfPay() {
        return modeOfPay;
    }

    public ModeOfPaymentFilter modeOfPay() {
        if (modeOfPay == null) {
            modeOfPay = new ModeOfPaymentFilter();
        }
        return modeOfPay;
    }

    public void setModeOfPay(ModeOfPaymentFilter modeOfPay) {
        this.modeOfPay = modeOfPay;
    }

    public StringFilter getNoteNumbers() {
        return noteNumbers;
    }

    public StringFilter noteNumbers() {
        if (noteNumbers == null) {
            noteNumbers = new StringFilter();
        }
        return noteNumbers;
    }

    public void setNoteNumbers(StringFilter noteNumbers) {
        this.noteNumbers = noteNumbers;
    }

    public StringFilter getUpiId() {
        return upiId;
    }

    public StringFilter upiId() {
        if (upiId == null) {
            upiId = new StringFilter();
        }
        return upiId;
    }

    public void setUpiId(StringFilter upiId) {
        this.upiId = upiId;
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

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public LocalDateFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new LocalDateFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public StringFilter getReceiptId() {
        return receiptId;
    }

    public StringFilter receiptId() {
        if (receiptId == null) {
            receiptId = new StringFilter();
        }
        return receiptId;
    }

    public void setReceiptId(StringFilter receiptId) {
        this.receiptId = receiptId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentPaymentsCriteria that = (StudentPaymentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amountPaid, that.amountPaid) &&
            Objects.equals(modeOfPay, that.modeOfPay) &&
            Objects.equals(noteNumbers, that.noteNumbers) &&
            Objects.equals(upiId, that.upiId) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(receiptId, that.receiptId) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(cancelDate, that.cancelDate) &&
            Objects.equals(classStudentId, that.classStudentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            amountPaid,
            modeOfPay,
            noteNumbers,
            upiId,
            remarks,
            paymentDate,
            receiptId,
            createDate,
            lastModified,
            cancelDate,
            classStudentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentPaymentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amountPaid != null ? "amountPaid=" + amountPaid + ", " : "") +
            (modeOfPay != null ? "modeOfPay=" + modeOfPay + ", " : "") +
            (noteNumbers != null ? "noteNumbers=" + noteNumbers + ", " : "") +
            (upiId != null ? "upiId=" + upiId + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (receiptId != null ? "receiptId=" + receiptId + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (cancelDate != null ? "cancelDate=" + cancelDate + ", " : "") +
            (classStudentId != null ? "classStudentId=" + classStudentId + ", " : "") +
            "}";
    }
}
