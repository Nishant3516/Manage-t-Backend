package com.ssik.manageit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ssik.manageit.domain.SchoolPictureGallery} entity.
 */
public class SchoolPictureGalleryDTO implements Serializable {

    private Long id;

    @NotNull
    private String pictureTitle;

    private String pictureDescription;

    @Lob
    private byte[] pictureFile;

    private String pictureFileContentType;
    private String pictureLink;

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

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public byte[] getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(byte[] pictureFile) {
        this.pictureFile = pictureFile;
    }

    public String getPictureFileContentType() {
        return pictureFileContentType;
    }

    public void setPictureFileContentType(String pictureFileContentType) {
        this.pictureFileContentType = pictureFileContentType;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
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
        if (!(o instanceof SchoolPictureGalleryDTO)) {
            return false;
        }

        SchoolPictureGalleryDTO schoolPictureGalleryDTO = (SchoolPictureGalleryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolPictureGalleryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolPictureGalleryDTO{" +
            "id=" + getId() +
            ", pictureTitle='" + getPictureTitle() + "'" +
            ", pictureDescription='" + getPictureDescription() + "'" +
            ", pictureFile='" + getPictureFile() + "'" +
            ", pictureLink='" + getPictureLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", schoolClasses=" + getSchoolClasses() +
            "}";
    }
}
