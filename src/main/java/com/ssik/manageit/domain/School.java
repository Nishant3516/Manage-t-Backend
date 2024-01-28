package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A School.
 */
@Entity
@Table(name = "school")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class School implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "group_name", nullable = false)
	private String groupName;

	@NotNull
	@Column(name = "school_name", nullable = false)
	private String schoolName;

	@Column(name = "address")
	private String address;

	@Column(name = "affl_number")
	private String afflNumber;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToOne
	private Tenant tenant;

	@OneToMany(mappedBy = "school")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudents", "classLessionPlans", "school", "schoolNotifications", "classFees",
			"classSubjects", "schoolUsers", "schoolDaysOffs", "schoolEvents", "schoolPictureGalleries",
			"vchoolVideoGalleries", "schoolReports", }, allowSetters = true)
	private Set<SchoolClass> schoolClasses = new HashSet<>();

	@OneToMany(mappedBy = "school")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classFees", "studentDiscounts", "studentAdditionalCharges",
			"studentChargesSummaries", "school" }, allowSetters = true)
	private Set<SchoolLedgerHead> schoolLedgerHeads = new HashSet<>();

	@OneToMany(mappedBy = "school")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "school" }, allowSetters = true)
	private Set<IdStore> idStores = new HashSet<>();

	@OneToMany(mappedBy = "school")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "school", "schoolUser" }, allowSetters = true)
	private Set<AuditLog> auditLogs = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public School id(Long id) {
		this.id = id;
		return this;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public School groupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getSchoolName() {
		return this.schoolName;
	}

	public School schoolName(String schoolName) {
		this.schoolName = schoolName;
		return this;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getAddress() {
		return this.address;
	}

	public School address(String address) {
		this.address = address;
		return this;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAfflNumber() {
		return this.afflNumber;
	}

	public School afflNumber(String afflNumber) {
		this.afflNumber = afflNumber;
		return this;
	}

	public void setAfflNumber(String afflNumber) {
		this.afflNumber = afflNumber;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public School createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public School lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public School cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Set<SchoolClass> getSchoolClasses() {
		return this.schoolClasses;
	}

	public School schoolClasses(Set<SchoolClass> schoolClasses) {
		this.setSchoolClasses(schoolClasses);
		return this;
	}

	public School addSchoolClass(SchoolClass schoolClass) {
		this.schoolClasses.add(schoolClass);
		schoolClass.setSchool(this);
		return this;
	}

	public School removeSchoolClass(SchoolClass schoolClass) {
		this.schoolClasses.remove(schoolClass);
		schoolClass.setSchool(null);
		return this;
	}

	public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
		if (this.schoolClasses != null) {
			this.schoolClasses.forEach(i -> i.setSchool(null));
		}
		if (schoolClasses != null) {
			schoolClasses.forEach(i -> i.setSchool(this));
		}
		this.schoolClasses = schoolClasses;
	}

	public Set<SchoolLedgerHead> getSchoolLedgerHeads() {
		return this.schoolLedgerHeads;
	}

	public School schoolLedgerHeads(Set<SchoolLedgerHead> schoolLedgerHeads) {
		this.setSchoolLedgerHeads(schoolLedgerHeads);
		return this;
	}

	public School addSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.schoolLedgerHeads.add(schoolLedgerHead);
		schoolLedgerHead.setSchool(this);
		return this;
	}

	public School removeSchoolLedgerHead(SchoolLedgerHead schoolLedgerHead) {
		this.schoolLedgerHeads.remove(schoolLedgerHead);
		schoolLedgerHead.setSchool(null);
		return this;
	}

	public void setSchoolLedgerHeads(Set<SchoolLedgerHead> schoolLedgerHeads) {
		if (this.schoolLedgerHeads != null) {
			this.schoolLedgerHeads.forEach(i -> i.setSchool(null));
		}
		if (schoolLedgerHeads != null) {
			schoolLedgerHeads.forEach(i -> i.setSchool(this));
		}
		this.schoolLedgerHeads = schoolLedgerHeads;
	}

	public Set<IdStore> getIdStores() {
		return this.idStores;
	}

	public School idStores(Set<IdStore> idStores) {
		this.setIdStores(idStores);
		return this;
	}

	public School addIdStore(IdStore idStore) {
		this.idStores.add(idStore);
		idStore.setSchool(this);
		return this;
	}

	public School removeIdStore(IdStore idStore) {
		this.idStores.remove(idStore);
		idStore.setSchool(null);
		return this;
	}

	public void setIdStores(Set<IdStore> idStores) {
		if (this.idStores != null) {
			this.idStores.forEach(i -> i.setSchool(null));
		}
		if (idStores != null) {
			idStores.forEach(i -> i.setSchool(this));
		}
		this.idStores = idStores;
	}

	public Set<AuditLog> getAuditLogs() {
		return this.auditLogs;
	}

	public School auditLogs(Set<AuditLog> auditLogs) {
		this.setAuditLogs(auditLogs);
		return this;
	}

	public School addAuditLog(AuditLog auditLog) {
		this.auditLogs.add(auditLog);
		auditLog.setSchool(this);
		return this;
	}

	public School removeAuditLog(AuditLog auditLog) {
		this.auditLogs.remove(auditLog);
		auditLog.setSchool(null);
		return this;
	}

	public void setAuditLogs(Set<AuditLog> auditLogs) {
		if (this.auditLogs != null) {
			this.auditLogs.forEach(i -> i.setSchool(null));
		}
		if (auditLogs != null) {
			auditLogs.forEach(i -> i.setSchool(this));
		}
		this.auditLogs = auditLogs;
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
		if (!(o instanceof School)) {
			return false;
		}
		return id != null && id.equals(((School) o).id);
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
		return "School{" + "id=" + getId() + ", groupName='" + getGroupName() + "'" + ", schoolName='" + getSchoolName()
				+ "'" + ", address='" + getAddress() + "'" + ", afflNumber='" + getAfflNumber() + "'" + ", createDate='"
				+ getCreateDate() + "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='"
				+ getCancelDate() + "'" + "}";
	}
}
