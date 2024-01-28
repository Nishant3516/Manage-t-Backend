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
 * A SchoolVideoGallery.
 */
@Entity
@Table(name = "school_video_gallery")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolVideoGallery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "video_title", nullable = false)
    private String videoTitle;

    @Column(name = "video_description")
    private String videoDescription;

    @Lob
    @Column(name = "video_file", nullable = false)
    private byte[] videoFile;

    @Column(name = "video_file_content_type", nullable = false)
    private String videoFileContentType;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified")
    private LocalDate lastModified;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_school_video_gallery__school_class",
        joinColumns = @JoinColumn(name = "school_video_gallery_id"),
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

    public SchoolVideoGallery id(Long id) {
        this.id = id;
        return this;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public SchoolVideoGallery videoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
        return this;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return this.videoDescription;
    }

    public SchoolVideoGallery videoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
        return this;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public byte[] getVideoFile() {
        return this.videoFile;
    }

    public SchoolVideoGallery videoFile(byte[] videoFile) {
        this.videoFile = videoFile;
        return this;
    }

    public void setVideoFile(byte[] videoFile) {
        this.videoFile = videoFile;
    }

    public String getVideoFileContentType() {
        return this.videoFileContentType;
    }

    public SchoolVideoGallery videoFileContentType(String videoFileContentType) {
        this.videoFileContentType = videoFileContentType;
        return this;
    }

    public void setVideoFileContentType(String videoFileContentType) {
        this.videoFileContentType = videoFileContentType;
    }

    public String getVideoLink() {
        return this.videoLink;
    }

    public SchoolVideoGallery videoLink(String videoLink) {
        this.videoLink = videoLink;
        return this;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SchoolVideoGallery createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public SchoolVideoGallery lastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return this.cancelDate;
    }

    public SchoolVideoGallery cancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return this.schoolClasses;
    }

    public SchoolVideoGallery schoolClasses(Set<SchoolClass> schoolClasses) {
        this.setSchoolClasses(schoolClasses);
        return this;
    }

    public SchoolVideoGallery addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        schoolClass.getVchoolVideoGalleries().add(this);
        return this;
    }

    public SchoolVideoGallery removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        schoolClass.getVchoolVideoGalleries().remove(this);
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
        if (!(o instanceof SchoolVideoGallery)) {
            return false;
        }
        return id != null && id.equals(((SchoolVideoGallery) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolVideoGallery{" +
            "id=" + getId() +
            ", videoTitle='" + getVideoTitle() + "'" +
            ", videoDescription='" + getVideoDescription() + "'" +
            ", videoFile='" + getVideoFile() + "'" +
            ", videoFileContentType='" + getVideoFileContentType() + "'" +
            ", videoLink='" + getVideoLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            "}";
    }
}
