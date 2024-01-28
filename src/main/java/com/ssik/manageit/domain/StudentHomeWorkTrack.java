package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.WorkStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentHomeWorkTrack.
 */
@Entity
@Table(name = "student_home_work_track")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentHomeWorkTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "work_status", nullable = false)
    private WorkStatus workStatus;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "studentDiscounts",
            "studentAdditionalCharges",
            "studentChargesSummaries",
            "studentPayments",
            "studentAttendences",
            "studentHomeWorkTracks",
            "studentClassWorkTracks",
            "schoolClass",
        },
        allowSetters = true
    )
    private ClassStudent classStudent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "studentHomeWorkTracks", "chapterSection" }, allowSetters = true)
    private ClassHomeWork classHomeWork;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentHomeWorkTrack id(Long id) {
        this.id = id;
        return this;
    }

    public WorkStatus getWorkStatus() {
        return this.workStatus;
    }

    public StudentHomeWorkTrack workStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
        return this;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public StudentHomeWorkTrack remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public StudentHomeWorkTrack createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public StudentHomeWorkTrack lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public StudentHomeWorkTrack cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ClassStudent getClassStudent() {
        return this.classStudent;
    }

    public StudentHomeWorkTrack classStudent(ClassStudent classStudent) {
        this.setClassStudent(classStudent);
        return this;
    }

    public void setClassStudent(ClassStudent classStudent) {
        this.classStudent = classStudent;
    }

    public ClassHomeWork getClassHomeWork() {
        return this.classHomeWork;
    }

    public StudentHomeWorkTrack classHomeWork(ClassHomeWork classHomeWork) {
        this.setClassHomeWork(classHomeWork);
        return this;
    }

    public void setClassHomeWork(ClassHomeWork classHomeWork) {
        this.classHomeWork = classHomeWork;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentHomeWorkTrack)) {
            return false;
        }
        return id != null && id.equals(((StudentHomeWorkTrack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentHomeWorkTrack{" +
            "id=" + getId() +
            ", workStatus='" + getWorkStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
