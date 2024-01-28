package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.ssik.manageit.domain.Tenant;
import com.ssik.manageit.domain.enumeration.SchoolLedgerHeadType;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolLedgerHead} entity.
 */
public class SchoolLedgerHeadDTO implements Serializable {

	private Long id;

	@NotNull
	private SchoolLedgerHeadType studentLedgerHeadType;

	@NotNull
	private String ledgerHeadName;

	private String ledgerHeadLongName;

	private LocalDate createDate;

	private LocalDate lastModified;

	private LocalDate cancelDate;

	private SchoolDTO school;

	private Tenant tenant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SchoolLedgerHeadType getStudentLedgerHeadType() {
		return studentLedgerHeadType;
	}

	public void setStudentLedgerHeadType(SchoolLedgerHeadType studentLedgerHeadType) {
		this.studentLedgerHeadType = studentLedgerHeadType;
	}

	public String getLedgerHeadName() {
		return ledgerHeadName;
	}

	public void setLedgerHeadName(String ledgerHeadName) {
		this.ledgerHeadName = ledgerHeadName;
	}

	public String getLedgerHeadLongName() {
		return ledgerHeadLongName;
	}

	public void setLedgerHeadLongName(String ledgerHeadLongName) {
		this.ledgerHeadLongName = ledgerHeadLongName;
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

	public SchoolDTO getSchool() {
		return school;
	}

	public void setSchool(SchoolDTO school) {
		this.school = school;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SchoolLedgerHeadDTO)) {
			return false;
		}

		SchoolLedgerHeadDTO schoolLedgerHeadDTO = (SchoolLedgerHeadDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, schoolLedgerHeadDTO.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "SchoolLedgerHeadDTO{" + "id=" + getId() + ", studentLedgerHeadType='" + getStudentLedgerHeadType() + "'"
				+ ", ledgerHeadName='" + getLedgerHeadName() + "'" + ", ledgerHeadLongName='" + getLedgerHeadLongName()
				+ "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + ", school=" + getSchool() + ", tenant=" + getTenant()
				+ "}";
	}
}
