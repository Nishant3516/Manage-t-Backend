package com.ssik.manageit.service.dto;

import com.ssik.manageit.domain.enumeration.FeeYear;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentDiscount} entity.
 */
public class StudentDiscountDTO implements Serializable {

	private Long id;

	@NotNull
	private FeeYear feeYear;

	private Integer dueDate;

	private Double janFeeDisc = 0.0;

	private Double febFeeDisc = 0.0;

	private Double marFeeDisc = 0.0;

	private Double aprFeeDisc = 0.0;

	private Double mayFeeDisc = 0.0;

	private Double junFeeDisc = 0.0;

	private Double julFeeDisc = 0.0;

	private Double augFeeDisc = 0.0;

	private Double sepFeeDisc = 0.0;

	private Double octFeeDisc = 0.0;

	private Double novFeeDisc = 0.0;

	private Double decFeeDisc = 0.0;

	private LocalDate createDate;

	private LocalDate lastModified;

	private LocalDate cancelDate;

	@NotNull
	private SchoolLedgerHeadDTO schoolLedgerHead;

	@NotNull
	private ClassStudentDTO classStudent;

	public StudentDiscountDTO() {
	}

	public StudentDiscountDTO(SchoolLedgerHeadDTO schoolLedgerHead, ClassStudentDTO classStudentDto, FeeYear feeYear,
			Integer dueDate, Double aprFeeDisc, Double mayFeeDisc, Double junFeeDisc, Double julFeeDisc,
			Double augFeeDisc, Double sepFeeDisc, Double octFeeDisc, Double novFeeDisc, Double decFeeDisc,
			Double janFeeDisc, Double febFeeDisc, Double marFeeDisc) {
		this.schoolLedgerHead = schoolLedgerHead;
		this.classStudent = classStudentDto;
		this.feeYear = feeYear;
		this.dueDate = dueDate;
		this.aprFeeDisc = aprFeeDisc;
		this.mayFeeDisc = mayFeeDisc;
		this.junFeeDisc = junFeeDisc;
		this.julFeeDisc = julFeeDisc;
		this.augFeeDisc = augFeeDisc;
		this.sepFeeDisc = sepFeeDisc;
		this.octFeeDisc = octFeeDisc;
		this.novFeeDisc = novFeeDisc;
		this.decFeeDisc = decFeeDisc;
		this.janFeeDisc = janFeeDisc;
		this.febFeeDisc = febFeeDisc;
		this.marFeeDisc = marFeeDisc;
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

	public Double getJanFeeDisc() {
		return janFeeDisc;
	}

	public void setJanFeeDisc(Double janFeeDisc) {
		this.janFeeDisc = janFeeDisc;
	}

	public Double getFebFeeDisc() {
		return febFeeDisc;
	}

	public void setFebFeeDisc(Double febFeeDisc) {
		this.febFeeDisc = febFeeDisc;
	}

	public Double getMarFeeDisc() {
		return marFeeDisc;
	}

	public void setMarFeeDisc(Double marFeeDisc) {
		this.marFeeDisc = marFeeDisc;
	}

	public Double getAprFeeDisc() {
		return aprFeeDisc;
	}

	public void setAprFeeDisc(Double aprFeeDisc) {
		this.aprFeeDisc = aprFeeDisc;
	}

	public Double getMayFeeDisc() {
		return mayFeeDisc;
	}

	public void setMayFeeDisc(Double mayFeeDisc) {
		this.mayFeeDisc = mayFeeDisc;
	}

	public Double getJunFeeDisc() {
		return junFeeDisc;
	}

	public void setJunFeeDisc(Double junFeeDisc) {
		this.junFeeDisc = junFeeDisc;
	}

	public Double getJulFeeDisc() {
		return julFeeDisc;
	}

	public void setJulFeeDisc(Double julFeeDisc) {
		this.julFeeDisc = julFeeDisc;
	}

	public Double getAugFeeDisc() {
		return augFeeDisc;
	}

	public void setAugFeeDisc(Double augFeeDisc) {
		this.augFeeDisc = augFeeDisc;
	}

	public Double getSepFeeDisc() {
		return sepFeeDisc;
	}

	public void setSepFeeDisc(Double sepFeeDisc) {
		this.sepFeeDisc = sepFeeDisc;
	}

	public Double getOctFeeDisc() {
		return octFeeDisc;
	}

	public void setOctFeeDisc(Double octFeeDisc) {
		this.octFeeDisc = octFeeDisc;
	}

	public Double getNovFeeDisc() {
		return novFeeDisc;
	}

	public void setNovFeeDisc(Double novFeeDisc) {
		this.novFeeDisc = novFeeDisc;
	}

	public Double getDecFeeDisc() {
		return decFeeDisc;
	}

	public void setDecFeeDisc(Double decFeeDisc) {
		this.decFeeDisc = decFeeDisc;
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

	public StudentChargesSummaryDTO toStudentChargesSummaryDTO(String ledgerHeadName) {
		StudentChargesSummaryDTO studentChargesSummaryDTO = new StudentChargesSummaryDTO();

		studentChargesSummaryDTO.setSummaryType("Discount -" + ledgerHeadName);

		studentChargesSummaryDTO.setFeeYear(this.getFeeYear().name());

		studentChargesSummaryDTO.setDueDate(this.getDueDate());

		studentChargesSummaryDTO.setAprSummary("" + this.getAprFeeDisc());
		studentChargesSummaryDTO.setMaySummary("" + this.getMayFeeDisc());
		studentChargesSummaryDTO.setJunSummary("" + this.getJunFeeDisc());
		studentChargesSummaryDTO.setJulSummary("" + this.getJulFeeDisc());
		studentChargesSummaryDTO.setAugSummary("" + this.getAugFeeDisc());
		studentChargesSummaryDTO.setSepSummary("" + this.getSepFeeDisc());
		studentChargesSummaryDTO.setOctSummary("" + this.getOctFeeDisc());
		studentChargesSummaryDTO.setNovSummary("" + this.getNovFeeDisc());
		studentChargesSummaryDTO.setDecSummary("" + this.getDecFeeDisc());
		studentChargesSummaryDTO.setJanSummary("" + this.getJanFeeDisc());
		studentChargesSummaryDTO.setFebSummary("" + this.getFebFeeDisc());
		studentChargesSummaryDTO.setMarSummary("" + this.getMarFeeDisc());
		studentChargesSummaryDTO.setClassStudent(this.getClassStudent());
		return studentChargesSummaryDTO;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StudentDiscountDTO)) {
			return false;
		}

		StudentDiscountDTO studentDiscountDTO = (StudentDiscountDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, studentDiscountDTO.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "StudentDiscountDTO{" + "id=" + getId() + ", feeYear='" + getFeeYear() + "'" + ", dueDate="
				+ getDueDate() + ", janFeeDisc=" + getJanFeeDisc() + ", febFeeDisc=" + getFebFeeDisc() + ", marFeeDisc="
				+ getMarFeeDisc() + ", aprFeeDisc=" + getAprFeeDisc() + ", mayFeeDisc=" + getMayFeeDisc()
				+ ", junFeeDisc=" + getJunFeeDisc() + ", julFeeDisc=" + getJulFeeDisc() + ", augFeeDisc="
				+ getAugFeeDisc() + ", sepFeeDisc=" + getSepFeeDisc() + ", octFeeDisc=" + getOctFeeDisc()
				+ ", novFeeDisc=" + getNovFeeDisc() + ", decFeeDisc=" + getDecFeeDisc() + ", createDate='"
				+ getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='"
				+ getCancelDate() + "'" + ", schoolLedgerHead=" + getSchoolLedgerHead() + ", classStudent="
				+ getClassStudent() + "}";
	}
}
