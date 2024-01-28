package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ssik.manageit.domain.StudentChargesSummary} entity.
 */
public class StudentChargesSummaryDTO implements Serializable {

	private Long id;

	private String summaryType;

	private String feeYear;

	private Integer dueDate;

	private String aprSummary = "0.0";

	private String maySummary = "0.0";

	private String junSummary = "0.0";

	private String julSummary = "0.0";

	private String augSummary = "0.0";

	private String sepSummary = "0.0";

	private String octSummary = "0.0";

	private String novSummary = "0.0";

	private String decSummary = "0.0";

	private String janSummary = "0.0";

	private String febSummary = "0.0";

	private String marSummary = "0.0";

	private String additionalInfo1;

	private String additionalInfo2;

	private LocalDate createDate;

	private LocalDate lastModified;

	private LocalDate cancelDate;

	private SchoolLedgerHeadDTO schoolLedgerHead;

	private ClassStudentDTO classStudent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getFeeYear() {
		return feeYear;
	}

	public void setFeeYear(String feeYear) {
		this.feeYear = feeYear;
	}

	public Integer getDueDate() {
		return dueDate;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public String getAprSummary() {
		return aprSummary;
	}

	public void setAprSummary(String aprSummary) {
		this.aprSummary = aprSummary;
	}

	public String getMaySummary() {
		return maySummary;
	}

	public void setMaySummary(String maySummary) {
		this.maySummary = maySummary;
	}

	public String getJunSummary() {
		return junSummary;
	}

	public void setJunSummary(String junSummary) {
		this.junSummary = junSummary;
	}

	public String getJulSummary() {
		return julSummary;
	}

	public void setJulSummary(String julSummary) {
		this.julSummary = julSummary;
	}

	public String getAugSummary() {
		return augSummary;
	}

	public void setAugSummary(String augSummary) {
		this.augSummary = augSummary;
	}

	public String getSepSummary() {
		return sepSummary;
	}

	public void setSepSummary(String sepSummary) {
		this.sepSummary = sepSummary;
	}

	public String getOctSummary() {
		return octSummary;
	}

	public void setOctSummary(String octSummary) {
		this.octSummary = octSummary;
	}

	public String getNovSummary() {
		return novSummary;
	}

	public void setNovSummary(String novSummary) {
		this.novSummary = novSummary;
	}

	public String getDecSummary() {
		return decSummary;
	}

	public void setDecSummary(String decSummary) {
		this.decSummary = decSummary;
	}

	public String getJanSummary() {
		return janSummary;
	}

	public void setJanSummary(String janSummary) {
		this.janSummary = janSummary;
	}

	public String getFebSummary() {
		return febSummary;
	}

	public void setFebSummary(String febSummary) {
		this.febSummary = febSummary;
	}

	public String getMarSummary() {
		return marSummary;
	}

	public void setMarSummary(String marSummary) {
		this.marSummary = marSummary;
	}

	public String getAdditionalInfo1() {
		return additionalInfo1;
	}

	public void setAdditionalInfo1(String additionalInfo1) {
		this.additionalInfo1 = additionalInfo1;
	}

	public String getAdditionalInfo2() {
		return additionalInfo2;
	}

	public void setAdditionalInfo2(String additionalInfo2) {
		this.additionalInfo2 = additionalInfo2;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StudentChargesSummaryDTO)) {
			return false;
		}

		StudentChargesSummaryDTO studentChargesSummaryDTO = (StudentChargesSummaryDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, studentChargesSummaryDTO.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "StudentChargesSummaryDTO{" + "id=" + getId() + ", summaryType='" + getSummaryType() + "'"
				+ ", feeYear='" + getFeeYear() + "'" + ", dueDate=" + getDueDate() + ", aprSummary='" + getAprSummary()
				+ "'" + ", maySummary='" + getMaySummary() + "'" + ", junSummary='" + getJunSummary() + "'"
				+ ", julSummary='" + getJulSummary() + "'" + ", augSummary='" + getAugSummary() + "'" + ", sepSummary='"
				+ getSepSummary() + "'" + ", octSummary='" + getOctSummary() + "'" + ", novSummary='" + getNovSummary()
				+ "'" + ", decSummary='" + getDecSummary() + "'" + ", janSummary='" + getJanSummary() + "'"
				+ ", febSummary='" + getFebSummary() + "'" + ", marSummary='" + getMarSummary() + "'"
				+ ", additionalInfo1='" + getAdditionalInfo1() + "'" + ", additionalInfo2='" + getAdditionalInfo2()
				+ "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + ", schoolLedgerHead=" + getSchoolLedgerHead()
				+ ", classStudent=" + getClassStudent() + "}";
	}
}
