package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AuditLog.
 */
@Entity
@Table(name = "audit_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditLog implements Serializable {

	private static final long serialVersionUID = 1L;

	public AuditLog() {

	}

	public AuditLog(String action, LocalDate lastModifiedDate, String userName, School school, String newData,
			String oldData, String entityName) {
		this.action = action;
		this.lastModified = lastModifiedDate;
		this.userName = userName;
		this.school = school;
		this.data1 = newData;
		this.data2 = oldData;
		this.data3 = entityName;
		this.userDeviceDetails = "Browser";

	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Column(name = "user_name", nullable = false)
	private String userName;

	@NotNull
	@Column(name = "user_device_details", nullable = false)
	private String userDeviceDetails;

	@Column(name = "action")
	private String action;

	@Column(name = "data_1")
	private String data1;

	@Column(name = "data_2")
	private String data2;

	@Column(name = "data_3")
	private String data3;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToOne
	@JsonIgnoreProperties(value = { "schoolClasses", "schoolLedgerHeads", "idStores",
			"auditLogs" }, allowSetters = true)
	private School school;

	@ManyToOne
	@JsonIgnoreProperties(value = { "auditLogs", "schoolClasses", "classSubjects" }, allowSetters = true)
	private SchoolUser schoolUser;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuditLog id(Long id) {
		this.id = id;
		return this;
	}

	public String getUserName() {
		return this.userName;
	}

	public AuditLog userName(String userName) {
		this.userName = userName;
		return this;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDeviceDetails() {
		return this.userDeviceDetails;
	}

	public AuditLog userDeviceDetails(String userDeviceDetails) {
		this.userDeviceDetails = userDeviceDetails;
		return this;
	}

	public void setUserDeviceDetails(String userDeviceDetails) {
		this.userDeviceDetails = userDeviceDetails;
	}

	public String getAction() {
		return this.action;
	}

	public AuditLog action(String action) {
		this.action = action;
		return this;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getData1() {
		return this.data1;
	}

	public AuditLog data1(String data1) {
		this.data1 = data1;
		return this;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return this.data2;
	}

	public AuditLog data2(String data2) {
		this.data2 = data2;
		return this;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return this.data3;
	}

	public AuditLog data3(String data3) {
		this.data3 = data3;
		return this;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public AuditLog createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public AuditLog lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public AuditLog cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public School getSchool() {
		return this.school;
	}

	public AuditLog school(School school) {
		this.setSchool(school);
		return this;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public SchoolUser getSchoolUser() {
		return this.schoolUser;
	}

	public AuditLog schoolUser(SchoolUser schoolUser) {
		this.setSchoolUser(schoolUser);
		return this;
	}

	public void setSchoolUser(SchoolUser schoolUser) {
		this.schoolUser = schoolUser;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AuditLog)) {
			return false;
		}
		return id != null && id.equals(((AuditLog) o).id);
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
		return "AuditLog{" + "id=" + getId() + ", userName='" + getUserName() + "'" + ", userDeviceDetails='"
				+ getUserDeviceDetails() + "'" + ", action='" + getAction() + "'" + ", data1='" + getData1() + "'"
				+ ", data2='" + getData2() + "'" + ", data3='" + getData3() + "'" + ", createDate='" + getCreateDate()
				+ "'" + ", lastModified='" + getLastModified() + "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
