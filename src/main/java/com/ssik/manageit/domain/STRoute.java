package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A STRoute.
 */
@Entity
@Table(name = "st_route")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class STRoute implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "transport_route_name", nullable = false)
	private String transportRouteName;

	@NotNull
	@Column(name = "route_charge", nullable = false)
	private Double routeCharge;

	@Column(name = "transport_route_address")
	private String transportRouteAddress;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@Column(name = "remarks")
	private String remarks;

	@OneToMany(mappedBy = "stRoute")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "classStudent", "stRoute", "operatedBy" }, allowSetters = true)
	private Set<STIncomeExpenses> sTIncomeExpenses = new HashSet<>();
	@ManyToOne
	private School school;

	@ManyToOne
	private Tenant tenant;

	// jhipster-needle-entity-add-field - JHipster will add fields here

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

	public Long getId() {
		return this.id;
	}

	public STRoute id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransportRouteName() {
		return this.transportRouteName;
	}

	public STRoute transportRouteName(String transportRouteName) {
		this.setTransportRouteName(transportRouteName);
		return this;
	}

	public void setTransportRouteName(String transportRouteName) {
		this.transportRouteName = transportRouteName;
	}

	public Double getRouteCharge() {
		return this.routeCharge;
	}

	public STRoute routeCharge(Double routeCharge) {
		this.setRouteCharge(routeCharge);
		return this;
	}

	public void setRouteCharge(Double routeCharge) {
		this.routeCharge = routeCharge;
	}

	public String getTransportRouteAddress() {
		return this.transportRouteAddress;
	}

	public STRoute transportRouteAddress(String transportRouteAddress) {
		this.setTransportRouteAddress(transportRouteAddress);
		return this;
	}

	public void setTransportRouteAddress(String transportRouteAddress) {
		this.transportRouteAddress = transportRouteAddress;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public STRoute contactNumber(String contactNumber) {
		this.setContactNumber(contactNumber);
		return this;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public STRoute createDate(LocalDate createDate) {
		this.setCreateDate(createDate);
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public STRoute cancelDate(LocalDate cancelDate) {
		this.setCancelDate(cancelDate);
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public STRoute remarks(String remarks) {
		this.setRemarks(remarks);
		return this;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Set<STIncomeExpenses> getSTIncomeExpenses() {
		return this.sTIncomeExpenses;
	}

	public void setSTIncomeExpenses(Set<STIncomeExpenses> sTIncomeExpenses) {
		if (this.sTIncomeExpenses != null) {
			this.sTIncomeExpenses.forEach(i -> i.setStRoute(null));
		}
		if (sTIncomeExpenses != null) {
			sTIncomeExpenses.forEach(i -> i.setStRoute(this));
		}
		this.sTIncomeExpenses = sTIncomeExpenses;
	}

	public STRoute sTIncomeExpenses(Set<STIncomeExpenses> sTIncomeExpenses) {
		this.setSTIncomeExpenses(sTIncomeExpenses);
		return this;
	}

	public STRoute addSTIncomeExpenses(STIncomeExpenses sTIncomeExpenses) {
		this.sTIncomeExpenses.add(sTIncomeExpenses);
		sTIncomeExpenses.setStRoute(this);
		return this;
	}

	public STRoute removeSTIncomeExpenses(STIncomeExpenses sTIncomeExpenses) {
		this.sTIncomeExpenses.remove(sTIncomeExpenses);
		sTIncomeExpenses.setStRoute(null);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof STRoute)) {
			return false;
		}
		return id != null && id.equals(((STRoute) o).id);
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
		return "STRoute{" + "id=" + getId() + ", transportRouteName='" + getTransportRouteName() + "'"
				+ ", routeCharge=" + getRouteCharge() + ", transportRouteAddress='" + getTransportRouteAddress() + "'"
				+ ", contactNumber='" + getContactNumber() + "'" + ", createDate='" + getCreateDate() + "'"
				+ ", cancelDate='" + getCancelDate() + "'" + ", remarks='" + getRemarks() + "'" + "}";
	}
}
