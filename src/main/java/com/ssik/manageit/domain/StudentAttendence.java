package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentAttendence.
 */
@Entity
@Table(name = "student_attendence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentAttendence implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "school_date", nullable = false)
	private LocalDate schoolDate;

	@NotNull
	@Column(name = "attendence", nullable = false)
	private Boolean attendence;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "studentDiscounts", "studentAdditionalCharges", "studentChargesSummaries",
			"studentPayments", "studentAttendences", "studentHomeWorkTracks", "studentClassWorkTracks",
			"schoolClass", }, allowSetters = true)
	private ClassStudent classStudent;
	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StudentAttendence id(Long id) {
		this.id = id;
		return this;
	}

	public LocalDate getSchoolDate() {
		return this.schoolDate;
	}

	public StudentAttendence schoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
		return this;
	}

	public void setSchoolDate(LocalDate schoolDate) {
		this.schoolDate = schoolDate;
	}

	public Boolean getAttendence() {
		return this.attendence;
	}

	public StudentAttendence attendence(Boolean attendence) {
		this.attendence = attendence;
		return this;
	}

	public void setAttendence(Boolean attendence) {
		this.attendence = attendence;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public StudentAttendence createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public StudentAttendence lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public StudentAttendence cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public ClassStudent getClassStudent() {
		return this.classStudent;
	}

	public StudentAttendence classStudent(ClassStudent classStudent) {
		this.setClassStudent(classStudent);
		return this;
	}

	public void setClassStudent(ClassStudent classStudent) {
		this.classStudent = classStudent;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StudentAttendence)) {
			return false;
		}
		return id != null && id.equals(((StudentAttendence) o).id);
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "StudentAttendence{" + "id=" + getId() + ", schoolDate='" + getSchoolDate() + "'" + ", attendence='"
				+ getAttendence() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='"
				+ getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
