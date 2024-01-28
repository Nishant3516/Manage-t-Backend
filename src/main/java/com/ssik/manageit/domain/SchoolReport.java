package com.ssik.manageit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssik.manageit.domain.enumeration.ReportType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchoolReport.
 */
@Entity
@Table(name = "school_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column(name = "report_file")
    private byte[] reportFile;

    @Column(name = "report_file_content_type")
    private String reportFileContentType;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_report__school_class",
        joinColumns = @JoinColumn(name = "school_report_id"),
        inverseJoinColumns = @JoinColumn(name = "school_class_id")
    )
    @JsonIgnoreProperties(
        value = {
            "classStudents",
            "classLessionPlans",
            "school",
            "schoolNotifications",
            "classFees",
            "classSubjects",
            "schoolUsers",
            "schoolDaysOffs",
            "schoolEvents",
            "schoolPictureGalleries",
            "vchoolVideoGalleries",
            "schoolReports",
        },
        allowSetters = true
    )
    private Set<SchoolClass> schoolClasses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolReport id(Long id) {
        this.id = id;
        return this;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public SchoolReport reportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public SchoolReport startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public SchoolReport endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public byte[] getReportFile() {
        return this.reportFile;
    }

    public SchoolReport reportFile(byte[] reportFile) {
        this.reportFile = reportFile;
        return this;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return this.reportFileContentType;
    }

    public SchoolReport reportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
        return this;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolReport createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolReport lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolReport cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public SchoolReport schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public SchoolReport addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getSchoolReports().add(this);
        return this;
    }

    public SchoolReport removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getSchoolReports().remove(this);
        return this;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolReport)) {
            return false;
        }
        return id != null && id.equals(((SchoolReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolReport{" +
            "id=" + getId() +
            ", reportType='" + getReportType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileContentType='" + getReportFileContentType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
