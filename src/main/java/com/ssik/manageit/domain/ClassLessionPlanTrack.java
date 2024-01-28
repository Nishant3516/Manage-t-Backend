package com.ssik.manageit.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.TaskStatus;

/**
 * A ClassLessionPlanTrack.
 */
@Entity
@Table(name = "class_lession_plan_track")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassLessionPlanTrack implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "work_status", nullable = false)
	private TaskStatus workStatus;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "create_date")
	private LocalDate createDate;

	@Column(name = "last_modified")
	private LocalDate lastModified;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = { "classLessionPlanTracks", "chapterSection", "schoolClass", "classSubject",
			"subjectChapter" }, allowSetters = true)
	private ClassLessionPlan classLessionPlan;

	// jhipster-needle-entity-add-field - JHipster will add fields here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClassLessionPlanTrack id(Long id) {
		this.id = id;
		return this;
	}

	public TaskStatus getWorkStatus() {
		return this.workStatus;
	}

	public ClassLessionPlanTrack workStatus(TaskStatus workStatus) {
		this.workStatus = workStatus;
		return this;
	}

	public void setWorkStatus(TaskStatus workStatus) {
		this.workStatus = workStatus;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public ClassLessionPlanTrack remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public ClassLessionPlanTrack createDate(LocalDate createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastModified() {
		return this.lastModified;
	}

	public ClassLessionPlanTrack lastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public LocalDate getCancelDate() {
		return this.cancelDate;
	}

	public ClassLessionPlanTrack cancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
		return this;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public ClassLessionPlan getClassLessionPlan() {
		return this.classLessionPlan;
	}

	public ClassLessionPlanTrack classLessionPlan(ClassLessionPlan classLessionPlan) {
		this.setClassLessionPlan(classLessionPlan);
		return this;
	}

	public void setClassLessionPlan(ClassLessionPlan classLessionPlan) {
		this.classLessionPlan = classLessionPlan;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassLessionPlanTrack)) {
			return false;
		}
		return id != null && id.equals(((ClassLessionPlanTrack) o).id);
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
		return "ClassLessionPlanTrack{" + "id=" + getId() + ", workStatus='" + getWorkStatus() + "'" + ", remarks='"
				+ getRemarks() + "'" + ", createDate='" + getCreateDate() + "'" + ", lastModified='" + getLastModified()
				+ "'" + ", cancelDate='" + getCancelDate() + "'" + "}";
	}
}
