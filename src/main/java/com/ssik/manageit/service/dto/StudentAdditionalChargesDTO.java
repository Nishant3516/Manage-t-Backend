package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.FeeYear;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentAdditionalCharges}
 * entity.
 */
public class StudentAdditionalChargesDTO implements Serializable {

	private Long id;

	@NotNull
	private FeeYear feeYear;

	@NotNull
	private Integer dueDate;

	private Double janAddChrg = 0.0;

	private Double febAddChrgc = 0.0;

	private Double marAddChrg = 0.0;

	private Double aprAddChrg = 0.0;

	private Double mayAddChrg = 0.0;

	private Double junAddChrg = 0.0;

	private Double julAddChrg = 0.0;

	private Double augAddChrg = 0.0;

	private Double sepAddChrg = 0.0;

	private Double octAddChrg = 0.0;

	private Double novAddChrg = 0.0;

	private Double decAddChrg = 0.0;

	private LocalDate createDate;

	private LocalDate lastModified;

	private LocalDate cancelDate;

	private SchoolLedgerHeadDTO schoolLedgerHead;

	private ClassStudentDTO classStudent;

	public StudentAdditionalChargesDTO() {
	}

	public StudentAdditionalChargesDTO(SchoolLedgerHeadDTO schoolLedgerHead, ClassStudentDTO classStudentDto,
			FeeYear feeYear, Integer dueDate, Double aprAddChrg, Double mayAddChrg, Double junFeeDisc,
			Double julFeeDisc, Double augFeeDisc, Double sepFeeDisc, Double octFeeDisc, Double novFeeDisc,
			Double decFeeDisc, Double janFeeDisc, Double febFeeDisc, Double marFeeDisc) {
		this.schoolLedgerHead = schoolLedgerHead;
		this.classStudent = classStudentDto;
		this.feeYear = feeYear;
		this.dueDate = dueDate;
		this.aprAddChrg = aprAddChrg;
		this.mayAddChrg = mayAddChrg;
		this.junAddChrg = junFeeDisc;
		this.julAddChrg = julFeeDisc;
		this.augAddChrg = augFeeDisc;
		this.sepAddChrg = sepFeeDisc;
		this.octAddChrg = octFeeDisc;
		this.novAddChrg = novFeeDisc;
		this.decAddChrg = decFeeDisc;
		this.janAddChrg = janFeeDisc;
		this.febAddChrgc = febFeeDisc;
		this.marAddChrg = marFeeDisc;
		this.createDate = LocalDate.now();
	}

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

	public Double getJanAddChrg() {
		return janAddChrg;
	}

	public void setJanAddChrg(Double janAddChrg) {
		this.janAddChrg = janAddChrg;
	}

	public Double getFebAddChrgc() {
		return febAddChrgc;
	}

	public void setFebAddChrgc(Double febAddChrgc) {
		this.febAddChrgc = febAddChrgc;
	}

	public Double getMarAddChrg() {
		return marAddChrg;
	}

	public void setMarAddChrg(Double marAddChrg) {
		this.marAddChrg = marAddChrg;
	}

	public Double getAprAddChrg() {
		return aprAddChrg;
	}

	public void setAprAddChrg(Double aprAddChrg) {
		this.aprAddChrg = aprAddChrg;
	}

	public Double getMayAddChrg() {
		return mayAddChrg;
	}

	public void setMayAddChrg(Double mayAddChrg) {
		this.mayAddChrg = mayAddChrg;
	}

	public Double getJunAddChrg() {
		return junAddChrg;
	}

	public void setJunAddChrg(Double junAddChrg) {
		this.junAddChrg = junAddChrg;
	}

	public Double getJulAddChrg() {
		return julAddChrg;
	}

	public void setJulAddChrg(Double julAddChrg) {
		this.julAddChrg = julAddChrg;
	}

	public Double getAugAddChrg() {
		return augAddChrg;
	}

	public void setAugAddChrg(Double augAddChrg) {
		this.augAddChrg = augAddChrg;
	}

	public Double getSepAddChrg() {
		return sepAddChrg;
	}

	public void setSepAddChrg(Double sepAddChrg) {
		this.sepAddChrg = sepAddChrg;
	}

	public Double getOctAddChrg() {
		return octAddChrg;
	}

	public void setOctAddChrg(Double octAddChrg) {
		this.octAddChrg = octAddChrg;
	}

	public Double getNovAddChrg() {
		return novAddChrg;
	}

	public void setNovAddChrg(Double novAddChrg) {
		this.novAddChrg = novAddChrg;
	}

	public Double getDecAddChrg() {
		return decAddChrg;
	}

	public void setDecAddChrg(Double decAddChrg) {
		this.decAddChrg = decAddChrg;
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

	public SchoolLedgerHeadDTO getSchoolLedgerHead() {
		return schoolLedgerHead;
	}

	public void setSchoolLedgerHead(SchoolLedgerHeadDTO schoolLedgerHead) {
		this.schoolLedgerHead = schoolLedgerHead;
	}

	public ClassStudentDTO getClassStudent() {
		return classStudent;
	}

	public void setClassStudent(ClassStudentDTO classStudent) {
		this.classStudent = classStudent;
	}

	public StudentChargesSummaryDTO toStudentChargesSummaryDTO(String ledgerHeadNAme) {
		StudentChargesSummaryDTO studentChargesSummaryDTO = new StudentChargesSummaryDTO();
		studentChargesSummaryDTO.setSummaryType("AddCharge -" + ledgerHeadNAme);

		studentChargesSummaryDTO.setFeeYear(this.getFeeYear().name());

		studentChargesSummaryDTO.setDueDate(this.getDueDate());

		studentChargesSummaryDTO.setAprSummary("" + this.getAprAddChrg());
		studentChargesSummaryDTO.setMaySummary("" + this.getMayAddChrg());
		studentChargesSummaryDTO.setJunSummary("" + this.getJunAddChrg());
		studentChargesSummaryDTO.setJulSummary("" + this.getJulAddChrg());
		studentChargesSummaryDTO.setAugSummary("" + this.getAugAddChrg());
		studentChargesSummaryDTO.setSepSummary("" + this.getSepAddChrg());
		studentChargesSummaryDTO.setOctSummary("" + this.getOctAddChrg());
		studentChargesSummaryDTO.setNovSummary("" + this.getNovAddChrg());
		studentChargesSummaryDTO.setDecSummary("" + this.getDecAddChrg());
		studentChargesSummaryDTO.setJanSummary("" + this.getJanAddChrg());
		studentChargesSummaryDTO.setFebSummary("" + this.getFebAddChrgc());
		studentChargesSummaryDTO.setMarSummary("" + this.getMarAddChrg());
		studentChargesSummaryDTO.setClassStudent(this.getClassStudent());
		return studentChargesSummaryDTO;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StudentAdditionalChargesDTO)) {
			return false;
		}

		StudentAdditionalChargesDTO studentAdditionalChargesDTO = (StudentAdditionalChargesDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, studentAdditionalChargesDTO.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "StudentAdditionalChargesDTO{" + "id=" + getId() + ", feeYear='" + getFeeYear() + "'" + ", dueDate="
				+ getDueDate() + ", janAddChrg=" + getJanAddChrg() + ", febAddChrgc=" + getFebAddChrgc()
				+ ", marAddChrg=" + getMarAddChrg() + ", aprAddChrg=" + getAprAddChrg() + ", mayAddChrg="
				+ getMayAddChrg() + ", junAddChrg=" + getJunAddChrg() + ", julAddChrg=" + getJulAddChrg()
				+ ", augAddChrg=" + getAugAddChrg() + ", sepAddChrg=" + getSepAddChrg() + ", octAddChrg="
				+ getOctAddChrg() + ", novAddChrg=" + getNovAddChrg() + ", decAddChrg=" + getDecAddChrg()
				+ ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + ", schoolLedgerHead=" + getSchoolLedgerHead()
				+ ", classStudent=" + getClassStudent() + "}";
	}
}
