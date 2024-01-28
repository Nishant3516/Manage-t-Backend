package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.FeeYear;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.ClassFee} entity.
 */
public class ClassFeeDTO implements Serializable {

    private Long id;

    @NotNull
    private FeeYear feeYear;

    @NotNull
    private Integer dueDate;

    private Double janFee = 0.0;

    private Double febFee = 0.0;

    private Double marFee = 0.0;

    private Double aprFee = 0.0;

    private Double mayFee = 0.0;

    private Double junFee = 0.0;

    private Double julFee = 0.0;

    private Double augFee = 0.0;

    private Double sepFee = 0.0;

    private Double octFee = 0.0;

    private Double novFee = 0.0;

    private Double decFee = 0.0;

    private LocalDate payByDate;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private Set<SchoolClassDTO> schoolClasses = new HashSet<>();

    private SchoolLedgerHeadDTO schoolLedgerHead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeeYear getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(FeeYear feeYear) {
        this.feeYear = feeYear;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Double getJanFee() {
        return janFee;
    }

    public void setJanFee(Double janFee) {
        this.janFee = janFee;
    }

    public Double getFebFee() {
        return febFee;
    }

    public void setFebFee(Double febFee) {
        this.febFee = febFee;
    }

    public Double getMarFee() {
        return marFee;
    }

    public void setMarFee(Double marFee) {
        this.marFee = marFee;
    }

    public Double getAprFee() {
        return aprFee;
    }

    public void setAprFee(Double aprFee) {
        this.aprFee = aprFee;
    }

    public Double getMayFee() {
        return mayFee;
    }

    public void setMayFee(Double mayFee) {
        this.mayFee = mayFee;
    }

    public Double getJunFee() {
        return junFee;
    }

    public void setJunFee(Double junFee) {
        this.junFee = junFee;
    }

    public Double getJulFee() {
        return julFee;
    }

    public void setJulFee(Double julFee) {
        this.julFee = julFee;
    }

    public Double getAugFee() {
        return augFee;
    }

    public void setAugFee(Double augFee) {
        this.augFee = augFee;
    }

    public Double getSepFee() {
        return sepFee;
    }

    public void setSepFee(Double sepFee) {
        this.sepFee = sepFee;
    }

    public Double getOctFee() {
        return octFee;
    }

    public void setOctFee(Double octFee) {
        this.octFee = octFee;
    }

    public Double getNovFee() {
        return novFee;
    }

    public void setNovFee(Double novFee) {
        this.novFee = novFee;
    }

    public Double getDecFee() {
        return decFee;
    }

    public void setDecFee(Double decFee) {
        this.decFee = decFee;
    }

    public LocalDate getPayByDate() {
        return payByDate;
    }

    public void setPayByDate(LocalDate payByDate) {
        this.payByDate = payByDate;
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

    public Set<SchoolClassDTO> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(Set<SchoolClassDTO> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public SchoolLedgerHeadDTO getSchoolLedgerHead() {
        return schoolLedgerHead;
    }

    public void setSchoolLedgerHead(SchoolLedgerHeadDTO schoolLedgerHead) {
        this.schoolLedgerHead = schoolLedgerHead;
    }

    public StudentChargesSummaryDTO toStudentChargesSummaryDTO(String ledgerHeadName) {
        StudentChargesSummaryDTO studentChargesSummaryDTO = new StudentChargesSummaryDTO();

        studentChargesSummaryDTO.setSummaryType("ClassFee -" + ledgerHeadName);

        studentChargesSummaryDTO.setFeeYear(this.getFeeYear().name());

        studentChargesSummaryDTO.setDueDate(this.getDueDate());

        studentChargesSummaryDTO.setAprSummary("" + this.getAprFee());
        studentChargesSummaryDTO.setMaySummary("" + this.getMayFee());
        studentChargesSummaryDTO.setJunSummary("" + this.getJunFee());
        studentChargesSummaryDTO.setJulSummary("" + this.getJulFee());
        studentChargesSummaryDTO.setAugSummary("" + this.getAugFee());
        studentChargesSummaryDTO.setSepSummary("" + this.getSepFee());
        studentChargesSummaryDTO.setOctSummary("" + this.getOctFee());
        studentChargesSummaryDTO.setNovSummary("" + this.getNovFee());
        studentChargesSummaryDTO.setDecSummary("" + this.getDecFee());
        studentChargesSummaryDTO.setJanSummary("" + this.getJanFee());
        studentChargesSummaryDTO.setFebSummary("" + this.getFebFee());
        studentChargesSummaryDTO.setMarSummary("" + this.getMarFee());
        return studentChargesSummaryDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassFeeDTO)) {
            return false;
        }

        ClassFeeDTO classFeeDTO = (ClassFeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classFeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassFeeDTO{" +
            "id=" + getId() +
            ", feeYear='" + getFeeYear() + "'" +
            ", dueDate=" + getDueDate() +
            ", janFee=" + getJanFee() +
            ", febFee=" + getFebFee() +
            ", marFee=" + getMarFee() +
            ", aprFee=" + getAprFee() +
            ", mayFee=" + getMayFee() +
            ", junFee=" + getJunFee() +
            ", julFee=" + getJulFee() +
            ", augFee=" + getAugFee() +
            ", sepFee=" + getSepFee() +
            ", octFee=" + getOctFee() +
            ", novFee=" + getNovFee() +
            ", decFee=" + getDecFee() +
            ", payByDate='" + getPayByDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            ", schoolLedgerHead=" + getSchoolLedgerHead() +
            "}";
    }
}
