package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.SchoolLedgerHeadType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchoolLedgerHead.
 */
@Entity
@Table(name = "school_ledger_head")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolLedgerHead implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "student_ledger_head_type", nullable = false)
	private SchoolLedgerHeadType studentLedgerHeadType;

	@NotNull
	@Column(name = "ledger_head_name", nullable = false)
	private String ledgerHeadName;

	@Column(name = "ledger_head_long_name")
	private String ledgerHeadLongName;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@OneToMany(mappedBy = "schoolLedgerHead")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHead" }, allowSetters = true)
	private Set<ClassFee> classFees = new HashSet<>();

	@OneToMany(mappedBy = "schoolLedgerHead")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentDiscount> studentDiscounts = new HashSet<>();

	@OneToMany(mappedBy = "schoolLedgerHead")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentAdditionalCharges> studentAdditionalCharges = new HashSet<>();

	@OneToMany(mappedBy = "schoolLedgerHead")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "schoolLedgerHead", "classStudent" }, allowSetters = true)
	private Set<StudentChargesSummary> studentChargesSummaries = new HashSet<>();

	@ManyToOne
	@JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHeads", "idStores",
			"auditLogs" }, allowSetters = true)
	private School school;

	@ManyToOne
	private Tenant tenant;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SchoolLedgerHead id(Long id) {
		this.id = id;
		return this;
	}

	public SchoolLedgerHeadType getStudentLedgerHeadType() {
		return this.studentLedgerHeadType;
	}

	public SchoolLedgerHead studentLedgerHeadType(SchoolLedgerHeadType studentLedgerHeadType) {
		this.studentLedgerHeadType = studentLedgerHeadType;
		return this;
	}

	public void setStudentLedgerHeadType(SchoolLedgerHeadType studentLedgerHeadType) {
		this.studentLedgerHeadType = studentLedgerHeadType;
	}

	public String getLedgerHeadName() {
		return this.ledgerHeadName;
	}

	public SchoolLedgerHead ledgerHeadName(String ledgerHeadName) {
		this.ledgerHeadName = ledgerHeadName;
		return this;
	}

	public void setLedgerHeadName(String ledgerHeadName) {
		this.ledgerHeadName = ledgerHeadName;
	}

	public String getLedgerHeadLongName() {
		return this.ledgerHeadLongName;
	}

	public SchoolLedgerHead ledgerHeadLongName(String ledgerHeadLongName) {
		this.ledgerHeadLongName = ledgerHeadLongName;
		return this;
	}

	public void setLedgerHeadLongName(String ledgerHeadLongName) {
		this.ledgerHeadLongName = ledgerHeadLongName;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public SchoolLedgerHead createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public SchoolLedgerHead lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public SchoolLedgerHead cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<ClassFee> getClassFees() {
		return this.classFees;
	}

	public SchoolLedgerHead classFees(Set<ClassFee> classFees) {
		this.setClassFees(classFees);
		return this;
	}

	public SchoolLedgerHead addClassFee(ClassFee classFee) {
		this.classFees.add(classFee);
		classFee.setSchoolLedgerHead(this);
		return this;
	}

	public SchoolLedgerHead removeClassFee(ClassFee classFee) {
		this.classFees.remove(classFee);
		classFee.setSchoolLedgerHead(null);
		return this;
	}

	public void setClassFees(Set<ClassFee> classFees) {
		if (this.classFees != null) {
			this.classFees.forEach(i -> i.setSchoolLedgerHead(null));
		}
		if (classFees != null) {
			classFees.forEach(i -> i.setSchoolLedgerHead(this));
		}
		this.classFees = classFees;
	}

	public Set<StudentDiscount> getStudentDiscounts() {
		return this.studentDiscounts;
	}

	public SchoolLedgerHead studentDiscounts(Set<StudentDiscount> studentDiscounts) {
		this.setStudentDiscounts(studentDiscounts);
		return this;
	}

	public SchoolLedgerHead addStudentDiscount(StudentDiscount studentDiscount) {
		this.studentDiscounts.add(studentDiscount);
		studentDiscount.setSchoolLedgerHead(this);
		return this;
	}

	public SchoolLedgerHead removeStudentDiscount(StudentDiscount studentDiscount) {
		this.studentDiscounts.remove(studentDiscount);
		studentDiscount.setSchoolLedgerHead(null);
		return this;
	}

	public void setStudentDiscounts(Set<StudentDiscount> studentDiscounts) {
		if (this.studentDiscounts != null) {
			this.studentDiscounts.forEach(i -> i.setSchoolLedgerHead(null));
		}
		if (studentDiscounts != null) {
			studentDiscounts.forEach(i -> i.setSchoolLedgerHead(this));
		}
		this.studentDiscounts = studentDiscounts;
	}

	public Set<StudentAdditionalCharges> getStudentAdditionalCharges() {
		return this.studentAdditionalCharges;
	}

	public SchoolLedgerHead studentAdditionalCharges(Set<StudentAdditionalCharges> studentAdditionalCharges) {
		this.setStudentAdditionalCharges(studentAdditionalCharges);
		return this;
	}

	public SchoolLedgerHead addStudentAdditionalCharges(StudentAdditionalCharges studentAdditionalCharges) {
		this.studentAdditionalCharges.add(studentAdditionalCharges);
		studentAdditionalCharges.setSchoolLedgerHead(this);
		return this;
	}

	public SchoolLedgerHead removeStudentAdditionalCharges(StudentAdditionalCharges studentAdditionalCharges) {
		this.studentAdditionalCharges.remove(studentAdditionalCharges);
		studentAdditionalCharges.setSchoolLedgerHead(null);
		return this;
	}

	public void setStudentAdditionalCharges(Set<StudentAdditionalCharges> studentAdditionalCharges) {
		if (this.studentAdditionalCharges != null) {
			this.studentAdditionalCharges.forEach(i -> i.setSchoolLedgerHead(null));
		}
		if (studentAdditionalCharges != null) {
			studentAdditionalCharges.forEach(i -> i.setSchoolLedgerHead(this));
		}
		this.studentAdditionalCharges = studentAdditionalCharges;
	}

	public Set<StudentChargesSummary> getStudentChargesSummaries() {
		return this.studentChargesSummaries;
	}

	public SchoolLedgerHead studentChargesSummaries(Set<StudentChargesSummary> studentChargesSummaries) {
		this.setStudentChargesSummaries(studentChargesSummaries);
		return this;
	}

	public SchoolLedgerHead addStudentChargesSummary(StudentChargesSummary studentChargesSummary) {
		this.studentChargesSummaries.add(studentChargesSummary);
		studentChargesSummary.setSchoolLedgerHead(this);
		return this;
	}

	public SchoolLedgerHead removeStudentChargesSummary(StudentChargesSummary studentChargesSummary) {
		this.studentChargesSummaries.remove(studentChargesSummary);
		studentChargesSummary.setSchoolLedgerHead(null);
		return this;
	}

	public void setStudentChargesSummaries(Set<StudentChargesSummary> studentChargesSummaries) {
		if (this.studentChargesSummaries != null) {
			this.studentChargesSummaries.forEach(i -> i.setSchoolLedgerHead(null));
		}
		if (studentChargesSummaries != null) {
			studentChargesSummaries.forEach(i -> i.setSchoolLedgerHead(this));
		}
		this.studentChargesSummaries = studentChargesSummaries;
	}

	public School getSchool() {
		return this.school;
	}

	public SchoolLedgerHead school(School school) {
		this.setSchool(school);
		return this;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

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
		if (!(o instanceof SchoolLedgerHead)) {
			return false;
		}
		return id != null && id.equals(((SchoolLedgerHead) o).id);
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
		return "SchoolLedgerHead{" + "id=" + getId() + ", studentLedgerHeadType='" + getStudentLedgerHeadType() + "'"
				+ ", ledgerHeadName='" + getLedgerHeadName() + "'" + ", ledgerHeadLongName='" + getLedgerHeadLongName()
				+ "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
