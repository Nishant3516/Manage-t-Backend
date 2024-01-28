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
 * A SchoolPictureGallery.
 */
@Entity
@Table(name = "school_picture_gallery")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolPictureGallery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "picture_title", nullable = false)
    private String pictureTitle;

    @Column(name = "picture_description")
    private String pictureDescription;

    @Lob
    @Column(name = "picture_file", nullable = false)
    private byte[] pictureFile;

    @Column(name = "picture_file_content_type", nullable = false)
    private String pictureFileContentType;

    @Column(name = "picture_link")
    private String pictureLink;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_picture_gallery__school_class",
        joinColumns = @JoinColumn(name = "school_picture_gallery_id"),
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

    public SchoolPictureGallery id(Long id) {
        this.id = id;
        return this;
    }

    public String getPictureTitle() {
        return this.pictureTitle;
    }

    public SchoolPictureGallery pictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
        return this;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPictureDescription() {
        return this.pictureDescription;
    }

    public SchoolPictureGallery pictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
        return this;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public byte[] getPictureFile() {
        return this.pictureFile;
    }

    public SchoolPictureGallery pictureFile(byte[] pictureFile) {
        this.pictureFile = pictureFile;
        return this;
    }

    public void setPictureFile(byte[] pictureFile) {
        this.pictureFile = pictureFile;
    }

    public String getPictureFileContentType() {
        return this.pictureFileContentType;
    }

    public SchoolPictureGallery pictureFileContentType(String pictureFileContentType) {
        this.pictureFileContentType = pictureFileContentType;
        return this;
    }

    public void setPictureFileContentType(String pictureFileContentType) {
        this.pictureFileContentType = pictureFileContentType;
    }

    public String getPictureLink() {
        return this.pictureLink;
    }

    public SchoolPictureGallery pictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
        return this;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolPictureGallery createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolPictureGallery lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolPictureGallery cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public SchoolPictureGallery schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public SchoolPictureGallery addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getSchoolPictureGalleries().add(this);
        return this;
    }

    public SchoolPictureGallery removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getSchoolPictureGalleries().remove(this);
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
        if (!(o instanceof SchoolPictureGallery)) {
            return false;
        }
        return id != null && id.equals(((SchoolPictureGallery) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolPictureGallery{" +
            "id=" + getId() +
            ", pictureTitle='" + getPictureTitle() + "'" +
            ", pictureDescription='" + getPictureDescription() + "'" +
            ", pictureFile='" + getPictureFile() + "'" +
            ", pictureFileContentType='" + getPictureFileContentType() + "'" +
            ", pictureLink='" + getPictureLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
