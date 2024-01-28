package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import liquibase.util.StringUtil;
import org.springframework.util.StringUtils;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentPayments} entity.
 */
public class StudentPaymentsDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amountPaid;

    private ModeOfPayment modeOfPay;

    private String noteNumbers;

    private String upiId;

    private String remarks;

    private LocalDate paymentDate;

    private String receiptId;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private ClassStudentDTO classStudent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public ModeOfPayment getModeOfPay() {
        return modeOfPay;
    }

    public void setModeOfPay(ModeOfPayment modeOfPay) {
        this.modeOfPay = modeOfPay;
    }

    public String getNoteNumbers() {
        return noteNumbers;
    }

    public void setNoteNumbers(String noteNumbers) {
        this.noteNumbers = noteNumbers;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ClassStudentDTO getClassStudent() {
        return classStudent;
    }

    public void setClassStudent(ClassStudentDTO classStudent) {
        this.classStudent = classStudent;
    }

    public StudentChargesSummaryDTO toStudentChargesSummaryDTO(String summary) {
        StudentChargesSummaryDTO studentChargesSummaryDTO = new StudentChargesSummaryDTO();

        studentChargesSummaryDTO.setSummaryType(summary);

        studentChargesSummaryDTO.setFeeYear("" + this.paymentDate.getYear());

        studentChargesSummaryDTO.setDueDate(this.paymentDate.getDayOfYear());

        if (this.getPaymentDate().getMonthValue() == 4) {
            studentChargesSummaryDTO.setAprSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 5) {
            studentChargesSummaryDTO.setMaySummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 6) {
            studentChargesSummaryDTO.setJunSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 7) {
            studentChargesSummaryDTO.setJulSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 8) {
            studentChargesSummaryDTO.setAugSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 9) {
            studentChargesSummaryDTO.setSepSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 10) {
            studentChargesSummaryDTO.setOctSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 11) {
            studentChargesSummaryDTO.setNovSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 12) {
            studentChargesSummaryDTO.setDecSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 1) {
            studentChargesSummaryDTO.setJanSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 2) {
            studentChargesSummaryDTO.setFebSummary("" + this.getAmountPaid());
        }
        if (this.getPaymentDate().getMonthValue() == 3) {
            studentChargesSummaryDTO.setMarSummary("" + this.getAmountPaid());
        }

        studentChargesSummaryDTO.setClassStudent(this.getClassStudent());
        studentChargesSummaryDTO.setAdditionalInfo1("Receipt ID : " + this.getReceiptId());
        studentChargesSummaryDTO.setAdditionalInfo2(
            "ModeOfPay : " +
            (this.getModeOfPay() != null ? this.getModeOfPay().name() : ModeOfPayment.CASH.name()) +
            (StringUtils.hasText(this.noteNumbers) ? " Note Numbers : " + this.noteNumbers : "")
        );
        studentChargesSummaryDTO.setCreateDate(this.getPaymentDate());
        return studentChargesSummaryDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentPaymentsDTO)) {
            return false;
        }

        StudentPaymentsDTO studentPaymentsDTO = (StudentPaymentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentPaymentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentPaymentsDTO{" +
            "id=" + getId() +
            ", amountPaid=" + getAmountPaid() +
            ", modeOfPay='" + getModeOfPay() + "'" +
            ", noteNumbers='" + getNoteNumbers() + "'" +
            ", upiId='" + getUpiId() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", receiptId='" + getReceiptId() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", classStudent=" + getClassStudent() +
            "}";
    }
}
