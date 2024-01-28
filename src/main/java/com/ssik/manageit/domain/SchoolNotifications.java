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
 * A SchoolNotifications.
 */
@Entity
@Table(name = "school_notifications")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolNotifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "notification_title", nullable = false)
    private String notificationTitle;

    @NotNull
    @Column(name = "notification_details", nullable = false)
    private String notificationDetails;

    @Lob
    @Column(name = "notification_file")
    private byte[] notificationFile;

    @Column(name = "notification_file_content_type")
    private String notificationFileContentType;

    @Column(name = "notification_file_link")
    private String notificationFileLink;

    @Column(name = "show_till_date")
    private LocalDate showTillDate;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_notifications__school_class",
        joinColumns = @JoinColumn(name = "school_notifications_id"),
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

    public SchoolNotifications id(Long id) {
        this.id = id;
        return this;
    }

    public String getNotificationTitle() {
        return this.notificationTitle;
    }

    public SchoolNotifications notificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
        return this;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDetails() {
        return this.notificationDetails;
    }

    public SchoolNotifications notificationDetails(String notificationDetails) {
        this.notificationDetails = notificationDetails;
        return this;
    }

    public void setNotificationDetails(String notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public byte[] getNotificationFile() {
        return this.notificationFile;
    }

    public SchoolNotifications notificationFile(byte[] notificationFile) {
        this.notificationFile = notificationFile;
        return this;
    }

    public void setNotificationFile(byte[] notificationFile) {
        this.notificationFile = notificationFile;
    }

    public String getNotificationFileContentType() {
        return this.notificationFileContentType;
    }

    public SchoolNotifications notificationFileContentType(String notificationFileContentType) {
        this.notificationFileContentType = notificationFileContentType;
        return this;
    }

    public void setNotificationFileContentType(String notificationFileContentType) {
        this.notificationFileContentType = notificationFileContentType;
    }

    public String getNotificationFileLink() {
        return this.notificationFileLink;
    }

    public SchoolNotifications notificationFileLink(String notificationFileLink) {
        this.notificationFileLink = notificationFileLink;
        return this;
    }

    public void setNotificationFileLink(String notificationFileLink) {
        this.notificationFileLink = notificationFileLink;
    }

    public LocalDate getShowTillDate() {
        return this.showTillDate;
    }

    public SchoolNotifications showTillDate(LocalDate showTillDate) {
        this.showTillDate = showTillDate;
        return this;
    }

    public void setShowTillDate(LocalDate showTillDate) {
        this.showTillDate = showTillDate;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolNotifications createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolNotifications lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolNotifications cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public SchoolNotifications schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public SchoolNotifications addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getSchoolNotifications().add(this);
        return this;
    }

    public SchoolNotifications removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getSchoolNotifications().remove(this);
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
        if (!(o instanceof SchoolNotifications)) {
            return false;
        }
        return id != null && id.equals(((SchoolNotifications) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolNotifications{" +
            "id=" + getId() +
            ", notificationTitle='" + getNotificationTitle() + "'" +
            ", notificationDetails='" + getNotificationDetails() + "'" +
            ", notificationFile='" + getNotificationFile() + "'" +
            ", notificationFileContentType='" + getNotificationFileContentType() + "'" +
            ", notificationFileLink='" + getNotificationFileLink() + "'" +
            ", showTillDate='" + getShowTillDate() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
