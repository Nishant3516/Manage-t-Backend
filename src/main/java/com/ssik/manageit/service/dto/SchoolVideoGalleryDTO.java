package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolVideoGallery} entity.
 */
public class SchoolVideoGalleryDTO implements Serializable {

    private Long id;

    @NotNull
    private String videoTitle;

    private String videoDescription;

    @Lob
    private byte[] videoFile;

    private String videoFileContentType;
    private String videoLink;

    private LocalDate createDate;

    private LocalDate lastModified;

    private LocalDate cancelDate;

    private Set<SchoolClassDTO> schoolClasses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public byte[] getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(byte[] videoFile) {
        this.videoFile = videoFile;
    }

    public String getVideoFileContentType() {
        return videoFileContentType;
    }

    public void setVideoFileContentType(String videoFileContentType) {
        this.videoFileContentType = videoFileContentType;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<SchoolClassDTO> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(Set<SchoolClassDTO> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolVideoGalleryDTO)) {
            return false;
        }

        SchoolVideoGalleryDTO schoolVideoGalleryDTO = (SchoolVideoGalleryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolVideoGalleryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolVideoGalleryDTO{" +
            "id=" + getId() +
            ", videoTitle='" + getVideoTitle() + "'" +
            ", videoDescription='" + getVideoDescription() + "'" +
            ", videoFile='" + getVideoFile() + "'" +
            ", videoLink='" + getVideoLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            "}";
    }
}
